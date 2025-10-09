package hanniejewelry.vn.payment.controller;

import hanniejewelry.vn.order.CreateOrderResult;
import hanniejewelry.vn.order.dto.OrderRequest;
import hanniejewelry.vn.order.dto.CheckoutResponseView;
import hanniejewelry.vn.payment.dto.PaymentMethodView;
import hanniejewelry.vn.payment.service.CheckoutService;
import hanniejewelry.vn.payment.service.PaymentMethodService;
import hanniejewelry.vn.shared.constants.ApiConstants;
import hanniejewelry.vn.shared.dto.RestResponse;
import hanniejewelry.vn.shared.utils.RestResponseUtils;
import hanniejewelry.vn.shopping_cart.dto.CheckoutSessionRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping(ApiConstants.API_PREFIX + "/client/checkout")
@RequiredArgsConstructor
@Slf4j
public class CheckoutController {

    private final CheckoutService checkoutService;
    private final PaymentMethodService paymentMethodService;

    /** Lấy thông tin checkout cho user hiện tại */
    @GetMapping
    public ResponseEntity<RestResponse<CheckoutResponseView>> getCheckout() {
        log.debug("Getting checkout for current user");
        CheckoutResponseView response = checkoutService.getCheckout();
        return RestResponseUtils.successResponse(response);
    }

    /** Cập nhật thông tin checkout cho user hiện tại */
    @PutMapping
    public ResponseEntity<RestResponse<CheckoutResponseView>> updateCheckout(
            @Valid @RequestBody CheckoutSessionRequest request) {
        log.debug("Updating checkout for current user");
        CheckoutResponseView response = checkoutService.updateCheckout(request);
        return RestResponseUtils.successResponse(response);
    }

    /** Hoàn tất đặt hàng cho user hiện tại */
    @PostMapping("/complete")
    public CompletableFuture<ResponseEntity<RestResponse<CreateOrderResult>>> completeCheckout() {
        log.info("Completing checkout for current user");

        if (!checkoutService.validateInventory()) {
            return CompletableFuture.completedFuture(
                    RestResponseUtils.errorResponse("Một số sản phẩm không còn đủ số lượng")
            );
        }

        return checkoutService.completeCheckout()
                .thenApply(result -> {
                    if (result.getCode() == 202) {
                        log.info("Order created successfully: {}", result.getData());
                        return ResponseEntity.accepted().body(result);
                    } else {
                        log.error("Failed to complete checkout: {}", result.getMessage());
                        return RestResponseUtils.errorResponse(result.getMessage());
                    }
                });
    }

    /** Đặt hàng trực tiếp (custom) */
    @PostMapping("/direct")
    public CompletableFuture<ResponseEntity<RestResponse<CreateOrderResult>>> directCheckout(
            @Valid @RequestBody OrderRequest request,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return CompletableFuture.completedFuture(RestResponseUtils.badRequestResponse(bindingResult));
        }

        log.info("Processing direct checkout for current user");

        if (!checkoutService.validateInventory()) {
            return CompletableFuture.completedFuture(
                    RestResponseUtils.errorResponse("Một số sản phẩm không còn đủ số lượng")
            );
        }

        return checkoutService.directCheckout(request)
                .thenApply(result -> {
                    if (result.getCode() == 202) {
                        log.info("Order created via direct checkout: {}", result.getData());
                        return ResponseEntity.accepted().body(result);
                    } else {
                        log.error("Failed to complete direct checkout: {}", result.getMessage());
                        return RestResponseUtils.errorResponse(result.getMessage());
                    }
                });
    }

    @GetMapping("/payment-methods")
    public ResponseEntity<RestResponse<List<PaymentMethodView>>> getPaymentMethods() {
        log.debug("Getting all payment methods");
        List<PaymentMethodView> methods = paymentMethodService.getAllActivePaymentMethods();
        return RestResponseUtils.successResponse(methods);
    }

    @GetMapping("/cod-methods")
    public ResponseEntity<RestResponse<List<PaymentMethodView>>> getCODPaymentMethods() {
        log.debug("Getting COD payment methods");
        List<PaymentMethodView> methods = paymentMethodService.getOfflinePaymentMethods();
        return RestResponseUtils.successResponse(methods);
    }
}
