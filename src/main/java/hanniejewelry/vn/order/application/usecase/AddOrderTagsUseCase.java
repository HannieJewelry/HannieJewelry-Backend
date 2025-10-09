package hanniejewelry.vn.order.application.usecase;

import lombok.RequiredArgsConstructor;
import hanniejewelry.vn.order.domain.event.OrderTagsAddedEvent;
import hanniejewelry.vn.order.entity.Order;
import hanniejewelry.vn.order.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AddOrderTagsUseCase {
    private final OrderRepository orderRepository;

    @Transactional
    public void handle(OrderTagsAddedEvent evt) {
        Order oldOrder = orderRepository.findById(evt.orderId()).orElseThrow();
        
        String currentTags = oldOrder.getTags();
        String newTags;
        
        if (currentTags == null || currentTags.isEmpty()) {
            newTags = evt.tags();
        } else {
            newTags = currentTags + "," + evt.tags();
        }
        
        Order updatedOrder = oldOrder.toBuilder()
                .tags(newTags)
                .build();
                
        orderRepository.save(updatedOrder);
    }
} 