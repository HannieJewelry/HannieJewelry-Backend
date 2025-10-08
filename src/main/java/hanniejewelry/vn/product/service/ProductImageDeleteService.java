package hanniejewelry.vn.product.service;

import hanniejewelry.vn.product.entity.ProductImage;
import hanniejewelry.vn.product.repository.ProductImageRepository;
import hanniejewelry.vn.product.repository.ProductRepository;
import hanniejewelry.vn.product.repository.ProductVariantRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Transactional
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ProductImageDeleteService {

    ProductRepository productRepository;
    ProductImageRepository productImageRepository;
    ProductVariantRepository productVariantRepository;
    EntityManager entityManager;

    /**
     * Deletes a product image and updates any variants that were using it
     *
     * @param productId The ID of the product
     * @param imageId The ID of the image to delete
     */
    public void deleteProductImage(UUID productId, UUID imageId) {
        // Verify that the product exists
        if (!productRepository.existsById(productId)) {
            throw new RuntimeException("Product not found with ID: " + productId);
        }
        
        // Verify that the image exists
        ProductImage image = productImageRepository.findById(imageId)
                .orElseThrow(() -> new RuntimeException("Image not found with ID: " + imageId));
        
        // Verify that the image belongs to the product
        if (image.getProduct() == null || !productId.equals(image.getProduct().getId())) {
            throw new RuntimeException("Image does not belong to the specified product");
        }
        
        // First, update all variants that reference this image to set image_id to null
        // Using a direct SQL update query to avoid Hibernate issues
        entityManager.createNativeQuery("UPDATE product_variant SET image_id = NULL WHERE image_id = :imageId")
                .setParameter("imageId", imageId)
                .executeUpdate();
        
        // Then remove the image from the product's collection
        // Using a direct SQL delete query for the join table if there is one
        try {
            entityManager.createNativeQuery("DELETE FROM product_image_variant_ids WHERE product_image_id = :imageId")
                    .setParameter("imageId", imageId)
                    .executeUpdate();
        } catch (Exception e) {
            // Ignore if the table doesn't exist
        }
        
        // Finally delete the image
        entityManager.createNativeQuery("DELETE FROM product_image WHERE id = :imageId")
                .setParameter("imageId", imageId)
                .executeUpdate();
        
        // Flush and clear the entity manager to avoid stale references
        entityManager.flush();
        entityManager.clear();
    }
} 