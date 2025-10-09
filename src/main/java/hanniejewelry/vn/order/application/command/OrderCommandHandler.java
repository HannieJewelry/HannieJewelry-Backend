package hanniejewelry.vn.order.application.command;

import hanniejewelry.vn.order.OrderPaidEvent;
import hanniejewelry.vn.order.entity.Order;
import hanniejewelry.vn.order.domain.model.entity.OrderTransaction;
import hanniejewelry.vn.order.domain.model.enums.FinancialStatus;
import hanniejewelry.vn.order.domain.model.enums.FulfillmentStatus;
import hanniejewelry.vn.order.domain.model.enums.OrderProcessingStatus;
import hanniejewelry.vn.order.domain.model.enums.TransactionKind;
import hanniejewelry.vn.order.repository.OrderRepository;
import hanniejewelry.vn.shared.exception.BaseMessageType;
import hanniejewelry.vn.shared.exception.BizException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventhandling.GenericEventMessage;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderCommandHandler {

    private final OrderRepository orderRepository;
    private final EventBus eventBus;

    @CommandHandler
    @Transactional
    public void handle(MarkOrderFulfilledCommand command) {
        Order order = orderRepository.findById(command.getOrderId())
                .orElseThrow(() -> new BizException(BaseMessageType.NOT_FOUND, "Order not found"));

        order.setFulfillmentStatus(FulfillmentStatus.FULFILLED);

        orderRepository.save(order);
        log.info("Order {} has been marked as fulfilled", command.getOrderId());
    }
    
    @CommandHandler
    @Transactional
    public void handle(MarkOrderPaidByBankCommand command) {
        Order order = orderRepository.findById(command.getOrderId())
                .orElseThrow(() -> new BizException(BaseMessageType.NOT_FOUND, "Order not found"));

        order.setFinancialStatus(FinancialStatus.PAID);

        OrderTransaction transaction = OrderTransaction.builder()
                .order(order)
                .amount(command.getAmount())
                .gateway(command.getGateway())
                .kind(TransactionKind.SALE.toJson())
                .status("success")
                .currency("VND")
                .userId(order.getUserId())
                .locationId(order.getLocationId())
                .paymentDetails(command.getPaymentDetails())
                .externalTransactionId(command.getReferenceCode())
                .sendEmail(false)
                .build();

        order.getTransactions().add(transaction);

        orderRepository.save(order);
        log.info("Order {} has been marked as paid with amount {} via {}", 
                command.getOrderId(), command.getAmount(), command.getGateway());
                
        eventBus.publish(GenericEventMessage.asEventMessage(new OrderPaidEvent(order.getId())));
    }
    
    @CommandHandler
    @Transactional
    public void handle(ConfirmOrderCommand command) {
        Order order = orderRepository.findById(command.orderId())
                .orElseThrow(() -> new BizException(BaseMessageType.NOT_FOUND, "Order not found"));

        order.setOrderProcessingStatus(OrderProcessingStatus.CONFIRMED);

        orderRepository.save(order);
        log.info("Order {} has been confirmed", command.orderId());
    }
} 