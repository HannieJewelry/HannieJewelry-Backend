package hanniejewelry.vn.order.application.usecase;

import lombok.RequiredArgsConstructor;
import hanniejewelry.vn.order.domain.event.OrderReopenedEvent;
import hanniejewelry.vn.order.entity.Order;
import hanniejewelry.vn.order.domain.model.enums.ClosedStatus;
import hanniejewelry.vn.order.domain.model.enums.OrderProcessingStatus;
import hanniejewelry.vn.order.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReopenOrderUseCase {
    private final OrderRepository orderRepository;

    @Transactional
    public void handle(OrderReopenedEvent evt) {
        Order oldOrder = orderRepository.findById(evt.orderId()).orElseThrow();
        Order updatedOrder = oldOrder.toBuilder()
                .closedStatus(ClosedStatus.UNCLOSED)
                .closedAt(null)
                .orderProcessingStatus(OrderProcessingStatus.PROCESSING)
                .build();
        orderRepository.save(updatedOrder);
    }
} 