package hanniejewelry.vn.product.controller;

import hanniejewelry.vn.product.service.ProductImageDeleteService;
import hanniejewelry.vn.shared.dto.RestResponse;
import hanniejewelry.vn.shared.utils.RestResponseUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProductImageDeleteController {

    private final ProductImageDeleteService productImageDeleteService;
    
    /**
     * Deletes a product image
     *
     * @param productId The ID of the product
     * @param imageId The ID of the image to delete
     * @return A success response with no data
     */
    @DeleteMapping("/products/{productId}/images/{imageId}")
    public ResponseEntity<RestResponse<Void>> deleteProductImage(
            @PathVariable UUID productId,
            @PathVariable UUID imageId) {
        
        productImageDeleteService.deleteProductImage(productId, imageId);
        
        return RestResponseUtils.successResponse(null);
    }
} 