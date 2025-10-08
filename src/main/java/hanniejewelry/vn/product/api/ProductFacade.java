package hanniejewelry.vn.product.api;

import hanniejewelry.vn.order.dto.OrderRequest;
import hanniejewelry.vn.product.entity.Product;
import hanniejewelry.vn.product.entity.ProductVariant;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;


public interface ProductFacade {
    

    Map<Long, ProductVariant> loadVariants(OrderRequest.OrderInfo orderInfo);

    Map<UUID, Product> loadProducts(Map<Long, ProductVariant> variantsMap);
    
    BigDecimal calculateLineItemsTotal(OrderRequest.OrderInfo orderInfo, Map<Long, ProductVariant> variantsMap);
    boolean validateVariants(Map<Long, ProductVariant> variantsMap, UUID orderId);
    
    Map<Long, Boolean> validateVariantsWithDetails(OrderRequest.OrderInfo orderInfo, UUID orderId);
} 