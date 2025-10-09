package hanniejewelry.vn.order.api.dto;

import hanniejewelry.vn.order.entity.Order;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
@Builder
public class OrderTrackingDTO {
    private UUID id;
    private String orderCode;
    private String orderNumber;
    private String name;
    private String email;
    private BigDecimal totalPrice;
    private String financialStatus;
    private String fulfillmentStatus;
    private String orderProcessingStatus;
    private String tags;
    private String gateway;
    private String note;
    private String source;
    private String sourceName;
    private Instant createdAt;
    private Instant updatedAt;
    private String cartToken;
    private List<LineItemDTO> lineItems;
    private List<TimelineDTO> timelines;
    
    @Data
    @Builder
    public static class LineItemDTO {
        private UUID id;
        private Long variantId;
        private UUID productId;
        private String productTitle;
        private String variantTitle;
        private BigDecimal price;
        private Integer quantity;
        private BigDecimal lineAmount;
    }
    
    @Data
    @Builder
    public static class TimelineDTO {
        private Instant createdAt;
        private String action;
        private BigDecimal amount;
        private BigDecimal remainAmount;
        private String gateway;
    }
    

    public static OrderTrackingDTO fromEntity(Order order) {
        List<LineItemDTO> lineItemDTOs = order.getLineItems().stream()
            .map(item -> LineItemDTO.builder()
                .id(item.getId())
                .variantId(item.getVariantId())
                .productId(item.getProductId())
                .productTitle(item.getTitle())
                .variantTitle(item.getVariantTitle())
                .price(item.getPrice())
                .quantity(item.getQuantity())
                .lineAmount(item.getPrice().multiply(new BigDecimal(item.getQuantity())))
                .build())
            .collect(Collectors.toList());
        
        return OrderTrackingDTO.builder()
            .id(order.getId())
            .orderCode(order.getOrderCode())
            .orderNumber(order.getOrderNumber())
            .name(order.getName())
            .email(order.getEmail())
            .totalPrice(order.getTotalPrice())
            .financialStatus(order.getFinancialStatus() != null ? order.getFinancialStatus().name() : null)
            .fulfillmentStatus(order.getFulfillmentStatus() != null ? order.getFulfillmentStatus().name() : null)
            .orderProcessingStatus(order.getOrderProcessingStatus() != null ? order.getOrderProcessingStatus().name() : null)
            .tags(order.getTags())
            .gateway(order.getGateway())
            .note(order.getNote())
            .source(order.getSource())
            .sourceName(order.getSourceName())
            .createdAt(order.getCreatedAt())
            .updatedAt(order.getUpdatedAt())
            .cartToken(order.getCartToken())
            .lineItems(lineItemDTOs)
            .build();
    }
} 