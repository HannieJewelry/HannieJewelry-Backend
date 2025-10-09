package hanniejewelry.vn.order.application.usecase;

import hanniejewelry.vn.order.application.orchestrator.CreateOrderOrchestrator;
import hanniejewelry.vn.order.domain.event.OrderCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Slf4j
public class CreateOrderUseCase {
    private final CreateOrderOrchestrator createOrderOrchestrator;


    @Transactional
    public void handle(OrderCreatedEvent evt, String userEmail) {
        log.info("Handling create order event. EventId: {}, UserEmail: {}", evt.orderId(), userEmail);
        createOrderOrchestrator.handleOrderCreation(evt, userEmail);
    }
}
