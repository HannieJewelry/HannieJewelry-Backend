package hanniejewelry.vn.product.service;

import hanniejewelry.vn.order.dto.OrderRequest;
import hanniejewelry.vn.product.api.ProductFacade;
import hanniejewelry.vn.product.entity.Product;
import hanniejewelry.vn.product.entity.ProductVariant;
import hanniejewelry.vn.product.repository.ProductRepository;
import hanniejewelry.vn.product.repository.ProductVariantRepository;
import hanniejewelry.vn.shared.utils.BigDecimalUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductOrderService implements ProductFacade {
    private final ProductVariantRepository productVariantRepository;
    private final ProductRepository productRepository;

    @Override
    public Map<Long, ProductVariant> loadVariants(OrderRequest.OrderInfo orderInfo) {
        Map<Long, ProductVariant> variantsMap = new HashMap<>();
        
        if (orderInfo.getLineItems() == null || orderInfo.getLineItems().isEmpty()) {
            return variantsMap;
        }
        
        List<Long> variantIds = new ArrayList<>();
        for (OrderRequest.LineItem item : orderInfo.getLineItems()) {
            if (item.getVariantId() != null) {
                variantIds.add(item.getVariantId());
            }
        }
        
        if (!variantIds.isEmpty()) {
            List<ProductVariant> variants = productVariantRepository.findAllById(variantIds);
            for (ProductVariant variant : variants) {
                variantsMap.put(variant.getId(), variant);
            }
        }
        
        return variantsMap;
    }


    @Override
    public Map<UUID, Product> loadProducts(Map<Long, ProductVariant> variantsMap) {
        Map<UUID, Product> productsMap = new HashMap<>();
        
        for (ProductVariant variant : variantsMap.values()) {
            if (variant.getProduct() != null) {
                productsMap.put(variant.getProduct().getId(), variant.getProduct());
            }
        }
        
        List<UUID> missingProductIds = new ArrayList<>();
        for (ProductVariant variant : variantsMap.values()) {
            if (variant.getProduct() == null || variant.getProduct().getId() == null) {
                continue;
            }
            
            Product product = productsMap.get(variant.getProduct().getId());
            if (product != null && (product.getImages() == null || product.getImages().isEmpty())) {
                missingProductIds.add(product.getId());
            }
        }
        
        if (!missingProductIds.isEmpty()) {
            List<Product> products = productRepository.findAllById(missingProductIds);
            for (Product product : products) {
                productsMap.put(product.getId(), product);
            }
        }
        
        return productsMap;
    }

    @Override
    public BigDecimal calculateLineItemsTotal(OrderRequest.OrderInfo orderInfo, Map<Long, ProductVariant> variantsMap) {
        BigDecimal lineItemsTotal = BigDecimalUtils.zero();
        
        if (orderInfo.getLineItems() == null) {
            return lineItemsTotal;
        }
        
        for (OrderRequest.LineItem item : orderInfo.getLineItems()) {
            ProductVariant variant = variantsMap.get(item.getVariantId());
            if (variant == null) {
                log.warn("Product variant not found: {}. Skipping this line item.", item.getVariantId());
                continue;
            }
            
            BigDecimal itemPrice = BigDecimalUtils.of(variant.getPrice());
            lineItemsTotal = BigDecimalUtils.add(lineItemsTotal, 
                                                BigDecimalUtils.multiply(itemPrice, item.getQuantity()));
        }
        
        return lineItemsTotal;
    }


    @Override
    public boolean validateVariants(Map<Long, ProductVariant> variantsMap, UUID orderId) {
        if (variantsMap.isEmpty()) {
            log.warn("No valid product variants found for order {}. Order will not be created.", orderId);
            return false;
        }
        return true;
    }
    

    @Override
    public Map<Long, Boolean> validateVariantsWithDetails(OrderRequest.OrderInfo orderInfo, UUID orderId) {
        Map<Long, Boolean> validationResults = new HashMap<>();
        
        if (orderInfo.getLineItems() == null || orderInfo.getLineItems().isEmpty()) {
            log.warn("No line items found in order {}. Validation failed.", orderId);
            return validationResults;
        }
        
        List<Long> requestedVariantIds = new ArrayList<>();
        for (OrderRequest.LineItem item : orderInfo.getLineItems()) {
            if (item.getVariantId() != null) {
                requestedVariantIds.add(item.getVariantId());
                validationResults.put(item.getVariantId(), false);
            }
        }
        
        if (requestedVariantIds.isEmpty()) {
            log.warn("No variant IDs found in order {}. Validation failed.", orderId);
            return validationResults;
        }
        
        List<ProductVariant> foundVariants = productVariantRepository.findAllById(requestedVariantIds);
        
        for (ProductVariant variant : foundVariants) {
            validationResults.put(variant.getId(), true);
        }
        
        int validCount = 0;
        for (Map.Entry<Long, Boolean> entry : validationResults.entrySet()) {
            if (entry.getValue()) {
                validCount++;
            } else {
                log.warn("Invalid product variant ID {} in order {}", entry.getKey(), orderId);
            }
        }
        
        log.info("Variant validation for order {}: {} valid out of {} requested", 
                orderId, validCount, validationResults.size());
        
        return validationResults;
    }
} 