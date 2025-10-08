package hanniejewelry.vn.product.controller;

import hanniejewelry.vn.product.dto.ProductCreateWrapper;
import hanniejewelry.vn.product.dto.SimpleIdNameDTO;
import hanniejewelry.vn.shared.dto.RestResponse;
import hanniejewelry.vn.shared.utils.RestResponseUtils;
import io.vavr.collection.List;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import hanniejewelry.vn.product.service.ProductService;
import hanniejewelry.vn.product.view.ProductView;
import hanniejewelry.vn.shared.dto.GenericFilterRequest;
import hanniejewelry.vn.shared.dto.PagedResponse;
import hanniejewelry.vn.shared.utils.ResponseUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import hanniejewelry.vn.shared.constants.ApiConstants;
import hanniejewelry.vn.product.dto.ProductUpdateRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@RestController
@RequestMapping(ApiConstants.API_PREFIX + "/products")
@RequiredArgsConstructor
@Slf4j
public class ProductController {
    private final ProductService productService;

    @PostMapping
//    @PreAuthorize("hasAuthority('com_api.product.write')")
    public ResponseEntity<RestResponse<ProductView>> createProduct(
            @Valid @RequestBody ProductCreateWrapper wrapper
    ) {
        return RestResponseUtils.createdResponse(productService.createProduct(wrapper.getProduct()));
    }

    @GetMapping("/{id}")
//    @PreAuthorize("hasAuthority('com_api.product.read')")
    public ResponseEntity<RestResponse<ProductView>> getProduct(@PathVariable UUID id) {
        return RestResponseUtils.successResponse(productService.getProduct(id));
    }

    @GetMapping
    public ResponseEntity<RestResponse<PagedResponse<ProductView>>> getProductsByBlazeFilter(
            @ModelAttribute GenericFilterRequest filter,
            @RequestParam(name = "not_in_collection", required = false) String collectionHandle) {
        
        return RestResponseUtils.successResponse(
                ResponseUtils.pagedSuccess(productService.getFilteredProducts(filter, collectionHandle), filter)
        );
    }

    @GetMapping("/vendors")
    public ResponseEntity<RestResponse<List<SimpleIdNameDTO>>> getAllVendors() {
        return RestResponseUtils.successResponse(productService.getAllVendors());
    }

    @GetMapping("/types")
    public ResponseEntity<RestResponse<List<SimpleIdNameDTO>>> getAllProductTypes() {
        return RestResponseUtils.successResponse(productService.getAllProductTypes());
    }
    
    @GetMapping("/tags")
    public ResponseEntity<RestResponse<List<SimpleIdNameDTO>>> getAllProductTags() {
        return RestResponseUtils.successResponse(productService.getAllProductTags());
    }

    @PutMapping("/{productId}")
    public ResponseEntity<RestResponse<ProductView>> updateProduct(
            @PathVariable UUID productId,
            @RequestBody String requestBody
    ) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            objectMapper.configure(DeserializationFeature.ACCEPT_FLOAT_AS_INT, true);
            
            ProductUpdateRequest request = objectMapper.readValue(requestBody, ProductUpdateRequest.class);
            ProductView updatedProduct = productService.updateProduct(productId, request);
            return RestResponseUtils.successResponse(updatedProduct);
        } catch (Exception e) {
            log.error("Error updating product: {}", e.getMessage(), e);
            throw new RuntimeException("Error updating product: " + e.getMessage(), e);
        }
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<RestResponse<String>> deleteProduct(@PathVariable UUID productId) {
        boolean deleted = productService.deleteProduct(productId);
        
        if (deleted) {
            return RestResponseUtils.successResponse("Product deleted successfully");
        } else {
            return RestResponseUtils.errorResponse("Cannot delete product because it has inventory. Only products with zero inventory can be deleted.");
        }
    }
}