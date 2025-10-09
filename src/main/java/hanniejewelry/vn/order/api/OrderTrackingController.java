package hanniejewelry.vn.order.api;

import hanniejewelry.vn.order.api.dto.OrderTrackingDTO;
import hanniejewelry.vn.order.entity.Order;
import hanniejewelry.vn.order.repository.OrderRepository;
import hanniejewelry.vn.shared.constants.ApiConstants;
import hanniejewelry.vn.shared.dto.RestResponse;
import hanniejewelry.vn.shared.utils.RestResponseUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@RestController
@RequestMapping(ApiConstants.API_PREFIX + "/client/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderTrackingController {
    
    private final OrderRepository orderRepository;
    

    @GetMapping("/{orderId}/tracking")
    public ResponseEntity<RestResponse<OrderTrackingDTO>> getOrderTracking(@PathVariable UUID orderId) {
        log.info("Fetching order tracking for order ID: {}", orderId);
        
        return orderRepository.findById(orderId)
                .map(order -> {
                    log.info("Found order tracking for order ID: {}", orderId);
                    OrderTrackingDTO dto = OrderTrackingDTO.fromEntity(order);
                    return RestResponseUtils.successResponse(dto);
                })
                .orElseGet(() -> {
                    log.warn("Order not found for tracking with ID: {}", orderId);
                    return RestResponseUtils.notFoundResponse("Order not found");
                });
    }
    

    @GetMapping("/tracking")
    @Transactional(readOnly = true)
    public ResponseEntity<RestResponse<List<OrderTrackingDTO>>> getOrderTrackingByToken(
            @RequestParam("token") String cartToken) {
        
        log.info("Fetching order tracking for cart token: {}", cartToken);
        
        List<Order> orders = orderRepository.findByCartToken(cartToken);
        
        if (orders.isEmpty()) {
            log.warn("No orders found for tracking with cart token: {}", cartToken);
            return RestResponseUtils.notFoundResponse("No orders found for this cart token");
        }
        
        List<OrderTrackingDTO> dtos = orders.stream()
                .map(OrderTrackingDTO::fromEntity)
                .collect(Collectors.toList());
        
        log.info("Found {} orders for tracking with cart token: {}", orders.size(), cartToken);
        return RestResponseUtils.successResponse(dtos);
    }
} 