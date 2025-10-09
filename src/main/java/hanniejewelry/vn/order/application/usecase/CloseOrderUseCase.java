package hanniejewelry.vn.order.application.usecase;

import lombok.RequiredArgsConstructor;
import hanniejewelry.vn.order.domain.event.OrderClosedEvent;
import hanniejewelry.vn.order.entity.Order;
import hanniejewelry.vn.order.domain.model.enums.ClosedStatus;
import hanniejewelry.vn.order.domain.model.enums.OrderProcessingStatus;
import hanniejewelry.vn.order.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class CloseOrderUseCase {
    private final OrderRepository orderRepository;

    @Transactional
    public void handle(OrderClosedEvent evt) {
        Order oldOrder = orderRepository.findById(evt.orderId()).orElseThrow();
        Order updatedOrder = oldOrder.toBuilder()
                .closedStatus(ClosedStatus.CLOSED)
                .closedAt(Instant.now())
                .orderProcessingStatus(OrderProcessingStatus.COMPLETED)
                .build();
        orderRepository.save(updatedOrder);
    }
} 