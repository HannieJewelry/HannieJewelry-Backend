package hanniejewelry.vn.order.domain.service;

import hanniejewelry.vn.order.domain.command.CheckProductCommand;
import hanniejewelry.vn.order.domain.event.ProductCheckedEvent;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventhandling.GenericEventMessage;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductCheckerService {

    private final EventBus eventBus;

    @CommandHandler
    public void handle(CheckProductCommand command) {
        boolean valid = true;
        ProductCheckedEvent event = new ProductCheckedEvent(command.orderId(), valid);
        eventBus.publish(GenericEventMessage.asEventMessage(event));
    }
}
