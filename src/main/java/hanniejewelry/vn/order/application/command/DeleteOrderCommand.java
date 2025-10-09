package hanniejewelry.vn.order.application.command;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.util.UUID;

public record DeleteOrderCommand(@TargetAggregateIdentifier UUID orderId) {}