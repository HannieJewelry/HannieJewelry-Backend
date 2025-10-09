package hanniejewelry.vn.order.application.command;

import hanniejewelry.vn.order.dto.OrderRequest;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.util.UUID;

public record UpdateOrderCommand(
    @TargetAggregateIdentifier UUID orderId, 
    OrderRequest orderRequest
) {}
