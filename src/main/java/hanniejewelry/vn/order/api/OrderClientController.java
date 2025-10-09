package hanniejewelry.vn.order.api;

import hanniejewelry.vn.order.api.dto.CancelOrderRequest;
import hanniejewelry.vn.order.domain.model.enums.OrderProcessingStatus;
import hanniejewelry.vn.order.entity.Order;
import hanniejewelry.vn.order.repository.OrderRepository;
import hanniejewelry.vn.shared.constants.ApiConstants;
import hanniejewelry.vn.shared.dto.RestResponse;
import hanniejewelry.vn.shared.utils.RestResponseUtils;
import hanniejewelry.vn.shared.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(ApiConstants.API_PREFIX + "/client/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderClientController {

    private final OrderRepository orderRepository;

    /** User login: lấy tất cả đơn hàng của current user */
    @GetMapping("/me")
    public ResponseEntity<RestResponse<List<Order>>> getOrdersForCurrentUser() {
        UUID customerId = SecurityUtils.getCurrentCustomerId();
        log.info("Fetching all orders for customer: {}", customerId);

        List<Order> orders = orderRepository.findByCustomerIdOrderByCreatedAtDesc(customerId);
        return RestResponseUtils.successResponse(orders);
    }

    /** User login: lấy chi tiết 1 order của current user */
    @GetMapping("/me/{orderId}")
    public ResponseEntity<RestResponse<Order>> getOrderDetailForCurrentUser(@PathVariable UUID orderId) {
        UUID customerId = SecurityUtils.getCurrentCustomerId();
        log.info("Fetching order {} for customer {}", orderId, customerId);

        return RestResponseUtils.successResponse(orderRepository.findById(orderId).orElse(null));
    }

    /** Lấy order theo ID (không cần login) */
    @GetMapping("/{orderId}")
    public ResponseEntity<RestResponse<Order>> getOrderById(@PathVariable UUID orderId) {
        log.info("Fetching order for client with ID: {}", orderId);
        return RestResponseUtils.successResponse(orderRepository.findById(orderId).orElse(null));
    }

    /** Lấy orders theo cart token */
    @GetMapping
    public ResponseEntity<RestResponse<List<Order>>> getOrdersByCartToken(
            @RequestParam("cart_token") String cartToken) {
        log.info("Fetching orders for cart token: {}", cartToken);
        List<Order> orders = orderRepository.findByCartToken(cartToken);
        return RestResponseUtils.successResponse(orders);
    }

    @PutMapping("/me/{orderId}/cancel")
    public ResponseEntity<RestResponse<Order>> cancelOrderForCurrentUser(
            @PathVariable UUID orderId,
            @RequestBody CancelOrderRequest cancelRequest) {
        UUID customerId = SecurityUtils.getCurrentCustomerId();

        return orderRepository.findById(orderId)
                .map(order -> {
                    if (!order.getUserId().equals(customerId)) {
                        return ResponseEntity.badRequest()
                                .body(new RestResponse<Order>(null, "You can only cancel your own orders", 400, null));
                    }
                    if (order.getCancelledAt() != null) {
                        return ResponseEntity.badRequest()
                                .body(new RestResponse<Order>(null, "Order is already cancelled", 400, null));
                    }

                    order.setCancelledAt(Instant.now());
                    order.setCancelledReason(cancelRequest.getReason());
                    order.setOrderProcessingStatus(OrderProcessingStatus.CANCEL);

                    if (cancelRequest.getNote() != null && !cancelRequest.getNote().trim().isEmpty()) {
                        String existingNote = order.getNote() != null ? order.getNote() : "";
                        String cancellationNote = "Cancellation note: " + cancelRequest.getNote();
                        order.setNote(existingNote.isEmpty() ? cancellationNote : existingNote + "\n" + cancellationNote);
                    }

                    Order savedOrder = orderRepository.save(order);
                    return RestResponseUtils.successResponse(savedOrder); // OK vì T = Order
                })
                .orElseGet(() -> RestResponseUtils.successResponse(null));
    }

}
