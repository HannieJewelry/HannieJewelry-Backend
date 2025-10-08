package hanniejewelry.vn.product.controller;

import hanniejewelry.vn.product.dto.CollectionImageRequest;
import hanniejewelry.vn.product.dto.CustomCollectionRequest;
import hanniejewelry.vn.product.service.CustomCollectionService;
import hanniejewelry.vn.product.view.CustomCollectionView;
import hanniejewelry.vn.shared.constants.ApiConstants;
import hanniejewelry.vn.shared.dto.GenericFilterRequest;
import hanniejewelry.vn.shared.dto.RestResponse;
import hanniejewelry.vn.shared.utils.RestResponseUtils;
import hanniejewelry.vn.shared.dto.PagedResponse;
import hanniejewelry.vn.shared.utils.ResponseUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping(ApiConstants.API_PREFIX + "/custom_collections")
@RequiredArgsConstructor
public class CustomCollectionController {
    
    private final CustomCollectionService customCollectionService;
    
    @GetMapping
    public ResponseEntity<RestResponse<PagedResponse<CustomCollectionView>>> getCustomCollections(
            @ModelAttribute GenericFilterRequest filter) {
        return RestResponseUtils.successResponse(
                ResponseUtils.pagedSuccess(customCollectionService.getAllCustomCollectionsWithBlazeFilter(filter), filter)
        );
    }
    
    @GetMapping("/count")
    public ResponseEntity<RestResponse<Map<String, Long>>> countCustomCollections() {
        return RestResponseUtils.successResponse(Map.of("count", customCollectionService.countCustomCollections()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestResponse<Map<String, CustomCollectionView>>> getCustomCollectionById(@PathVariable UUID id) {
        return RestResponseUtils.successResponse(Map.of("custom_collection", customCollectionService.getCustomCollectionById(id)));
    }
    
    @GetMapping("/handle/{handle}")
    public ResponseEntity<RestResponse<Map<String, CustomCollectionView>>> getCustomCollectionByHandle(@PathVariable String handle) {
        return RestResponseUtils.successResponse(Map.of("custom_collection", customCollectionService.getCustomCollectionByHandle(handle)));
    }
    
    @PostMapping
    public ResponseEntity<RestResponse<Map<String, CustomCollectionView>>> createCustomCollection(
            @Valid @RequestBody CustomCollectionRequest request) {
        return RestResponseUtils.createdResponse(Map.of("custom_collection", customCollectionService.createCustomCollection(request)));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<RestResponse<Map<String, CustomCollectionView>>> updateCustomCollection(
            @PathVariable UUID id, @Valid @RequestBody CustomCollectionRequest request) {
        return RestResponseUtils.successResponse(Map.of("custom_collection", customCollectionService.updateCustomCollection(id, request)));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<RestResponse<Void>> deleteCustomCollection(@PathVariable UUID id) {
        customCollectionService.deleteCustomCollection(id);
        return RestResponseUtils.successResponse(null);
    }
    
    @PostMapping(value = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<RestResponse<Map<String, CustomCollectionView>>> uploadCollectionImage(
            @PathVariable UUID id, @ModelAttribute CollectionImageRequest request) {
        return RestResponseUtils.successResponse(Map.of("custom_collection", customCollectionService.uploadCollectionImage(id, request)));
    }
} 