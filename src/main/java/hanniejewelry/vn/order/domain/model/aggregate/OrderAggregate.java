package hanniejewelry.vn.order.domain.model.aggregate;

import hanniejewelry.vn.order.OrderPaidEvent;
import hanniejewelry.vn.order.dto.OrderRequest;
import hanniejewelry.vn.order.application.command.*;
import hanniejewelry.vn.order.domain.event.*;
import hanniejewelry.vn.order.domain.model.enums.CancelledStatus;
import hanniejewelry.vn.order.domain.model.enums.ClosedStatus;
import hanniejewelry.vn.order.domain.model.enums.ConfirmedStatus;
import hanniejewelry.vn.order.domain.model.enums.FinancialStatus;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.spring.stereotype.Aggregate;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;

import java.math.BigDecimal;
import java.util.UUID;

@Aggregate
@Slf4j
public class OrderAggregate {

    @AggregateIdentifier
    private UUID orderId;
    private BigDecimal totalPrice;
    private String email;
    private FinancialStatus financialStatus;
    private OrderRequest orderRequest;
    private ConfirmedStatus confirmedStatus;
    private ClosedStatus closedStatus;
    private CancelledStatus cancelledStatus;
    private String tags;
    private String orderCode;

    public OrderAggregate() {}

    @CommandHandler
    public OrderAggregate(CreateOrderCommand cmd) {
        log.info("Handling CreateOrderCommand: orderId={}, orderRequest={}", cmd.orderId(), cmd.orderRequest());
        if (cmd.orderId() == null || cmd.orderRequest() == null) {
            throw new IllegalArgumentException("OrderId or OrderRequest cannot be null!");
        }
        AggregateLifecycle.apply(new OrderCreatedEvent(cmd.orderId(), cmd.orderRequest()));
    }
    @EventSourcingHandler
    public void on(OrderCreatedEvent evt) {
        this.orderId = evt.orderId();
        this.orderRequest = evt.orderRequest();
        log.info("OrderCreatedEvent handled: orderId={}, orderRequest={}", orderId, orderRequest);

        if (orderRequest == null || orderRequest.getOrder() == null) {
            throw new IllegalStateException("OrderRequest or inner Order is null!");
        }
    }
    
    @CommandHandler
    public void handle(UpdateOrderCommand cmd) {
        AggregateLifecycle.apply(new OrderUpdatedEvent(cmd.orderId(), cmd.orderRequest()));
    }

    @EventSourcingHandler
    public void on(OrderUpdatedEvent evt) {
        this.orderRequest = evt.orderRequest();
        
        if (evt.orderRequest().getOrder().getEmail() != null) {
            this.email = evt.orderRequest().getOrder().getEmail();
        }
    }
    
    @CommandHandler
    public void handle(MarkOrderPaidCommand cmd) {
        log.info("Handling MarkOrderPaidCommand for order ID: {}, order code: {}", cmd.getOrderId(), cmd.getOrderCode());
        this.orderCode = cmd.getOrderCode();
        OrderPaidEvent event = new OrderPaidEvent(cmd.getOrderId());
        log.info("Publishing OrderPaidEvent for order ID: {}", cmd.getOrderId());
        AggregateLifecycle.apply(event);
    }

    @EventSourcingHandler
    public void on(OrderPaidEvent evt) {
        this.financialStatus = FinancialStatus.PAID;
        log.info("Updated order {} financial status to PAID", this.orderCode);
    }
    
    @CommandHandler
    public void handle(DeleteOrderCommand cmd) {
        AggregateLifecycle.apply(new OrderDeletedEvent(cmd.orderId()));
    }

    @EventSourcingHandler
    public void on(OrderDeletedEvent evt) {
    }

    @CommandHandler
    public void handle(ConfirmOrderCommand cmd) {
        AggregateLifecycle.apply(new OrderConfirmedEvent(cmd.orderId()));
    }
    
    @EventSourcingHandler
    public void on(OrderConfirmedEvent evt) {
        this.confirmedStatus = ConfirmedStatus.CONFIRMED;
    }
    
    @CommandHandler
    public void handle(CloseOrderCommand cmd) {
        AggregateLifecycle.apply(new OrderClosedEvent(cmd.orderId()));
    }
    
    @EventSourcingHandler
    public void on(OrderClosedEvent evt) {
        this.closedStatus = ClosedStatus.CLOSED;
    }
    
    @CommandHandler
    public void handle(ReopenOrderCommand cmd) {
        AggregateLifecycle.apply(new OrderReopenedEvent(cmd.orderId()));
    }
    
    @EventSourcingHandler
    public void on(OrderReopenedEvent evt) {
        this.closedStatus = ClosedStatus.UNCLOSED;
    }
    
    @CommandHandler
    public void handle(CancelOrderCommand cmd) {
        AggregateLifecycle.apply(new OrderCancelledEvent(cmd.orderId(), cmd.reason(), cmd.note()));
    }
    
    @EventSourcingHandler
    public void on(OrderCancelledEvent evt) {
        this.cancelledStatus = CancelledStatus.CANCELLED;
    }
    
    @CommandHandler
    public void handle(AddOrderTagsCommand cmd) {
        AggregateLifecycle.apply(new OrderTagsAddedEvent(cmd.orderId(), cmd.tags()));
    }
    
    @EventSourcingHandler
    public void on(OrderTagsAddedEvent evt) {
        if (this.tags == null || this.tags.isEmpty()) {
            this.tags = evt.tags();
        } else {
            this.tags = this.tags + "," + evt.tags();
        }
    }
    
    @CommandHandler
    public void handle(DeleteOrderTagsCommand cmd) {
        AggregateLifecycle.apply(new OrderTagsDeletedEvent(cmd.orderId(), cmd.tags()));
    }
    
    @EventSourcingHandler
    public void on(OrderTagsDeletedEvent evt) {
    }
}
