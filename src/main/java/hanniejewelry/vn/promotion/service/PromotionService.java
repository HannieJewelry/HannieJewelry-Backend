package hanniejewelry.vn.promotion.service;

import hanniejewelry.vn.order.dto.OrderRequest;
import hanniejewelry.vn.order.entity.Order;
import hanniejewelry.vn.promotion.api.PromotionFacade;
import hanniejewelry.vn.promotion.domain.model.OrderDiscountCode;
import hanniejewelry.vn.shared.utils.BigDecimalUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PromotionService implements PromotionFacade {
    

    @Override
    public void createDiscountCodes(Order order, OrderRequest.OrderInfo orderInfo) {
        if (orderInfo.getDiscountCodes() == null) {
            return;
        }
        
        List<OrderDiscountCode> discountCodes = new ArrayList<>();
        for (OrderRequest.DiscountCode discount : orderInfo.getDiscountCodes()) {
            OrderDiscountCode discountCode = OrderDiscountCode.builder()
                    .order(order)
                    .code(discount.getCode())
                    .amount(BigDecimalUtils.of(discount.getAmount()))
                    .type(discount.getType())
                    .isCouponCode(false)
                    .build();
            discountCodes.add(discountCode);
        }
        
        order.getDiscountCodes().addAll(discountCodes);
    }
    
    /**
     * Calculate discount total
     * @param orderInfo Order information containing discount codes
     * @return Total discount amount
     */
    @Override
    public BigDecimal calculateDiscountTotal(OrderRequest.OrderInfo orderInfo) {
        BigDecimal discountTotal = BigDecimalUtils.zero();
        
        if (orderInfo.getDiscountCodes() == null) {
            return discountTotal;
        }
        
        for (OrderRequest.DiscountCode discount : orderInfo.getDiscountCodes()) {
            if (discount.getAmount() != null) {
                discountTotal = BigDecimalUtils.add(discountTotal, BigDecimalUtils.of(discount.getAmount()));
            }
        }
        
        return discountTotal;
    }
} 