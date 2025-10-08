package hanniejewelry.vn.inventory.controller;

import hanniejewelry.vn.inventory.dto.SupplierRequest;
import hanniejewelry.vn.inventory.service.SupplierService;
import hanniejewelry.vn.inventory.view.SupplierView;
import hanniejewelry.vn.shared.constants.ApiConstants;
import hanniejewelry.vn.shared.dto.GenericFilterRequest;
import hanniejewelry.vn.shared.dto.RestResponse;
import hanniejewelry.vn.shared.utils.RestResponseUtils;
import hanniejewelry.vn.shared.dto.PagedResponse;
import hanniejewelry.vn.shared.utils.ResponseUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping(ApiConstants.API_PREFIX + "/suppliers")
@RequiredArgsConstructor
public class SupplierController {

    private final SupplierService supplierService;

    @GetMapping
    public ResponseEntity<RestResponse<PagedResponse<SupplierView>>> getSuppliers(
            @ModelAttribute GenericFilterRequest filter) {
        return RestResponseUtils.successResponse(
                ResponseUtils.pagedSuccess(supplierService.getAllSuppliersWithBlazeFilter(filter), filter)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestResponse<SupplierView>> getSupplierById(@PathVariable UUID id) {
        return RestResponseUtils.successResponse(supplierService.getSupplierViewById(id));
    }

    @PostMapping
    public ResponseEntity<RestResponse<SupplierView>> createSupplier(@Valid @RequestBody SupplierRequest request) {
        return RestResponseUtils.createdResponse(supplierService.createSupplierAndReturnView(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RestResponse<SupplierView>> updateSupplier(
            @PathVariable UUID id, @Valid @RequestBody SupplierRequest request) {
        return RestResponseUtils.successResponse(supplierService.updateSupplierAndReturnView(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RestResponse<Void>> deleteSupplier(@PathVariable UUID id) {
        supplierService.deleteSupplier(id);
        return RestResponseUtils.successResponse(null);
    }

    @PutMapping("/{id}/debt")
    public ResponseEntity<RestResponse<SupplierView>> updateSupplierDebt(
            @PathVariable UUID id, @RequestParam BigDecimal debt) {
        return RestResponseUtils.successResponse(supplierService.updateSupplierDebt(id, debt));
    }

    @PutMapping("/{id}/total-purchase")
    public ResponseEntity<RestResponse<SupplierView>> updateSupplierTotalPurchase(
            @PathVariable UUID id, @RequestParam BigDecimal totalPurchase) {
        return RestResponseUtils.successResponse(supplierService.updateSupplierTotalPurchase(id, totalPurchase));
    }
} 