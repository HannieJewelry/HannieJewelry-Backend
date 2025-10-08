package hanniejewelry.vn.product.controller;

import hanniejewelry.vn.product.dto.ProductClientResponse;
import hanniejewelry.vn.product.dto.ProductFilterRequest;
import hanniejewelry.vn.product.dto.CustomCollectionResponse;
import hanniejewelry.vn.product.dto.SimpleIdNameDTO;
import hanniejewelry.vn.product.service.CustomCollectionClientService;
import hanniejewelry.vn.product.service.ProductService;
import hanniejewelry.vn.shared.constants.ApiConstants;
import hanniejewelry.vn.shared.dto.RestResponse;
import hanniejewelry.vn.shared.utils.RestResponseUtils;
import hanniejewelry.vn.shared.dto.PagedResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiConstants.API_PREFIX + "/client/collections")
public class CustomCollectionClientController {

    private final CustomCollectionClientService collectionService;
    private final ProductService productService;

    @GetMapping
    public ResponseEntity<RestResponse<List<CustomCollectionResponse>>> getAllCollections() {
        return RestResponseUtils.successResponse(collectionService.getAllCollections());
    }

    @GetMapping("/{handle}")
    public ResponseEntity<RestResponse<ProductClientResponse>> getProductByHandle(@PathVariable String handle) {
        return RestResponseUtils.successResponse(collectionService.getProductByHandle(handle));
    }

    @GetMapping("/all/products")
    public ResponseEntity<RestResponse<PagedResponse<ProductClientResponse>>> getAllProductsInCollection(
            @ModelAttribute ProductFilterRequest filter) {
        return RestResponseUtils.successResponse(collectionService.getAllProducts(filter));
    }

    @GetMapping("/{handle}/products")
    public ResponseEntity<RestResponse<PagedResponse<ProductClientResponse>>> getProductsByCollectionHandle(
            @PathVariable String handle,
            @ModelAttribute ProductFilterRequest filter) {
        return RestResponseUtils.successResponse(collectionService.getProductsByCollectionHandle(handle, filter));
    }

    @GetMapping("/vendors")
    public ResponseEntity<RestResponse<io.vavr.collection.List<SimpleIdNameDTO>>> getAllVendors() {
        return RestResponseUtils.successResponse(productService.getAllVendors());
    }

    @GetMapping("/product-types")
    public ResponseEntity<RestResponse<io.vavr.collection.List<SimpleIdNameDTO>>> getAllProductTypes() {
        return RestResponseUtils.successResponse(productService.getAllProductTypes());
    }


}
