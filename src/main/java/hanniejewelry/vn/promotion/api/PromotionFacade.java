package hanniejewelry.vn.promotion.api;

import hanniejewelry.vn.order.dto.OrderRequest;
import hanniejewelry.vn.order.entity.Order;

import java.math.BigDecimal;


public interface PromotionFacade {
    

    void createDiscountCodes(Order order, OrderRequest.OrderInfo orderInfo);
    

    BigDecimal calculateDiscountTotal(OrderRequest.OrderInfo orderInfo);
} 