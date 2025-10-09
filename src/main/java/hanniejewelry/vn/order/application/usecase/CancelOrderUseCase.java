package hanniejewelry.vn.order.application.usecase;

import lombok.RequiredArgsConstructor;
import hanniejewelry.vn.order.domain.event.OrderCancelledEvent;
import hanniejewelry.vn.order.entity.Order;
import hanniejewelry.vn.order.domain.model.enums.CancelledStatus;
import hanniejewelry.vn.order.domain.model.enums.OrderProcessingStatus;
import hanniejewelry.vn.order.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class CancelOrderUseCase {
    private final OrderRepository orderRepository;

    @Transactional
    public void handle(OrderCancelledEvent evt) {
        orderRepository.findById(evt.orderId()).ifPresentOrElse(oldOrder -> {
            Order updatedOrder = oldOrder.toBuilder()
                    .cancelledStatus(CancelledStatus.CANCELLED)
                    .cancelledAt(Instant.now())
                    .cancelReason(evt.reason())
                    .note(evt.note())
                    .orderProcessingStatus(OrderProcessingStatus.CANCEL)
                    .build();
            orderRepository.save(updatedOrder);
        }, () -> {
            System.out.println("Không tìm thấy order " + evt.orderId() + " để hủy!");
        });

    }
} 