package hanniejewelry.vn.product.service;

import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.PagedList;
import com.blazebit.persistence.view.EntityViewManager;
import com.blazebit.persistence.view.EntityViewSetting;
import hanniejewelry.vn.product.entity.InventoryAdvance;
import hanniejewelry.vn.product.entity.Product;
import hanniejewelry.vn.product.entity.ProductVariant;
import hanniejewelry.vn.product.dto.CollectionModel;
import hanniejewelry.vn.product.dto.ProductRequest;
import hanniejewelry.vn.product.dto.SimpleIdNameDTO;
import hanniejewelry.vn.product.dto.ProductUpdateRequest;
import hanniejewelry.vn.product.entity.*;
import hanniejewelry.vn.shared.dto.GenericFilterRequest;
import hanniejewelry.vn.shared.exception.BaseMessageType;
import hanniejewelry.vn.shared.exception.BizException;
import hanniejewelry.vn.shared.exception.ResourceNotFoundException;
import hanniejewelry.vn.shared.filter.GenericBlazeFilterApplier;
import hanniejewelry.vn.shared.utils.PositionUtils;
import hanniejewelry.vn.shared.utils.SlugUtils;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import hanniejewelry.vn.product.repository.CustomCollectionRepository;
import hanniejewelry.vn.product.repository.ProductRepository;
import hanniejewelry.vn.product.view.ProductView;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Collectors;

import io.vavr.collection.List;
import io.vavr.control.Option;

import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;
    private final CustomCollectionRepository customCollectionRepository;
    private final EntityViewManager evm;
    private final CriteriaBuilderFactory cbf;
    private final EntityManager em;
    private final GenericBlazeFilterApplier<ProductView> filterApplier = new GenericBlazeFilterApplier<>();

    public ProductView createProduct(ProductRequest request) {
        log.info("Creating new product: {}", request.getTitle());
        log.debug("Product request details: {}", request);
        
        Product product = Product.builder()
                .title(request.getTitle())
                .bodyHtml(request.getBodyHtml())
                .bodyPlain(request.getBodyPlain())
                .vendor(request.getVendor())
                .productType(request.getProductType())
                .tags(request.getTags())
                .templateSuffix(request.getTemplateSuffix())
                .publishedAt(request.getPublishedAt())
                .publishedScope(request.getPublishedScope())
                .onlyHideFromList(request.isOnlyHideFromList())
                .notAllowPromotion(request.isNotAllowPromotion())
                .handle(SlugUtils.toSlugUnique(request.getTitle(), productRepository::existsByHandle))
                .variants(
                        Option.of(request.getVariants())
                                .map(List::ofAll)
                                .getOrElse(List.empty())
                                .zipWithIndex()
                                .<ProductVariant>map(tuple -> {
                                    log.debug("Creating variant with SKU: {}", tuple._1.getSku());
                                    
                                    Integer inventoryQuantity = tuple._1.getInventoryQuantity();
                                    
                                    String inventoryManagement = tuple._1.isTrackingInventory() ?
                                            "nguyenhauweb" : "";
                                    
                                    InventoryAdvance inventoryAdvance = null;
                                    if (tuple._1.isTrackingInventory()) {
                                        inventoryAdvance = InventoryAdvance.builder()
                                                .qtyAvailable(inventoryQuantity)
                                                .qtyOnhand(inventoryQuantity)
                                                .qtyCommited(0)
                                                .qtyIncoming(0)
                                                .build();
                                    }
                                    
                                    return ProductVariant.builder()
                                        .title(tuple._1.getTitle())
                                        .price(tuple._1.getPrice())
                                        .compareAtPrice(tuple._1.getCompareAtPrice())
                                        .costPrice(tuple._1.getCostPrice())
                                        .sku(tuple._1.getSku())
                                        .barcode(tuple._1.getBarcode())
                                        .grams(tuple._1.getGrams())
                                        .position(tuple._2 + 1)
                                        .inventoryQuantity(inventoryQuantity)
                                        .inventoryPolicy(tuple._1.getInventoryPolicy())
                                        .inventoryManagement(inventoryManagement)
                                        .isTrackingInventory(tuple._1.isTrackingInventory())
                                        .allowPurchaseWhenSoldOut(tuple._1.isAllowPurchaseWhenSoldOut())
                                        .requiresShipping(tuple._1.isRequiresShipping())
                                        .taxable(tuple._1.isTaxable())
                                        .option1(tuple._1.getOption1())
                                        .option2(tuple._1.getOption2())
                                        .option3(tuple._1.getOption3())
                                        .inventoryAdvance(inventoryAdvance)
                                        .build();
                                })
                                .toJavaList()
                )
                .options(
                        Option.of(request.getOptions())
                                .map(List::ofAll)
                                .getOrElse(List.empty())
                                .zipWithIndex()
                                .<ProductOption>map(tuple -> {
                                    log.debug("Creating option: {}", tuple._1.getName());
                                    return ProductOption.builder()
                                        .name(tuple._1.getName())
                                        .position(tuple._2 + 1)
                                        .build();
                                })
                                .toJavaList()
                )
                .build();

        product.getVariants().forEach(v -> v.setProduct(product));
        product.getOptions().forEach(o -> o.setProduct(product));

        log.debug("Saving product with {} variants and {} options", 
                product.getVariants().size(), product.getOptions().size());
        
        Product savedProduct = productRepository.save(product);
        productRepository.flush();
        
        processCollections(request, savedProduct);
        
        log.info("Product created successfully with ID: {}", savedProduct.getId());
        
        ProductView productView = evm.find(em, ProductView.class, savedProduct.getId());
        
        if (productView != null) {
            log.debug("Product view loaded successfully");
            if (product.getVariants().size() > 0 && 
                (productView.getVariants() == null || productView.getVariants().isEmpty())) {
                log.warn("Data inconsistency detected: Product variants missing in view for product ID: {}", 
                        savedProduct.getId());
            }
        } else {
            log.warn("Failed to load product view for newly created product ID: {}", savedProduct.getId());
        }
        
        return productView;
    }
    
    private void processCollections(ProductRequest request, Product product) {
        java.util.List<String> collectionIdList = new ArrayList<>();
        
        if (request.getCollectionModel() != null && !request.getCollectionModel().isEmpty()) {
            for (CollectionModel model : request.getCollectionModel()) {
                if (model.getCollectionIds() != null && !model.getCollectionIds().isEmpty()) {
                    collectionIdList.add(model.getCollectionIds());
                }
            }
        }
        
        if (request.getCollectionIds() != null && !request.getCollectionIds().isEmpty()) {
            String[] ids = request.getCollectionIds().split(",");
            for (String id : ids) {
                String trimmedId = id.trim();
                if (!collectionIdList.contains(trimmedId)) {
                    collectionIdList.add(trimmedId);
                }
            }
        }
        
        if (!collectionIdList.isEmpty()) {
            // Create new collects list
            java.util.List<Collect> newCollects = new ArrayList<>();
            int position = 1;
            
            for (String collectionIdStr : collectionIdList) {
                try {
                    UUID collectionId = UUID.fromString(collectionIdStr);
                    CustomCollection collection = customCollectionRepository.findById(collectionId)
                            .orElse(null);
                    
                    if (collection != null) {
                        log.info("Associating product with collection: {}", collection.getTitle());
                        Collect collect = Collect.builder()
                                .collection(collection)
                                .product(product)
                                .position(position++)
                                .build();
                        newCollects.add(collect);
                    } else {
                        log.warn("Collection with ID {} not found", collectionIdStr);
                    }
                } catch (IllegalArgumentException e) {
                    log.warn("Invalid collection ID format: {}", collectionIdStr);
                }
            }
            
            if (product.getCollects() != null) {
                product.getCollects().clear();
            }
            
            if (!newCollects.isEmpty()) {
                product.getCollects().addAll(newCollects);
                productRepository.save(product);
                log.info("Product associated with {} collections", newCollects.size());
            } else {
                log.info("No valid collections found to associate with the product");
            }
        } else {
            if (product.getCollects() != null && !product.getCollects().isEmpty()) {
                product.getCollects().clear();
                productRepository.save(product);
            }
            log.info("No collection IDs provided, product will not be associated with any collections");
        }
    }

    public List<SimpleIdNameDTO> getAllVendors() {
        log.info("Fetching all product vendors");
        
        List<SimpleIdNameDTO> vendors = List.ofAll(
                cbf.create(em, SimpleIdNameDTO.class)
                        .from(Product.class, "p")
                        .selectNew(SimpleIdNameDTO.class)
                        .with("p.vendor")
                        .with("p.vendor")
                        .end()
                        .where("p.vendor").isNotNull()
                        .where("p.vendor").notEq("")
                        .distinct()
                        .getResultList()
        );
        
        log.info("Found {} distinct vendors", vendors.size());
        return vendors;
    }

    public List<SimpleIdNameDTO> getAllProductTypes() {
        log.info("Fetching all product types");
        
        List<SimpleIdNameDTO> productTypes = List.ofAll(
                cbf.create(em, SimpleIdNameDTO.class)
                        .from(Product.class, "p")
                        .selectNew(SimpleIdNameDTO.class)
                        .with("p.productType") // id
                        .with("p.productType") // name
                        .end()
                        .where("p.productType").isNotNull()
                        .where("p.productType").notEq("")
                        .distinct()
                        .getResultList()
        );
        
        log.info("Found {} distinct product types", productTypes.size());
        return productTypes;
    }

    public ProductView getProduct(UUID id) {
        log.info("Fetching product with ID: {}", id);
        
        Product product = productRepository.findById(id).orElseThrow(() -> {
            log.warn("Product not found with ID: {}", id);
            return new IllegalArgumentException("Product not found with ID: " + id);
        });
        
        ProductView productView = evm.find(em, ProductView.class, id);
        
        if (productView != null) {
            log.debug("Product view loaded successfully");
            if (product.getVariants().size() > 0 && 
                (productView.getVariants() == null || productView.getVariants().isEmpty())) {
                log.warn("Data inconsistency detected: Product variants missing in view for product ID: {}", id);
            }
        } else {
            log.warn("Failed to load product view for product ID: {}", id);
        }
        
        return productView;
    }

    
    public List<SimpleIdNameDTO> getAllProductTags() {
        log.info("Fetching all product tags");
        
        java.util.List<String> allTags = productRepository.findAll().stream()
                .filter(p -> p.getTags() != null && !p.getTags().isEmpty())
                .flatMap(p -> Arrays.stream(p.getTags().split(",")))
                .map(String::trim)
                .filter(tag -> !tag.isEmpty())
                .distinct()
                .collect(Collectors.toList());
        
        List<SimpleIdNameDTO> tagDTOs = List.ofAll(allTags)
                .map(tag -> {
                    SimpleIdNameDTO dto = new SimpleIdNameDTO();
                    dto.setId(tag);
                    dto.setName(tag);
                    return dto;
                });
        
        log.info("Found {} distinct tags", tagDTOs.size());
        return tagDTOs;
    }


    public PagedList<ProductView> getFilteredProducts(GenericFilterRequest filter, String collectionHandle) {
        var setting = EntityViewSetting.create(ProductView.class, filter.getPage(), filter.getSize());
        filterApplier.applyFilters(setting, filter);
        
        var cb = cbf.create(em, Product.class, "p");
        
        if (StringUtils.hasText(collectionHandle)) {
            CustomCollection collection = customCollectionRepository.findByHandle(collectionHandle)
                    .orElseThrow(() -> new BizException(BaseMessageType.NOT_FOUND,
                            "Collection not found with handle: {0}", collectionHandle));
            
            cb.whereNotExists()
                .from(Collect.class, "c")
                .where("c.collection.id").eq(collection.getId())
                .where("c.product.id").eqExpression("p.id")
                .end();
                
        }
        
        filterApplier.applySort(cb, filter);
        
        PagedList<ProductView> result = evm.applySetting(setting, cb).getResultList();
        
        if (StringUtils.hasText(collectionHandle)) {
            log.info("Found {} products not in collection '{}' out of {} total", 
                    result.size(), collectionHandle, result.getTotalSize());
        } else {
            log.info("Found {} products out of {} total", result.size(), result.getTotalSize());
        }
        
        return result;
    }

    public ProductView updateProduct(UUID productId, ProductUpdateRequest request) {
        log.info("Updating product with ID: {}", productId);
        log.debug("Product update request details: {}", request);
        
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> {
                    log.warn("Product not found with ID: {}", productId);
                    return new ResourceNotFoundException("Product not found with ID: " + productId);
                });
        
        // Update basic product information
        product.setTitle(request.getTitle());
        product.setProductType(request.getProductType());
        product.setVendor(request.getVendor());
        product.setBodyHtml(request.getBodyHtml());
        product.setBodyPlain(request.getBodyPlain());
        product.setTags(request.getTags());
        product.setTemplateSuffix(request.getTemplateSuffix());
        product.setPublishedAt(request.getPublishedAt()); 
        product.setOnlyHideFromList(request.isOnlyHideFromList());
        product.setNotAllowPromotion(request.isNotAllowPromotion());
        product.setPublishedScope(request.getPublishedScope());
        
        // Skip handle update
        // if (request.getHandle() != null && !request.getHandle().isEmpty()) {
        //     String newHandle = SlugUtils.toSlugUnique(
        //             request.getHandle(), 
        //             handle -> !handle.equals(product.getHandle()) && productRepository.existsByHandle(handle)
        //     );
        //     product.setHandle(newHandle);
        // }
        
        // Process variants if provided
        if (request.getVariants() != null && !request.getVariants().isEmpty()) {
            // Create a map of existing variants by ID for quick lookup
            java.util.Map<Long, ProductVariant> existingVariantsMap = product.getVariants().stream()
                    .collect(Collectors.toMap(
                            ProductVariant::getId, 
                            variant -> variant,
                            (v1, v2) -> v1
                    ));
            
            // First pass: Update basic variant information
            for (ProductUpdateRequest.ProductVariantDTO variantDTO : request.getVariants()) {
                if (variantDTO.getId() != null && existingVariantsMap.containsKey(variantDTO.getId())) {
                    // Update existing variant
                    ProductVariant existingVariant = existingVariantsMap.get(variantDTO.getId());
                    updateVariant(existingVariant, variantDTO);
                } else {
                    // Create new variant
                    ProductVariant newVariant = createVariantFromDTO(variantDTO);
                    newVariant.setProduct(product);
                    product.getVariants().add(newVariant);
                }
            }
            
            // Second pass: Reorder variants based on position
            // Create a map of requested positions
            Map<Long, Integer> requestedPositions = new HashMap<>();
            for (ProductUpdateRequest.ProductVariantDTO variantDTO : request.getVariants()) {
                if (variantDTO.getId() != null && variantDTO.getPosition() != null) {
                    requestedPositions.put(variantDTO.getId(), variantDTO.getPosition());
                }
            }
            
            // Sort variants by requested position or keep current position if not specified
            if (!requestedPositions.isEmpty()) {
                java.util.List<ProductVariant> sortedVariants = PositionUtils.reorderByPosition(
                    new java.util.ArrayList<>(product.getVariants()),
                    requestedPositions,
                    ProductVariant::getId,
                    ProductVariant::getPosition,
                    ProductVariant::setPosition
                );
                
                // Replace variants list with sorted list
                product.getVariants().clear();
                product.getVariants().addAll(sortedVariants);
            }
        }
        
        // Update product options if provided
        if (request.getOptions() != null && !request.getOptions().isEmpty()) {
            // Clear existing options and create new ones
            product.getOptions().clear();
            
            // Sort options by position
            java.util.List<ProductUpdateRequest.ProductOptionDTO> sortedOptions = PositionUtils.reorderByPosition(
                new java.util.ArrayList<>(request.getOptions()),
                ProductUpdateRequest.ProductOptionDTO::getPosition,
                (option, position) -> option.setPosition(position)
            );
            
            // Add options with sequential positions
            for (ProductUpdateRequest.ProductOptionDTO optionDTO : sortedOptions) {
                ProductOption option = ProductOption.builder()
                        .name(optionDTO.getName())
                        .position(optionDTO.getPosition())
                        .product(product)
                        .build();
                product.getOptions().add(option);
            }
        }
        
        log.debug("Saving updated product with {} variants and {} options", 
                product.getVariants().size(), product.getOptions().size());
        
        Product savedProduct = productRepository.save(product);
        productRepository.flush();
        
        log.info("Product updated successfully with ID: {}", savedProduct.getId());
        
        ProductView productView = evm.find(em, ProductView.class, savedProduct.getId());
        
        if (productView != null) {
            log.debug("Updated product view loaded successfully");
        } else {
            log.warn("Failed to load product view for updated product ID: {}", savedProduct.getId());
        }
        
        return productView;
    }
    
    private void updateVariant(ProductVariant existingVariant, ProductUpdateRequest.ProductVariantDTO variantDTO) {
        existingVariant.setPrice(variantDTO.getPrice());
        existingVariant.setCompareAtPrice(variantDTO.getCompareAtPrice());
        existingVariant.setSku(variantDTO.getSku());
        existingVariant.setBarcode(variantDTO.getBarcode());
        existingVariant.setGrams(variantDTO.getGrams());
        existingVariant.setRequiresShipping(variantDTO.isRequiresShipping());
        existingVariant.setTaxable(variantDTO.isTaxable());
        existingVariant.setOption1(variantDTO.getOption1());
        existingVariant.setOption2(variantDTO.getOption2());
        existingVariant.setOption3(variantDTO.getOption3());
        existingVariant.setTitle(variantDTO.getTitle());
        // Skip inventory-related fields
        // existingVariant.setInventoryQuantity(variantDTO.getInventoryQuantity());
        // existingVariant.setInventoryPolicy(variantDTO.getInventoryPolicy());
        // existingVariant.setInventoryManagement(variantDTO.getInventoryManagement());
        // Skip inventory advance
    }
    
    private ProductVariant createVariantFromDTO(ProductUpdateRequest.ProductVariantDTO variantDTO) {
        // No inventory advance for new variants
        
        return ProductVariant.builder()
                .title(variantDTO.getTitle())
                .price(variantDTO.getPrice())
                .compareAtPrice(variantDTO.getCompareAtPrice())
                .sku(variantDTO.getSku())
                .barcode(variantDTO.getBarcode())
                .grams(variantDTO.getGrams())
                .position(variantDTO.getPosition() != null ? variantDTO.getPosition() : 1)
                // Skip inventory-related fields
                // .inventoryQuantity(variantDTO.getInventoryQuantity())
                // .inventoryPolicy(variantDTO.getInventoryPolicy())
                // .inventoryManagement(variantDTO.getInventoryManagement())
                .requiresShipping(variantDTO.isRequiresShipping())
                .taxable(variantDTO.isTaxable())
                .option1(variantDTO.getOption1())
                .option2(variantDTO.getOption2())
                .option3(variantDTO.getOption3())
                // Skip inventory advance
                .build();
    }

    /**
     * Deletes a product if it has no inventory.
     * 
     * @param productId The ID of the product to delete
     * @return true if the product was deleted, false if it has inventory and cannot be deleted
     * @throws ResourceNotFoundException if the product doesn't exist
     */
    public boolean deleteProduct(UUID productId) {
        log.info("Attempting to delete product with ID: {}", productId);
        
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> {
                    log.warn("Product not found with ID: {}", productId);
                    return new ResourceNotFoundException("Product not found with ID: " + productId);
                });
        
        // Check if any variant has inventory
        boolean hasInventory = product.getVariants().stream()
                .anyMatch(variant -> {
                    Integer quantity = variant.getInventoryQuantity();
                    return quantity != null && quantity > 0;
                });
        
        if (hasInventory) {
            log.warn("Cannot delete product with ID: {} because it has inventory", productId);
            return false;
        }
        
        // Delete the product
        log.info("Deleting product with ID: {}", productId);
        productRepository.delete(product);
        return true;
    }
}