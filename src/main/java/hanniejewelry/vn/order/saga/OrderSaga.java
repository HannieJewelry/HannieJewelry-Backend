package hanniejewelry.vn.order.saga;

import hanniejewelry.vn.order.domain.event.OrderCreatedEvent;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.spring.stereotype.Saga;
import org.axonframework.modelling.saga.StartSaga;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

@Saga
public class OrderSaga {
    private int validated = 0, total = 0;
    private boolean invalid = false;
    private UUID orderId;

    @Autowired private transient CommandGateway commandGateway;
    @Autowired private transient EventBus eventBus;

    @StartSaga
    @SagaEventHandler(associationProperty = "orderId")
    public void on(OrderCreatedEvent event) {
        this.orderId = event.orderId();
        System.out.println("[saga] Step 1: Received OrderCreatedEvent, orderId=" + orderId);

        System.out.println("[saga] Step 1: Validate order data, email=" + event.orderRequest().getOrder().getEmail());

        total = event.orderRequest().getOrder().getLineItems().size();
        System.out.println("[saga] Step 1: Total line items to validate: " + total);


    }

}
