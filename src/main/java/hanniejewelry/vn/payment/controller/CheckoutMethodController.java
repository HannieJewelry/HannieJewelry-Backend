package hanniejewelry.vn.payment.controller;

import hanniejewelry.vn.payment.dto.PaymentMethodView;
import hanniejewelry.vn.order.dto.ShippingMethodView;
import hanniejewelry.vn.payment.service.PaymentMethodService;
import hanniejewelry.vn.shopping_cart.service.ShippingMethodService;
import hanniejewelry.vn.shared.constants.ApiConstants;
import hanniejewelry.vn.shared.dto.RestResponse;
import hanniejewelry.vn.shared.utils.RestResponseUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping(ApiConstants.API_PREFIX + "/client/checkout-methods")
@RequiredArgsConstructor
public class CheckoutMethodController {
    
    private final ShippingMethodService shippingMethodService;
    private final PaymentMethodService paymentMethodService;
    

    @GetMapping("/shipping")
    public ResponseEntity<RestResponse<List<ShippingMethodView>>> getShippingMethods() {
        List<ShippingMethodView> methods = shippingMethodService.getAllActiveShippingMethods();
        return RestResponseUtils.successResponse(methods);
    }
    
    @GetMapping("/payment")
    public ResponseEntity<RestResponse<List<PaymentMethodView>>> getPaymentMethods() {
        List<PaymentMethodView> methods = paymentMethodService.getAllActivePaymentMethods();
        return RestResponseUtils.successResponse(methods);
    }
    
    @GetMapping("/shipping/{id}")
    public ResponseEntity<RestResponse<ShippingMethodView>> getShippingMethodById(@PathVariable Long id) {
        ShippingMethodView method = shippingMethodService.getShippingMethodById(id);
        return RestResponseUtils.successResponse(method);
    }
    
    @GetMapping("/payment/{id}")
    public ResponseEntity<RestResponse<PaymentMethodView>> getPaymentMethodById(@PathVariable Long id) {
        PaymentMethodView method = paymentMethodService.getPaymentMethodById(id);
        return RestResponseUtils.successResponse(method);
    }
} 