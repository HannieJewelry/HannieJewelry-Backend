package hanniejewelry.vn.order.application.usecase;

import lombok.RequiredArgsConstructor;
import hanniejewelry.vn.order.domain.event.OrderTagsDeletedEvent;
import hanniejewelry.vn.order.entity.Order;
import hanniejewelry.vn.order.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DeleteOrderTagsUseCase {
    private final OrderRepository orderRepository;

    @Transactional
    public void handle(OrderTagsDeletedEvent evt) {
        Order oldOrder = orderRepository.findById(evt.orderId()).orElseThrow();
        
        String currentTags = oldOrder.getTags();
        
        if (currentTags == null || currentTags.isEmpty()) {
            return;
        }
        
        Set<String> currentTagsSet = new HashSet<>(Arrays.asList(currentTags.split(",")));
        Set<String> tagsToRemove = new HashSet<>(Arrays.asList(evt.tags().split(",")));
        
        currentTagsSet.removeAll(tagsToRemove);
        
        String updatedTags = currentTagsSet.stream().collect(Collectors.joining(","));
        
        Order updatedOrder = oldOrder.toBuilder()
                .tags(updatedTags)
                .build();
                
        orderRepository.save(updatedOrder);
    }
} 