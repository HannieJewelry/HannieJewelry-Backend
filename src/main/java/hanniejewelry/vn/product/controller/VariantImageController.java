package hanniejewelry.vn.product.controller;

import hanniejewelry.vn.product.dto.VariantImageUploadRequest;
import hanniejewelry.vn.product.service.VariantImageUploadService;
import hanniejewelry.vn.product.view.ProductImageView;
import hanniejewelry.vn.shared.dto.RestResponse;
import hanniejewelry.vn.shared.utils.RestResponseUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class VariantImageController {

    private final VariantImageUploadService variantImageUploadService;
    
    /**
     * Uploads an image file and links it to a variant
     *
     * @param productId The ID of the product
     * @param variantId The ID of the variant to link the image to
     * @param file The image file to upload
     * @return The uploaded product image
     * @throws IOException If there is an error uploading the image
     */
    @PostMapping(value = "/products/{productId}/variants/{variantId}/images/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<RestResponse<ProductImageView>> uploadVariantImage(
            @PathVariable UUID productId,
            @PathVariable Long variantId,
            @RequestParam("file") MultipartFile file) throws IOException {
        
        ProductImageView imageView = variantImageUploadService.uploadAndLinkVariantImage(productId, variantId, file);
        
        return RestResponseUtils.createdResponse(imageView);
    }
    
    /**
     * Uploads an image from a URL and links it to a variant
     *
     * @param productId The ID of the product
     * @param variantId The ID of the variant to link the image to
     * @param request The request containing the image URL
     * @return The uploaded product image
     * @throws IOException If there is an error uploading the image
     */
    @PostMapping("/products/{productId}/variants/{variantId}/images/upload-by-url")
    public ResponseEntity<RestResponse<ProductImageView>> uploadVariantImageFromUrl(
            @PathVariable UUID productId,
            @PathVariable Long variantId,
            @Valid @RequestBody VariantImageUploadRequest request) throws IOException {
        
        ProductImageView imageView = variantImageUploadService.uploadFromUrlAndLinkVariantImage(productId, variantId, request);
        
        return RestResponseUtils.createdResponse(imageView);
    }
    
    /**
     * Links an existing image to a variant
     *
     * @param productId The ID of the product
     * @param variantId The ID of the variant to link the image to
     * @param imageId The ID of the image to link
     * @return The linked product image
     */
    @PutMapping("/products/{productId}/variants/{variantId}/images/{imageId}")
    public ResponseEntity<RestResponse<ProductImageView>> linkVariantToImage(
            @PathVariable UUID productId,
            @PathVariable Long variantId,
            @PathVariable UUID imageId) {
        
        ProductImageView imageView = variantImageUploadService.linkVariantToImage(productId, variantId, imageId);
        
        return RestResponseUtils.successResponse(imageView);
    }
    
    /**
     * Updates a variant's image by uploading a new image file
     *
     * @param productId The ID of the product
     * @param variantId The ID of the variant to update
     * @param file The new image file to upload
     * @return The updated product image
     * @throws IOException If there is an error uploading the image
     */
    @PutMapping(value = "/products/{productId}/variants/{variantId}/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<RestResponse<ProductImageView>> updateVariantImageWithFormData(
            @PathVariable UUID productId,
            @PathVariable Long variantId,
            @RequestParam("file") MultipartFile file) throws IOException {
        
        ProductImageView imageView = variantImageUploadService.updateVariantImageWithFormData(productId, variantId, file);
        
        return RestResponseUtils.successResponse(imageView);
    }
} 