package hanniejewelry.vn.order.application.usecase;


import lombok.RequiredArgsConstructor;
import hanniejewelry.vn.order.dto.OrderRequest;
import hanniejewelry.vn.order.domain.event.OrderUpdatedEvent;
import hanniejewelry.vn.order.entity.Order;
import hanniejewelry.vn.order.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdateOrderUseCase {
    private final OrderRepository orderRepository;

    @Transactional
    public void handle(OrderUpdatedEvent evt) {
        Order oldOrder = orderRepository.findById(evt.orderId()).orElseThrow();
        OrderRequest.OrderInfo orderInfo = evt.orderRequest().getOrder();
        
        Order updatedOrder = oldOrder.toBuilder()
                .name(orderInfo.getShippingAddress() != null ? 
                      orderInfo.getShippingAddress().getFirstName() + " " + orderInfo.getShippingAddress().getLastName() : 
                      oldOrder.getName())
                .email(orderInfo.getEmail() != null ? orderInfo.getEmail() : oldOrder.getEmail())
                .tags(orderInfo.getTags() != null ? orderInfo.getTags() : oldOrder.getTags())
                .gateway(orderInfo.getGateway() != null ? orderInfo.getGateway() : oldOrder.getGateway())
                .note(orderInfo.getNote() != null ? orderInfo.getNote() : oldOrder.getNote())
                .build();
        
        orderRepository.save(updatedOrder);
    }
}
