package hanniejewelry.vn.order.application.usecase;

import lombok.RequiredArgsConstructor;
import hanniejewelry.vn.order.OrderPaidEvent;
import hanniejewelry.vn.order.entity.Order;
import hanniejewelry.vn.order.domain.model.enums.FinancialStatus;
import hanniejewelry.vn.order.repository.OrderRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PayOrderUseCase {
    private final OrderRepository orderRepository;

    public void handle(OrderPaidEvent evt) {
        Order oldOrder = orderRepository.findById(evt.orderId()).orElseThrow();
        Order updatedOrder = oldOrder.toBuilder()
                .financialStatus(FinancialStatus.PAID)
                .build();
        orderRepository.save(updatedOrder);
    }
}
