package hanniejewelry.vn.product.service;

import com.blazebit.persistence.view.EntityViewManager;
import hanniejewelry.vn.product.dto.VariantImageUploadRequest;
import hanniejewelry.vn.product.entity.Product;
import hanniejewelry.vn.product.entity.ProductImage;
import hanniejewelry.vn.product.entity.ProductVariant;
import hanniejewelry.vn.product.repository.ProductImageRepository;
import hanniejewelry.vn.product.repository.ProductRepository;
import hanniejewelry.vn.product.repository.ProductVariantRepository;
import hanniejewelry.vn.product.view.ProductImageView;
import hanniejewelry.vn.shared.service.CloudinaryService;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashSet;
import java.util.UUID;

@Service
@Transactional
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class VariantImageUploadService {

    ProductRepository productRepository;
    ProductVariantRepository productVariantRepository;
    ProductImageRepository productImageRepository;
    CloudinaryService cloudinaryService;
    EntityManager em;
    EntityViewManager evm;

    /**
     * Uploads an image file and links it to a variant
     *
     * @param productId The ID of the product
     * @param variantId The ID of the variant to link the image to
     * @param file The image file to upload
     * @return The uploaded product image view
     * @throws IOException If there is an error uploading the image
     */
    public ProductImageView uploadAndLinkVariantImage(UUID productId, Long variantId, MultipartFile file) throws IOException {
        // Verify that the product exists
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + productId));
        
        // Verify that the variant exists and belongs to the product
        ProductVariant variant = productVariantRepository.findById(variantId)
                .orElseThrow(() -> new RuntimeException("Variant not found with ID: " + variantId));
        
        if (!variant.getProduct().getId().equals(productId)) {
            throw new RuntimeException("Variant does not belong to the specified product");
        }
        
        // Generate a unique filename
        String filename = "product-" + productId + "-variant-" + variantId + "-" + System.currentTimeMillis();
        
        // Upload the image to Cloudinary
        String imageUrl = cloudinaryService.uploadImage(file, filename);
        
        // Create a new product image
        ProductImage image = ProductImage.builder()
                .src(imageUrl)
                .filename(file.getOriginalFilename())
                .position(product.getImages().size() + 1)
                .variantIds(new HashSet<>())
                .product(product)
                .build();
        
        // Add the variant ID to the image's variant IDs
        image.getVariantIds().add(variantId);
        
        // Save the image
        ProductImage savedImage = productImageRepository.save(image);
        
        // Link the image to the variant
        variant.setImageId(savedImage.getId());
        productVariantRepository.save(variant);
        
        // Return the image view
        return evm.find(em, ProductImageView.class, savedImage.getId());
    }
    
    /**
     * Uploads an image from a URL and links it to a variant
     *
     * @param productId The ID of the product
     * @param variantId The ID of the variant to link the image to
     * @param request The request containing the image URL
     * @return The uploaded product image view
     * @throws IOException If there is an error uploading the image
     */
    public ProductImageView uploadFromUrlAndLinkVariantImage(UUID productId, Long variantId, VariantImageUploadRequest request) throws IOException {
        // Verify that the product exists
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + productId));
        
        // Verify that the variant exists and belongs to the product
        ProductVariant variant = productVariantRepository.findById(variantId)
                .orElseThrow(() -> new RuntimeException("Variant not found with ID: " + variantId));
        
        if (!variant.getProduct().getId().equals(productId)) {
            throw new RuntimeException("Variant does not belong to the specified product");
        }
        
        // Generate a unique filename
        String filename = "product-" + productId + "-variant-" + variantId + "-" + System.currentTimeMillis();
        
        // Upload the image to Cloudinary
        String imageUrl = cloudinaryService.uploadImageFromUrl(request.getImageUrl(), filename);
        
        // Create a new product image
        ProductImage image = ProductImage.builder()
                .src(imageUrl)
                .filename(request.getFilename() != null ? request.getFilename() : "image-from-url.jpg")
                .position(product.getImages().size() + 1)
                .variantIds(new HashSet<>())
                .product(product)
                .build();
        
        // Add the variant ID to the image's variant IDs
        image.getVariantIds().add(variantId);
        
        // Save the image
        ProductImage savedImage = productImageRepository.save(image);
        
        // Link the image to the variant
        variant.setImageId(savedImage.getId());
        productVariantRepository.save(variant);
        
        // Return the image view
        return evm.find(em, ProductImageView.class, savedImage.getId());
    }
    
    /**
     * Links an existing image to a variant
     *
     * @param productId The ID of the product
     * @param variantId The ID of the variant to link the image to
     * @param imageId The ID of the image to link
     * @return The linked product image view
     */
    public ProductImageView linkVariantToImage(UUID productId, Long variantId, UUID imageId) {
        // Verify that the product exists
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + productId));
        
        // Verify that the variant exists and belongs to the product
        ProductVariant variant = productVariantRepository.findById(variantId)
                .orElseThrow(() -> new RuntimeException("Variant not found with ID: " + variantId));
        
        if (!variant.getProduct().getId().equals(productId)) {
            throw new RuntimeException("Variant does not belong to the specified product");
        }
        
        // Verify that the image exists and belongs to the product
        ProductImage image = productImageRepository.findById(imageId)
                .orElseThrow(() -> new RuntimeException("Image not found with ID: " + imageId));
        
        if (!image.getProduct().getId().equals(productId)) {
            throw new RuntimeException("Image does not belong to the specified product");
        }
        
        // Add the variant ID to the image's variant IDs if not already present
        if (!image.getVariantIds().contains(variantId)) {
            image.getVariantIds().add(variantId);
            productImageRepository.save(image);
        }
        
        // Link the image to the variant
        variant.setImageId(imageId);
        productVariantRepository.save(variant);
        
        // Return the image view
        return evm.find(em, ProductImageView.class, imageId);
    }
    
    /**
     * Updates a variant's image by uploading a new image file
     * If the variant already has an image, it will be replaced
     *
     * @param productId The ID of the product
     * @param variantId The ID of the variant to update
     * @param file The new image file to upload
     * @return The updated product image view
     * @throws IOException If there is an error uploading the image
     */
    public ProductImageView updateVariantImageWithFormData(UUID productId, Long variantId, MultipartFile file) throws IOException {
        // Verify that the product exists
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + productId));
        
        // Verify that the variant exists and belongs to the product
        ProductVariant variant = productVariantRepository.findById(variantId)
                .orElseThrow(() -> new RuntimeException("Variant not found with ID: " + variantId));
        
        if (!variant.getProduct().getId().equals(productId)) {
            throw new RuntimeException("Variant does not belong to the specified product");
        }
        
        // Check if the variant already has an image
        UUID oldImageId = variant.getImageId();
        ProductImage oldImage = null;
        
        if (oldImageId != null) {
            // Try to find the old image
            oldImage = productImageRepository.findById(oldImageId).orElse(null);
            
            // If the old image exists, remove the variant ID from its variant IDs
            if (oldImage != null && oldImage.getVariantIds().contains(variantId)) {
                oldImage.getVariantIds().remove(variantId);
                productImageRepository.save(oldImage);
            }
        }
        
        // Generate a unique filename
        String filename = "product-" + productId + "-variant-" + variantId + "-" + System.currentTimeMillis();
        
        // Upload the new image to Cloudinary
        String imageUrl = cloudinaryService.uploadImage(file, filename);
        
        // Create a new product image
        ProductImage newImage = ProductImage.builder()
                .src(imageUrl)
                .filename(file.getOriginalFilename())
                .position(product.getImages().size() + 1)
                .variantIds(new HashSet<>())
                .product(product)
                .build();
        
        // Add the variant ID to the new image's variant IDs
        newImage.getVariantIds().add(variantId);
        
        // Save the new image
        ProductImage savedImage = productImageRepository.save(newImage);
        
        // Link the new image to the variant
        variant.setImageId(savedImage.getId());
        productVariantRepository.save(variant);
        
        // Return the new image view
        return evm.find(em, ProductImageView.class, savedImage.getId());
    }
} 