package hanniejewelry.vn.order.domain.service;

import hanniejewelry.vn.order.domain.command.CheckInventoryCommand;
import hanniejewelry.vn.order.domain.event.InventoryCheckedEvent;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventhandling.GenericEventMessage;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InventoryCheckerService {

    private final EventBus eventBus;

    @CommandHandler
    public void handle(CheckInventoryCommand command) {
        boolean valid = true;
        InventoryCheckedEvent event = new InventoryCheckedEvent(command.orderId(), valid);
        eventBus.publish(GenericEventMessage.asEventMessage(event));
    }
}
