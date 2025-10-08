package hanniejewelry.vn.inventory.controller;

import hanniejewelry.vn.inventory.service.InventoryService;
import hanniejewelry.vn.inventory.view.InventorySummaryView;
import hanniejewelry.vn.inventory.view.ProductInventoryView;
import hanniejewelry.vn.shared.constants.ApiConstants;
import hanniejewelry.vn.shared.dto.GenericFilterRequest;
import hanniejewelry.vn.shared.dto.RestResponse;
import hanniejewelry.vn.shared.utils.RestResponseUtils;
import hanniejewelry.vn.shared.dto.PagedResponse;
import hanniejewelry.vn.shared.utils.ResponseUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ApiConstants.API_PREFIX + "/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping("/products")
    public ResponseEntity<RestResponse<PagedResponse<ProductInventoryView>>> getProductInventory(
            @ModelAttribute GenericFilterRequest filter) {
        return RestResponseUtils.successResponse(
                ResponseUtils.pagedSuccess(inventoryService.getAllProductInventoryWithBlazeFilter(filter), filter)
        );
    }

    @GetMapping("/products/variant/{variantId}")
    public ResponseEntity<RestResponse<ProductInventoryView>> getProductInventoryByVariantId(@PathVariable Long variantId) {
        return RestResponseUtils.successResponse(inventoryService.getProductInventoryByVariantId(variantId));
    }

    @GetMapping("/products/{productId}")
    public ResponseEntity<RestResponse<List<ProductInventoryView>>> getProductInventoryByProductId(
            @PathVariable Long productId) {
        return RestResponseUtils.successResponse(inventoryService.getProductInventoryByProductId(productId));
    }

    @GetMapping("/summary")
    public ResponseEntity<RestResponse<InventorySummaryView>> getInventorySummary() {
        return RestResponseUtils.successResponse(inventoryService.getInventorySummary());
    }
} 