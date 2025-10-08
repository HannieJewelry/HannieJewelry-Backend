package hanniejewelry.vn.payment.service;

import hanniejewelry.vn.order.domain.model.enums.FinancialStatus;
import hanniejewelry.vn.shared.exception.BaseMessageType;
import io.vavr.control.Option;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import hanniejewelry.vn.order.application.command.MarkOrderPaidByBankCommand;
import hanniejewelry.vn.order.repository.OrderRepository;
import hanniejewelry.vn.payment.dto.TransactionRequest;
import hanniejewelry.vn.payment.entity.Transaction;
import hanniejewelry.vn.payment.mapper.TransactionMapper;
import hanniejewelry.vn.payment.repository.TransactionRepository;
import hanniejewelry.vn.shared.exception.BizException;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderPaymentService {

    private final OrderRepository orderRepository;
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;
    private final CommandGateway commandGateway;

    @Transactional
    public void processWebhook(TransactionRequest data) {
        Transaction transaction = transactionMapper.toEntity(data);
        transactionRepository.save(transaction);

        extractOrderCodeVavr(data.getContent())
                .flatMap(orderCode -> Option.ofOptional(orderRepository.findByOrderCode(orderCode)))
                .filter(order -> order.getFinancialStatus() == FinancialStatus.PENDING)
                .filter(order -> order.getTotalPrice().compareTo(new BigDecimal(data.getTransferAmount())) == 0)
                .peek(order -> {
                    commandGateway.sendAndWait(MarkOrderPaidByBankCommand.builder()
                            .orderId(order.getId())
                            .amount(new BigDecimal(data.getTransferAmount()))
                            .gateway(data.getGateway())
                            .referenceCode(data.getReferenceCode())
                            .paymentDetails(buildPaymentDetails(transaction))
                            .build());
                })
                .onEmpty(() -> {
                    throw new BizException(BaseMessageType.NOT_FOUND, "Order not found or payment mismatch. Content: " + data.getContent());
                });
    }

    private String buildPaymentDetails(Transaction transaction) {
        StringBuilder details = new StringBuilder();
        details.append("Bank: ").append(transaction.getGateway()).append(", ");
        details.append("Account: ").append(transaction.getAccountNumber()).append(", ");
        details.append("Reference: ").append(transaction.getReferenceCode()).append(", ");
        details.append("Content: ").append(transaction.getContent()).append(", ");
        details.append("Date: ").append(transaction.getTransactionDate());
        return details.toString();
    }

    private Option<String> extractOrderCodeVavr(String content) {
        if (content == null) return Option.none();
        var matcher = Pattern.compile("(DH\\d+)").matcher(content);
        return matcher.find() ? Option.of(matcher.group(1)) : Option.none();
    }
}
