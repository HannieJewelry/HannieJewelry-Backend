package hanniejewelry.vn.payment.controller;

import hanniejewelry.vn.payment.dto.PaymentMethodView;
import hanniejewelry.vn.payment.service.PaymentMethodService;
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
@RequestMapping(ApiConstants.API_PREFIX + "/payment-methods")
@RequiredArgsConstructor
public class PaymentMethodController {
    
    private final PaymentMethodService paymentMethodService;
    
    @GetMapping
    public ResponseEntity<RestResponse<List<PaymentMethodView>>> getAllPaymentMethods() {
        List<PaymentMethodView> methods = paymentMethodService.getAllActivePaymentMethods();
        return RestResponseUtils.successResponse(methods);
    }
    
    @GetMapping("/online")
    public ResponseEntity<RestResponse<List<PaymentMethodView>>> getOnlinePaymentMethods() {
        List<PaymentMethodView> methods = paymentMethodService.getOnlinePaymentMethods();
        return RestResponseUtils.successResponse(methods);
    }
    
    @GetMapping("/offline")
    public ResponseEntity<RestResponse<List<PaymentMethodView>>> getOfflinePaymentMethods() {
        List<PaymentMethodView> methods = paymentMethodService.getOfflinePaymentMethods();
        return RestResponseUtils.successResponse(methods);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<RestResponse<PaymentMethodView>> getPaymentMethodById(@PathVariable Long id) {
        PaymentMethodView method = paymentMethodService.getPaymentMethodById(id);
        return RestResponseUtils.successResponse(method);
    }
    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RestResponse<PaymentMethodView>> createPaymentMethod(
            @Valid @RequestBody PaymentMethodView view) {
        PaymentMethodView created = paymentMethodService.createPaymentMethod(view);
        return RestResponseUtils.createdResponse(created);
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RestResponse<PaymentMethodView>> updatePaymentMethod(
            @PathVariable Long id,
            @Valid @RequestBody PaymentMethodView view) {
        PaymentMethodView updated = paymentMethodService.updatePaymentMethod(id, view);
        return RestResponseUtils.successResponse(updated);
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RestResponse<Void>> deletePaymentMethod(@PathVariable Long id) {
        paymentMethodService.deletePaymentMethod(id);
        return RestResponseUtils.successResponse(null);
    }
} 