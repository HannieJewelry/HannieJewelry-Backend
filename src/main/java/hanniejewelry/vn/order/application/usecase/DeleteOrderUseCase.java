package hanniejewelry.vn.order.application.usecase;

import lombok.RequiredArgsConstructor;
import hanniejewelry.vn.order.domain.event.OrderDeletedEvent;
import hanniejewelry.vn.order.repository.OrderRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteOrderUseCase {
    private final OrderRepository orderRepository;

    public void handle(OrderDeletedEvent evt) {
        orderRepository.deleteById(evt.orderId());
    }
}
