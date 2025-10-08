package hanniejewelry.vn.notification.handler;

import io.ably.lib.types.AblyException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import hanniejewelry.vn.notification.ably.AblyService;
import hanniejewelry.vn.order.OrderPaidEvent;
import hanniejewelry.vn.order.entity.Order;
import hanniejewelry.vn.order.repository.OrderRepository;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderPaidNotificationHandler {

    private final AblyService ablyService;
    private final OrderRepository orderRepository;

    @EventHandler
    @Transactional
    public void on(OrderPaidEvent event) {
        log.info("Received OrderPaidEvent for order ID: {}", event.orderId());
        try {
            var orderOpt = orderRepository.findById(event.orderId());
            if (orderOpt.isEmpty()) {
                log.error("Order not found for ID: {}", event.orderId());
                return;
            }

            Order order = orderOpt.get();
            log.info("Processing payment notification for order: {}", order.getOrderCode());

            try {
                ablyService.publishOrderPaid(order.getOrderCode());
                log.info("Published order paid event for order {}", order.getOrderCode());
            } catch (AblyException e) {
                log.error("Failed to publish order paid event for order {}", order.getOrderCode(), e);
            }
        } catch (Exception ex) {
            log.error("Error processing order paid event for order ID {}", event.orderId(), ex);
        }
    }
}