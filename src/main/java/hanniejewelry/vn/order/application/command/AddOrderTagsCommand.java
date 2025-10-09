package hanniejewelry.vn.order.application.command;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.util.UUID;

public record AddOrderTagsCommand(
        @TargetAggregateIdentifier UUID orderId,
        String tags
) {} 