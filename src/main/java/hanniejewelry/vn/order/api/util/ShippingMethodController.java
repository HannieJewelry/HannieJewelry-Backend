package hanniejewelry.vn.order.api.util;

import hanniejewelry.vn.order.dto.ShippingMethodView;
import hanniejewelry.vn.shopping_cart.service.ShippingMethodService;
import hanniejewelry.vn.shared.constants.ApiConstants;
import hanniejewelry.vn.shared.dto.RestResponse;
import hanniejewelry.vn.shared.utils.RestResponseUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ApiConstants.API_PREFIX + "/shipping-methods")
@RequiredArgsConstructor
public class ShippingMethodController {
    
    private final ShippingMethodService shippingMethodService;
    
    @GetMapping
    public ResponseEntity<RestResponse<List<ShippingMethodView>>> getAllShippingMethods() {
        List<ShippingMethodView> methods = shippingMethodService.getAllActiveShippingMethods();
        return RestResponseUtils.successResponse(methods);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<RestResponse<ShippingMethodView>> getShippingMethodById(@PathVariable Long id) {
        ShippingMethodView method = shippingMethodService.getShippingMethodById(id);
        return RestResponseUtils.successResponse(method);
    }
    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RestResponse<ShippingMethodView>> createShippingMethod(
            @Valid @RequestBody ShippingMethodView view) {
        ShippingMethodView created = shippingMethodService.createShippingMethod(view);
        return RestResponseUtils.createdResponse(created);
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RestResponse<ShippingMethodView>> updateShippingMethod(
            @PathVariable Long id,
            @Valid @RequestBody ShippingMethodView view) {
        ShippingMethodView updated = shippingMethodService.updateShippingMethod(id, view);
        return RestResponseUtils.successResponse(updated);
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RestResponse<Void>> deleteShippingMethod(@PathVariable Long id) {
        shippingMethodService.deleteShippingMethod(id);
        return RestResponseUtils.successResponse(null);
    }
} 