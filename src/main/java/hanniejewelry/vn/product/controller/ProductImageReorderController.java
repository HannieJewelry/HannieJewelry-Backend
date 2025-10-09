package hanniejewelry.vn.product.controller;

import hanniejewelry.vn.product.dto.ProductImageReorderRequestV2;
import hanniejewelry.vn.product.service.ProductImageReorderService;
import hanniejewelry.vn.product.view.ProductImageView;
import hanniejewelry.vn.shared.dto.RestResponse;
import hanniejewelry.vn.shared.utils.RestResponseUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProductImageReorderController {

    private final ProductImageReorderService productImageReorderService;
    
    /**
     * Reorders product images based on the provided order of image IDs.
     *
     * @param productId The ID of the product whose images are being reordered
     * @param request The request containing the ordered list of image IDs
     * @return A response containing the reordered product images
     */
    @PutMapping("/products/{productId}/images/reorder")
    public ResponseEntity<RestResponse<List<ProductImageView>>> reorderProductImages(
            @PathVariable UUID productId,
            @Valid @RequestBody ProductImageReorderRequestV2 request) {
        
        var reorderedImages = productImageReorderService.reorderProductImages(productId, request);
        var reorderedViews = productImageReorderService.toViews(reorderedImages);
        
        return RestResponseUtils.successResponse(reorderedViews);
    }
} 