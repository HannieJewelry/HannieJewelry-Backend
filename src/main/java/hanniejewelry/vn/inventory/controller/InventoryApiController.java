package hanniejewelry.vn.inventory.controller;

import hanniejewelry.vn.inventory.api.InventoryFacade;
import hanniejewelry.vn.inventory.dto.LocationRequest;
import hanniejewelry.vn.inventory.dto.SupplierRequest;
import hanniejewelry.vn.inventory.view.InventorySummaryView;
import hanniejewelry.vn.inventory.view.LocationView;
import hanniejewelry.vn.inventory.view.ProductInventoryView;
import hanniejewelry.vn.inventory.view.SupplierView;
import hanniejewelry.vn.shared.constants.ApiConstants;
import hanniejewelry.vn.shared.dto.RestResponse;
import hanniejewelry.vn.shared.utils.RestResponseUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(ApiConstants.API_PREFIX + "/inventory-api")
@RequiredArgsConstructor
public class InventoryApiController {

    private final InventoryFacade inventoryFacade;

    @GetMapping("/locations/{id}")
    public ResponseEntity<RestResponse<LocationView>> getLocationById(@PathVariable UUID id) {
        return RestResponseUtils.successResponse(inventoryFacade.getLocationById(id));
    }

    @GetMapping("/locations/shipping")
    public ResponseEntity<RestResponse<List<LocationView>>> getShippingLocations() {
        return RestResponseUtils.successResponse(inventoryFacade.getShippingLocations());
    }

    @PostMapping("/locations")
    public ResponseEntity<RestResponse<LocationView>> createLocation(@Valid @RequestBody LocationRequest request) {
        return RestResponseUtils.createdResponse(inventoryFacade.createLocation(request));
    }

    @PutMapping("/locations/{id}")
    public ResponseEntity<RestResponse<LocationView>> updateLocation(
            @PathVariable UUID id, @Valid @RequestBody LocationRequest request) {
        return RestResponseUtils.successResponse(inventoryFacade.updateLocation(id, request));
    }

    @DeleteMapping("/locations/{id}")
    public ResponseEntity<RestResponse<Void>> deleteLocation(@PathVariable UUID id) {
        inventoryFacade.deleteLocation(id);
        return RestResponseUtils.successResponse(null);
    }

    @PutMapping("/locations/{id}/primary")
    public ResponseEntity<RestResponse<LocationView>> setPrimaryLocation(@PathVariable UUID id) {
        return RestResponseUtils.successResponse(inventoryFacade.setPrimaryLocation(id));
    }

    @GetMapping("/suppliers/{id}")
    public ResponseEntity<RestResponse<SupplierView>> getSupplierById(@PathVariable UUID id) {
        return RestResponseUtils.successResponse(inventoryFacade.getSupplierById(id));
    }

    @PostMapping("/suppliers")
    public ResponseEntity<RestResponse<SupplierView>> createSupplier(@Valid @RequestBody SupplierRequest request) {
        return RestResponseUtils.createdResponse(inventoryFacade.createSupplier(request));
    }

    @PutMapping("/suppliers/{id}")
    public ResponseEntity<RestResponse<SupplierView>> updateSupplier(
            @PathVariable UUID id, @Valid @RequestBody SupplierRequest request) {
        return RestResponseUtils.successResponse(inventoryFacade.updateSupplier(id, request));
    }

    @DeleteMapping("/suppliers/{id}")
    public ResponseEntity<RestResponse<Void>> deleteSupplier(@PathVariable UUID id) {
        inventoryFacade.deleteSupplier(id);
        return RestResponseUtils.successResponse(null);
    }

    @PutMapping("/suppliers/{id}/debt")
    public ResponseEntity<RestResponse<SupplierView>> updateSupplierDebt(
            @PathVariable UUID id, @RequestParam BigDecimal debt) {
        return RestResponseUtils.successResponse(inventoryFacade.updateSupplierDebt(id, debt));
    }

    @PutMapping("/suppliers/{id}/total-purchase")
    public ResponseEntity<RestResponse<SupplierView>> updateSupplierTotalPurchase(
            @PathVariable UUID id, @RequestParam BigDecimal totalPurchase) {
        return RestResponseUtils.successResponse(inventoryFacade.updateSupplierTotalPurchase(id, totalPurchase));
    }

    @GetMapping("/products/variant/{variantId}")
    public ResponseEntity<RestResponse<ProductInventoryView>> getProductInventoryByVariantId(@PathVariable Long variantId) {
        return RestResponseUtils.successResponse(inventoryFacade.getProductInventoryByVariantId(variantId));
    }

    @GetMapping("/products/{productId}")
    public ResponseEntity<RestResponse<List<ProductInventoryView>>> getProductInventoryByProductId(
            @PathVariable Long productId) {
        return RestResponseUtils.successResponse(inventoryFacade.getProductInventoryByProductId(productId));
    }

    @GetMapping("/summary")
    public ResponseEntity<RestResponse<InventorySummaryView>> getInventorySummary() {
        return RestResponseUtils.successResponse(inventoryFacade.getInventorySummary());
    }
} 