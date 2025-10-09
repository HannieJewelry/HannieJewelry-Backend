package hanniejewelry.vn.order.application.event;

import hanniejewelry.vn.order.OrderPaidEvent;
import hanniejewelry.vn.order.application.usecase.*;
import hanniejewelry.vn.order.domain.event.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.messaging.annotation.MetaDataValue;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderEventHandler {

    private final CreateOrderUseCase createOrderUseCase;
    private final UpdateOrderUseCase updateOrderUseCase;
    private final PayOrderUseCase payOrderUseCase;
    private final DeleteOrderUseCase deleteOrderUseCase;
    private final ConfirmOrderUseCase confirmOrderUseCase;
    private final CloseOrderUseCase closeOrderUseCase;
    private final ReopenOrderUseCase reopenOrderUseCase;
    private final CancelOrderUseCase cancelOrderUseCase;
    private final AddOrderTagsUseCase addOrderTagsUseCase;
    private final DeleteOrderTagsUseCase deleteOrderTagsUseCase;

    @EventHandler
    public void on(OrderCreatedEvent evt, @MetaDataValue("userEmail") String userEmail) {
        try {
            createOrderUseCase.handle(evt, userEmail);
        } catch (Exception e) {
            log.error("Error handling OrderCreatedEvent", e);
            throw e;
        }
    }
    @EventHandler
    public void on(OrderUpdatedEvent evt) {
        try {
            updateOrderUseCase.handle(evt);
        } catch (Exception e) {
            log.error("Error handling OrderUpdatedEvent", e);
            throw e;
        }
    }

    @EventHandler
    public void on(OrderPaidEvent evt) {
        try {
            payOrderUseCase.handle(evt);
        } catch (Exception e) {
            log.error("Error handling OrderPaidEvent", e);
            throw e;
        }
    }

    @EventHandler
    public void on(OrderDeletedEvent evt) {
        try {
            deleteOrderUseCase.handle(evt);
        } catch (Exception e) {
            log.error("Error handling OrderDeletedEvent", e);
            throw e;
        }
    }

    @EventHandler
    public void on(OrderConfirmedEvent evt, @MetaDataValue("userId") String userId) {
        log.info("Confirm user id metaData: {}", userId);
        confirmOrderUseCase.handle(evt, userId);
    }


    @EventHandler
    public void on(OrderClosedEvent evt) {
        try {
            closeOrderUseCase.handle(evt);
        } catch (Exception e) {
            log.error("Error handling OrderClosedEvent", e);
            throw e;
        }
    }

    @EventHandler
    public void on(OrderReopenedEvent evt) {
        try {
            reopenOrderUseCase.handle(evt);
        } catch (Exception e) {
            log.error("Error handling OrderReopenedEvent", e);
            throw e;
        }
    }

    @EventHandler
    public void on(OrderCancelledEvent evt) {
        try {
            cancelOrderUseCase.handle(evt);
        } catch (Exception e) {
            log.error("Error handling OrderCancelledEvent", e);
            throw e;
        }
    }

    @EventHandler
    public void on(OrderTagsAddedEvent evt) {
        try {
            addOrderTagsUseCase.handle(evt);
        } catch (Exception e) {
            log.error("Error handling OrderTagsAddedEvent", e);
            throw e;
        }
    }

    @EventHandler
    public void on(OrderTagsDeletedEvent evt) {
        try {
            deleteOrderTagsUseCase.handle(evt);
        } catch (Exception e) {
            log.error("Error handling OrderTagsDeletedEvent", e);
            throw e;
        }
    }
}