package hanniejewelry.vn.product.service;

import com.blazebit.persistence.view.EntityViewManager;
import hanniejewelry.vn.product.dto.ProductImageReorderRequestV2;
import hanniejewelry.vn.product.entity.Product;
import hanniejewelry.vn.product.entity.ProductImage;
import hanniejewelry.vn.product.repository.ProductImageRepository;
import hanniejewelry.vn.product.repository.ProductRepository;
import hanniejewelry.vn.product.view.ProductImageView;
import hanniejewelry.vn.shared.utils.PositionUtils;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Transactional
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ProductImageReorderService {

    ProductImageRepository productImageRepository;
    ProductRepository productRepository;
    EntityManager em;
    EntityViewManager evm;

    /**
     * Reorders product images based on the provided order of image IDs.
     *
     * @param productId The ID of the product whose images are being reordered
     * @param request The request containing the ordered list of image IDs
     * @return The list of reordered product images
     */
    public List<ProductImage> reorderProductImages(UUID productId, ProductImageReorderRequestV2 request) {
        // Find the product or throw an exception if not found
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + productId));
        
        // Get all product images
        List<ProductImage> images = productImageRepository.findAllByProductId(productId);
        
        // Create a map of requested positions
        Map<UUID, Integer> requestedPositions = IntStream.range(0, request.getValues().size())
                .boxed()
                .collect(Collectors.toMap(
                        i -> request.getValues().get(i),
                        i -> i + 1
                ));
        
        // Reorder the images using PositionUtils
        List<ProductImage> reorderedImages = PositionUtils.reorderByPosition(
                images,
                requestedPositions,
                ProductImage::getId,
                ProductImage::getPosition,
                ProductImage::setPosition
        );
        
        // Save all reordered images
        return productImageRepository.saveAll(reorderedImages);
    }
    
    /**
     * Converts a ProductImage entity to a ProductImageView
     *
     * @param image The ProductImage entity to convert
     * @return The corresponding ProductImageView
     */
    public ProductImageView toView(ProductImage image) {
        if (image == null) {
            return null;
        }
        return evm.find(em, ProductImageView.class, image.getId());
    }
    
    /**
     * Converts a list of ProductImage entities to ProductImageViews
     *
     * @param images The list of ProductImage entities to convert
     * @return The list of corresponding ProductImageViews
     */
    public List<ProductImageView> toViews(List<ProductImage> images) {
        if (images == null) {
            return List.of();
        }
        return images.stream()
                .map(this::toView)
                .collect(Collectors.toList());
    }
} 