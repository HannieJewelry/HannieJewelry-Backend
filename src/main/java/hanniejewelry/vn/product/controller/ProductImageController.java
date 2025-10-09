package hanniejewelry.vn.product.controller;

import hanniejewelry.vn.shared.dto.RestResponse;
import hanniejewelry.vn.shared.utils.RestResponseUtils;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Set;

import hanniejewelry.vn.product.dto.UploadByUrlRequest;
import hanniejewelry.vn.product.dto.VariantImageDTO;
import hanniejewelry.vn.product.service.ProductImageService;
import hanniejewelry.vn.product.view.ProductImageView;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;
import hanniejewelry.vn.shared.constants.ApiConstants;

@RestController
@RequestMapping(ApiConstants.API_PREFIX + "/products/images")
@RequiredArgsConstructor
public class ProductImageController {

    private final ProductImageService productImageService;

    @PostMapping("/{productId}/upload")
    public ResponseEntity<?> uploadImage(
            @PathVariable UUID productId,
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "variantIds", required = false) Set<Long> variantIds
    ) {
        return ResponseEntity.ok(productImageService.uploadAndSave(productId, file, variantIds));
    }

    @PostMapping("/{productId}/upload-by-url")
    public ResponseEntity<RestResponse<ProductImageView>> uploadByUrl(
            @PathVariable UUID productId,
            @RequestBody UploadByUrlRequest request
    ) {
        return RestResponseUtils.successResponse(
                productImageService.uploadByUrl(productId, request.getImage().getSrc(), request.getImage().getVariant_ids())
        );
    }

    @GetMapping("/{productId}")
    public ResponseEntity<RestResponse<List<ProductImageView>>> listImages(
            @PathVariable UUID productId
    ) {
        return RestResponseUtils.successResponse(productImageService.listImages(productId));
    }

    @GetMapping("/{productId}/variant-images")
    public ResponseEntity<RestResponse<List<VariantImageDTO>>> findVariantsWithImage(
            @PathVariable UUID productId
    ) {
        return RestResponseUtils.successResponse(productImageService.findVariantsWithImage(productId));
    }

    @GetMapping("/{productId}/variant/{variantId}/image")
    public ResponseEntity<RestResponse<List<ProductImageView>>> findImageByVariantId(
            @PathVariable UUID productId,
            @PathVariable Long variantId
    ) {
        return RestResponseUtils.successResponse(productImageService.findImageByVariantId(productId, variantId));
    }
}

