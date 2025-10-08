package hanniejewelry.vn.order.api;

import com.blazebit.persistence.PagedList;
import hanniejewelry.vn.order.*;
import hanniejewelry.vn.order.api.dto.*;
import hanniejewelry.vn.order.application.command.*;
import hanniejewelry.vn.order.application.query.FindAllOrdersQuery;
import hanniejewelry.vn.order.application.query.FindOrderByIdQuery;
import hanniejewelry.vn.order.application.query.view.OrderView;
import hanniejewelry.vn.order.application.query.response.OrderPagedList;
import hanniejewelry.vn.order.dto.OrderRequest;
import hanniejewelry.vn.order.entity.Order;
import hanniejewelry.vn.order.repository.OrderRepository;
import hanniejewelry.vn.shared.dto.GenericFilterRequest;
import hanniejewelry.vn.shared.dto.RestResponse;
import hanniejewelry.vn.shared.utils.RestResponseUtils;
import hanniejewelry.vn.shared.dto.PagedResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import hanniejewelry.vn.shared.constants.ApiConstants;

@RestController
@RequestMapping(ApiConstants.API_PREFIX + "/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final CommandGateway commandGateway;
    private final QueryGateway queryGateway;
    private final OrderRepository orderRepository;
    private final ValidateCreateOrderUseCase validateCreateOrderUseCase;

    @Transactional(readOnly = true)
    public Order getOrderWithRelationships(UUID orderId) {
        log.debug("Fetching order with relationships for ID: {}", orderId);
        return orderRepository.findById(orderId).orElse(null);
    }

    @PostMapping
    public CompletableFuture<ResponseEntity<RestResponse<CreateOrderResult>>> createOrder(
            @RequestBody @Valid OrderRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return CompletableFuture.completedFuture(RestResponseUtils.badRequestResponse(bindingResult));

        return validateCreateOrderUseCase.createOrder(request)
                .thenApply(result ->
                        result.isOk()
                                ? ResponseEntity.accepted().body(
                                new RestResponse<>(result.getData(),
                                        "Order is being processed. Please query by order_id to get details.", 202))
                                : RestResponseUtils.errorResponse(result.getError())
                );
    }


    @PutMapping("/{id}")
    public CompletableFuture<ResponseEntity<RestResponse<OrderView>>> updateOrder(
            @PathVariable UUID id, @RequestBody @Valid OrderRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return CompletableFuture.completedFuture(RestResponseUtils.badRequestResponse(bindingResult));

        log.info("Updating order with ID: {}", id);
        log.debug("Order update details: {}", request);

        return commandGateway.send(new UpdateOrderCommand(id, request))
                .thenCompose(res -> queryGateway.query(new FindOrderByIdQuery(id), OrderView.class)
                        .thenApply(orderView -> {
                            if (orderView == null) {
                                log.warn("Failed to find order after update for ID: {}", id);
                                return RestResponseUtils.notFoundResponse();
                            }
                            log.info("Order updated successfully with ID: {}", id);
                            return RestResponseUtils.successResponse(orderView);
                        }));
    }

    @DeleteMapping("/{id}")
    public CompletableFuture<ResponseEntity<RestResponse<OrderView>>> deleteOrder(@PathVariable UUID id) {
        log.info("Deleting order with ID: {}", id);

        return queryGateway.query(new FindOrderByIdQuery(id), OrderView.class)
                .thenCompose(orderView -> {
                    if (orderView == null) {
                        log.warn("Cannot delete - order not found with ID: {}", id);
                        return CompletableFuture.completedFuture(RestResponseUtils.notFoundResponse());
                    }

                    return commandGateway.send(new DeleteOrderCommand(id))
                            .thenApply(res -> {
                                log.info("Order deleted successfully with ID: {}", id);
                                return RestResponseUtils.successResponse(orderView);
                            });
                });
    }

    @GetMapping("/{id}")
    public CompletableFuture<ResponseEntity<RestResponse<OrderView>>> getOrderById(@PathVariable UUID id) {
        log.info("Fetching order with ID: {}", id);

        return queryGateway.query(new FindOrderByIdQuery(id), OrderView.class)
                .thenApply(orderView -> {
                    if (orderView == null) {
                        log.warn("Order not found with ID: {}", id);
                        return RestResponseUtils.notFoundResponse();
                    }

                    Order fullOrder = getOrderWithRelationships(id);
                    if (fullOrder != null && !fullOrder.getLineItems().isEmpty()) {
                        log.debug("Order {} has {} line items", id, fullOrder.getLineItems().size());
                        if (orderView.getLineItems() == null || orderView.getLineItems().isEmpty()) {
                            log.warn("Data inconsistency detected: Order view missing line items for order ID: {}", id);
                        }
                    }

                    log.info("Order fetched successfully with ID: {}", id);
                    return RestResponseUtils.successResponse(orderView);
                });
    }

    @GetMapping("/bp/view")
    public CompletableFuture<ResponseEntity<RestResponse<PagedResponse<OrderView>>>> getOrdersByBlazeFilter(
            @ModelAttribute GenericFilterRequest filter) {
        log.info("Fetching orders with filter: page={}, size={}", filter.getPage(), filter.getSize());
        log.debug("Filter details: {}", filter);

        return queryGateway.query(
                        new FindAllOrdersQuery(filter),
                        ResponseTypes.instanceOf(OrderPagedList.class)
                ).thenApply(result -> {
                    OrderPagedList orderPagedList = (OrderPagedList) result;
                    PagedList<OrderView> paged = orderPagedList.getPagedList();
                    log.info("Found {} orders out of {} total", paged.size(), paged.getTotalSize());

                    var response = new PagedResponse<>(
                            paged, filter.getPage(), filter.getSize(),
                            paged.getTotalSize(), filter.getSortProperty(), filter.getDirection()
                    );
                    return RestResponseUtils.successResponse(response);
                })
                .exceptionally(throwable -> {
                    log.error("Error fetching orders with blaze filter", throwable);
                    return RestResponseUtils.errorServerResponse("Failed to retrieve orders. Please try again later.");
                });
    }


    @PostMapping("/{id}/confirm")
    public CompletableFuture<ResponseEntity<RestResponse<OrderView>>> confirmOrder(@PathVariable UUID id) {
        log.info("Confirming order with ID: {}", id);

        return queryGateway.query(new FindOrderByIdQuery(id), OrderView.class)
                .thenCompose(orderView -> {
                    if (orderView == null) {
                        log.warn("Cannot confirm - order not found with ID: {}", id);
                        return CompletableFuture.completedFuture(RestResponseUtils.notFoundResponse());
                    }

                    return commandGateway.send(new ConfirmOrderCommand(id))
                            .thenCompose(res -> queryGateway.query(new FindOrderByIdQuery(id), OrderView.class)
                                    .thenApply(updatedView -> {
                                        log.info("Order confirmed successfully with ID: {}", id);
                                        return RestResponseUtils.successResponse(updatedView);
                                    }));
                });
    }

    @PostMapping("/{id}/close")
    public CompletableFuture<ResponseEntity<RestResponse<OrderView>>> closeOrder(@PathVariable UUID id) {
        log.info("Closing order with ID: {}", id);

        return queryGateway.query(new FindOrderByIdQuery(id), OrderView.class)
                .thenCompose(orderView -> {
                    if (orderView == null) {
                        log.warn("Cannot close - order not found with ID: {}", id);
                        return CompletableFuture.completedFuture(RestResponseUtils.notFoundResponse());
                    }

                    return commandGateway.send(new CloseOrderCommand(id))
                            .thenCompose(res -> queryGateway.query(new FindOrderByIdQuery(id), OrderView.class)
                                    .thenApply(updatedView -> {
                                        log.info("Order closed successfully with ID: {}", id);
                                        return RestResponseUtils.successResponse(updatedView);
                                    }));
                });
    }

    @PostMapping("/{id}/open")
    public CompletableFuture<ResponseEntity<RestResponse<OrderView>>> reopenOrder(@PathVariable UUID id) {
        log.info("Reopening order with ID: {}", id);

        return queryGateway.query(new FindOrderByIdQuery(id), OrderView.class)
                .thenCompose(orderView -> {
                    if (orderView == null) {
                        log.warn("Cannot reopen - order not found with ID: {}", id);
                        return CompletableFuture.completedFuture(RestResponseUtils.notFoundResponse());
                    }

                    return commandGateway.send(new ReopenOrderCommand(id))
                            .thenCompose(res -> queryGateway.query(new FindOrderByIdQuery(id), OrderView.class)
                                    .thenApply(updatedView -> {
                                        log.info("Order reopened successfully with ID: {}", id);
                                        return RestResponseUtils.successResponse(updatedView);
                                    }));
                });
    }

    @PostMapping("/{id}/cancel")
    public CompletableFuture<ResponseEntity<RestResponse<OrderView>>> cancelOrder(
            @PathVariable UUID id,
            @RequestBody @Valid CancelOrderRequest request,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return CompletableFuture.completedFuture(RestResponseUtils.badRequestResponse(bindingResult));

        log.info("Cancelling order with ID: {}, reason: {}", id, request.getReason());

        return queryGateway.query(new FindOrderByIdQuery(id), OrderView.class)
                .thenCompose(orderView -> {
                    if (orderView == null) {
                        log.warn("Cannot cancel - order not found with ID: {}", id);
                        return CompletableFuture.completedFuture(RestResponseUtils.notFoundResponse());
                    }

                    return commandGateway.send(new CancelOrderCommand(id, request.getReason(), request.getNote()))
                            .thenCompose(res -> queryGateway.query(new FindOrderByIdQuery(id), OrderView.class)
                                    .thenApply(updatedView -> {
                                        log.info("Order cancelled successfully with ID: {}", id);
                                        return RestResponseUtils.successResponse(updatedView);
                                    }));
                });
    }

    @PostMapping("/{id}/tags")
    public CompletableFuture<ResponseEntity<RestResponse<OrderTagsResponse>>> addOrderTags(
            @PathVariable UUID id,
            @RequestBody @Valid OrderTagsRequest request,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return CompletableFuture.completedFuture(RestResponseUtils.badRequestResponse(bindingResult));

        log.info("Adding tags to order with ID: {}, tags: {}", id, request.getTags());

        return queryGateway.query(new FindOrderByIdQuery(id), OrderView.class)
                .thenCompose(orderView -> {
                    if (orderView == null) {
                        log.warn("Cannot add tags - order not found with ID: {}", id);
                        return CompletableFuture.completedFuture(RestResponseUtils.notFoundResponse());
                    }

                    return commandGateway.send(new AddOrderTagsCommand(id, request.getTags()))
                            .thenCompose(res -> queryGateway.query(new FindOrderByIdQuery(id), OrderView.class)
                                    .thenApply(updatedView -> {
                                        log.info("Tags added successfully to order ID: {}", id);
                                        OrderTagsResponse response = new OrderTagsResponse(updatedView.getTags());
                                        return RestResponseUtils.successResponse(response);
                                    }));
                });
    }

    @DeleteMapping("/{id}/tags")
    public CompletableFuture<ResponseEntity<RestResponse<OrderTagsResponse>>> deleteOrderTags(
            @PathVariable UUID id,
            @RequestBody @Valid OrderTagsRequest request,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return CompletableFuture.completedFuture(RestResponseUtils.badRequestResponse(bindingResult));

        log.info("Removing tags from order with ID: {}, tags: {}", id, request.getTags());

        return queryGateway.query(new FindOrderByIdQuery(id), OrderView.class)
                .thenCompose(orderView -> {
                    if (orderView == null) {
                        log.warn("Cannot remove tags - order not found with ID: {}", id);
                        return CompletableFuture.completedFuture(RestResponseUtils.notFoundResponse());
                    }

                    return commandGateway.send(new DeleteOrderTagsCommand(id, request.getTags()))
                            .thenCompose(res -> queryGateway.query(new FindOrderByIdQuery(id), OrderView.class)
                                    .thenApply(updatedView -> {
                                        log.info("Tags removed successfully from order ID: {}", id);
                                        OrderTagsResponse response = new OrderTagsResponse(updatedView.getTags());
                                        return RestResponseUtils.successResponse(response);
                                    }));
                });
    }
}
