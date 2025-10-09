package hanniejewelry.vn.order.application.usecase;

import lombok.RequiredArgsConstructor;
import hanniejewelry.vn.order.domain.event.OrderConfirmedEvent;
import hanniejewelry.vn.order.entity.Order;
import hanniejewelry.vn.order.domain.model.enums.ConfirmedStatus;
import hanniejewelry.vn.order.domain.model.enums.OrderProcessingStatus;
import hanniejewelry.vn.order.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class ConfirmOrderUseCase {
    private final OrderRepository orderRepository;
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ConfirmOrderUseCase.class);

    @Transactional
    public void handle(OrderConfirmedEvent evt, String confirmUserId) {
        log.info("Saving confirmUser: {}", confirmUserId);
        Order oldOrder = orderRepository.findById(evt.orderId()).orElseThrow();
        Order updatedOrder = oldOrder.toBuilder()
                .confirmedStatus(ConfirmedStatus.CONFIRMED)
                .confirmedAt(Instant.now())
                .orderProcessingStatus(OrderProcessingStatus.PROCESSING)
                .confirmUser(confirmUserId)
                .build();
        orderRepository.save(updatedOrder);
    }



} 