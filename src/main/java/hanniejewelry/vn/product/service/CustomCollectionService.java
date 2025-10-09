package hanniejewelry.vn.product.service;

import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.PagedList;
import com.blazebit.persistence.view.EntityViewManager;
import com.blazebit.persistence.view.EntityViewSetting;
import hanniejewelry.vn.product.api.CustomCollectionFacade;
import hanniejewelry.vn.product.dto.CollectionImageRequest;
import hanniejewelry.vn.product.dto.CustomCollectionRequest;
import hanniejewelry.vn.product.entity.Collect;
import hanniejewelry.vn.product.entity.CollectionImage;
import hanniejewelry.vn.product.entity.CustomCollection;
import hanniejewelry.vn.product.entity.Product;
import hanniejewelry.vn.product.repository.CollectRepository;
import hanniejewelry.vn.product.repository.CustomCollectionRepository;
import hanniejewelry.vn.product.repository.ProductRepository;
import hanniejewelry.vn.product.view.CustomCollectionView;
import hanniejewelry.vn.shared.exception.BaseMessageType;
import hanniejewelry.vn.shared.exception.BizException;
import hanniejewelry.vn.shared.filter.GenericBlazeFilterApplier;
import hanniejewelry.vn.shared.dto.GenericFilterRequest;
import hanniejewelry.vn.shared.service.CloudinaryService;
import hanniejewelry.vn.shared.utils.SlugUtils;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@Transactional
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class CustomCollectionService implements CustomCollectionFacade {
    
    EntityViewManager evm;
    CustomCollectionRepository customCollectionRepository;
    CollectRepository collectRepository;
    ProductRepository productRepository;
    CriteriaBuilderFactory cbf;
    EntityManager em;
    CloudinaryService cloudinaryService;
    private final GenericBlazeFilterApplier<CustomCollectionView> filterApplier = new GenericBlazeFilterApplier<>();

    public PagedList<CustomCollectionView> getAllCustomCollectionsWithBlazeFilter(GenericFilterRequest filter) {
        var setting = EntityViewSetting.create(CustomCollectionView.class, filter.getPage(), filter.getSize());
        filterApplier.applyFilters(setting, filter);

        var cb = cbf.create(em, CustomCollection.class);
        filterApplier.applySort(cb, filter);

        return evm.applySetting(setting, cb).getResultList();
    }

    @Override
    public CustomCollectionView getCustomCollectionById(UUID id) {
        customCollectionRepository.findById(id)
                .orElseThrow(() -> new BizException(BaseMessageType.NOT_FOUND, "Custom collection not found with id: {0}", id));

        return evm.find(em, CustomCollectionView.class, id);
    }

    @Override
    public CustomCollectionView getCustomCollectionByHandle(String handle) {
        CustomCollection collection = customCollectionRepository.findByHandle(handle)
                .orElseThrow(() -> new BizException(BaseMessageType.NOT_FOUND, "Custom collection not found with handle: {0}", handle));
        
        return evm.find(em, CustomCollectionView.class, collection.getId());
    }
    
    @Override
    public List<CustomCollectionView> getAllCustomCollections() {
        List<CustomCollection> collections = customCollectionRepository.findAll();
        return collections.stream()
                .map(collection -> evm.find(em, CustomCollectionView.class, collection.getId()))
                .collect(Collectors.toList());
    }
    
    @Override
    public long countCustomCollections() {
        return customCollectionRepository.count();
    }
    
    public CustomCollectionView createCustomCollection(CustomCollectionRequest request) {
        CustomCollection collection = CustomCollection.builder()
                .title(request.getTitle())
                .bodyHtml(request.getBodyHtml())
                .published(request.getPublished())
                .publishedAt(request.getPublished() ? Instant.now() : null)
                .publishedScope("web")
                .sortOrder(request.getSortOrder() != null ? request.getSortOrder() : "alpha_asc")
                .templateSuffix(request.getTemplateSuffix())
                .handle(generateHandle(request.getTitle()))
                .build();
        
        CustomCollection savedCollection = customCollectionRepository.save(collection);
        
        if (request.getCollects() != null && !request.getCollects().isEmpty()) {
            AtomicInteger position = new AtomicInteger(0);
            List<Collect> collects = new ArrayList<>();
            
            request.getCollects().forEach(collectRequest -> {
                Product product = productRepository.findById(collectRequest.getProductId())
                        .orElseThrow(() -> new BizException(BaseMessageType.NOT_FOUND, "Product not found with id: {0}", collectRequest.getProductId()));
                
                Collect collect = Collect.builder()
                        .collection(savedCollection)
                        .product(product)
                        .position(position.incrementAndGet())
                        .sortValue(collectRequest.getSortValue())
                        .build();
                
                collects.add(collect);
            });
            
            collectRepository.saveAll(collects);
        }
        
        customCollectionRepository.flush();
        
        return evm.find(em, CustomCollectionView.class, savedCollection.getId());
    }
    
    public CustomCollectionView updateCustomCollection(UUID id, CustomCollectionRequest request) {
        CustomCollection existingCollection = customCollectionRepository.findById(id)
                .orElseThrow(() -> new BizException(BaseMessageType.NOT_FOUND, "Custom collection not found with id: {0}", id));
        
        existingCollection.setTitle(request.getTitle() != null ? request.getTitle() : existingCollection.getTitle());
        existingCollection.setBodyHtml(request.getBodyHtml() != null ? request.getBodyHtml() : existingCollection.getBodyHtml());
        existingCollection.setTemplateSuffix(request.getTemplateSuffix() != null ? request.getTemplateSuffix() : existingCollection.getTemplateSuffix());
        existingCollection.setSortOrder(request.getSortOrder() != null ? request.getSortOrder() : existingCollection.getSortOrder());
        
        if (request.getPublished() != null && !request.getPublished().equals(existingCollection.getPublished())) {
            existingCollection.setPublished(request.getPublished());
            if (request.getPublished()) {
                existingCollection.setPublishedAt(Instant.now());
            }
        }
        
        CustomCollection savedCollection = customCollectionRepository.save(existingCollection);
        
        if (request.getCollects() != null && !request.getCollects().isEmpty()) {
            AtomicInteger position = new AtomicInteger(collectRepository.countByCollection(savedCollection));
            List<Collect> newCollects = new ArrayList<>();
            
            request.getCollects().forEach(collectRequest -> {
                Product product = productRepository.findById(collectRequest.getProductId())
                        .orElseThrow(() -> new BizException(BaseMessageType.NOT_FOUND, "Product not found with id: {0}", collectRequest.getProductId()));
                
                collectRepository.findByCollectionAndProduct(savedCollection, product)
                        .ifPresentOrElse(
                                existing -> {
                                    if (collectRequest.getSortValue() != null) {
                                        existing.setSortValue(collectRequest.getSortValue());
                                        collectRepository.save(existing);
                                    }
                                },
                                () -> {
                                    Collect collect = Collect.builder()
                                            .collection(savedCollection)
                                            .product(product)
                                            .position(position.incrementAndGet())
                                            .sortValue(collectRequest.getSortValue())
                                            .build();
                                    
                                    newCollects.add(collect);
                                }
                        );
            });
            
            if (!newCollects.isEmpty()) {
                collectRepository.saveAll(newCollects);
            }
        }
        
        customCollectionRepository.flush();
        
        return evm.find(em, CustomCollectionView.class, savedCollection.getId());
    }
    
    public void deleteCustomCollection(UUID id) {
        CustomCollection collection = customCollectionRepository.findById(id)
                .orElseThrow(() -> new BizException(BaseMessageType.NOT_FOUND, "Custom collection not found with id: {0}", id));
        
        customCollectionRepository.softDeleteById(collection.getId());
        customCollectionRepository.flush();
    }
    
    public CustomCollectionView uploadCollectionImage(UUID collectionId, CollectionImageRequest request) {
        CustomCollection collection = customCollectionRepository.findById(collectionId)
                .orElseThrow(() -> new BizException(BaseMessageType.NOT_FOUND, "Custom collection not found with id: {0}", collectionId));
        
        MultipartFile file = request.getImage();
        String filename = "collection_" + collectionId + "_" + System.currentTimeMillis();
        
        try {
            String imageUrl = cloudinaryService.uploadImage(file, filename);
            
            CollectionImage image = CollectionImage.builder()
                    .src(imageUrl)
                    .createdAt(Instant.now())
                    .build();
            
            collection.setImage(image);
            CustomCollection savedCollection = customCollectionRepository.save(collection);
            customCollectionRepository.flush();
            
            return evm.find(em, CustomCollectionView.class, savedCollection.getId());
        } catch (IOException e) {
            throw new BizException(BaseMessageType.INTERNAL_ERROR, "Failed to upload image: {0}", e.getMessage());
        }
    }
    
    private String generateHandle(String title) {
        return SlugUtils.toSlugUnique(title, customCollectionRepository::existsByHandle);
    }
} 