package hanniejewelry.vn.order.api;

import hanniejewelry.vn.payment.dto.PaymentMethodView;
import hanniejewelry.vn.payment.service.PaymentMethodService;
import hanniejewelry.vn.shared.constants.ApiConstants;
import hanniejewelry.vn.shared.dto.RestResponse;
import hanniejewelry.vn.shared.utils.RestResponseUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping(ApiConstants.API_PREFIX + "/order-payment-methods")
@RequiredArgsConstructor
@Slf4j
public class OrderPaymentMethodController {

    private final PaymentMethodService paymentMethodService;


    @GetMapping
    public ResponseEntity<RestResponse<List<PaymentMethodView>>> getAllPaymentMethods() {
        log.debug("Getting all payment methods");
        List<PaymentMethodView> methods = paymentMethodService.getAllActivePaymentMethods();
        return RestResponseUtils.successResponse(methods);
    }


    @GetMapping("/cod")
    public ResponseEntity<RestResponse<List<PaymentMethodView>>> getCODPaymentMethods() {
        log.debug("Getting COD payment methods");
        List<PaymentMethodView> methods = paymentMethodService.getOfflinePaymentMethods();
        return RestResponseUtils.successResponse(methods);
    }

    @GetMapping("/online")
    public ResponseEntity<RestResponse<List<PaymentMethodView>>> getOnlinePaymentMethods() {
        log.debug("Getting online payment methods");
        List<PaymentMethodView> methods = paymentMethodService.getOnlinePaymentMethods();
        return RestResponseUtils.successResponse(methods);
    }
}