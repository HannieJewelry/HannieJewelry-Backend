package hanniejewelry.vn.payment.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import hanniejewelry.vn.payment.dto.TransactionRequest;
import hanniejewelry.vn.payment.service.OrderPaymentService;
import hanniejewelry.vn.shared.utils.RestResponseUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/webhook")
@RequiredArgsConstructor
@Slf4j
public class WebhookController {

    private final OrderPaymentService orderPaymentService;

    @PostMapping("/payment")
    public ResponseEntity<?> paymentWebhook(@RequestBody TransactionRequest data) {
        log.info("Received payment webhook from gateway: {}, content: {}", data.getGateway(), data.getContent());
        try {
            orderPaymentService.processWebhook(data);
            log.info("Webhook processed successfully");
            return RestResponseUtils.successResponse("Webhook processed successfully");
        } catch (Exception e) {
            log.error("Error processing webhook: {}", e.getMessage(), e);
            return RestResponseUtils.successResponse("Webhook received with errors: " + e.getMessage());
        }
    }
}
