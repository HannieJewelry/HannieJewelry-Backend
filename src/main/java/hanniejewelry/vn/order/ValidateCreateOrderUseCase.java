package hanniejewelry.vn.order;



import hanniejewelry.vn.order.application.command.CreateOrderCommand;
import hanniejewelry.vn.order.dto.OrderRequest;
import hanniejewelry.vn.product.repository.ProductVariantRepository;
import hanniejewelry.vn.shared.dto.UseCaseResult;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class ValidateCreateOrderUseCase {
    private final ProductVariantRepository productVariantRepository;
    private final CommandGateway commandGateway;

    public CompletableFuture<UseCaseResult<CreateOrderResult>> createOrder(OrderRequest request) {
        List<String> errors = validate(request);
        if (!validate(request).isEmpty()) {
            return CompletableFuture.completedFuture(UseCaseResult.error(String.join("; ", errors)));
        }
        UUID orderId = UUID.randomUUID();
        return commandGateway.send(new CreateOrderCommand(orderId, request))
                .thenApply(res -> UseCaseResult.ok(new CreateOrderResult(orderId, "processing")));
    }


    public List<String> validate(OrderRequest request) {
        List<String> errors = new ArrayList<>();
        for (OrderRequest.LineItem item : request.getOrder().getLineItems()) {
            if (!productVariantRepository.existsById(item.getVariantId())) {
                errors.add("Biến thể sản phẩm không tồn tại: variantId = " + item.getVariantId());
            }
        }
        return errors;
    }
}
