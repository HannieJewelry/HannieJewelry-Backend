package hanniejewelry.vn.order.application.command;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.util.UUID;

public record CancelOrderCommand(
        @TargetAggregateIdentifier UUID orderId,
        String reason,
        String note
) {} 