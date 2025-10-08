package hanniejewelry.vn.shipping.api;

import hanniejewelry.vn.customer.entity.CustomerAddress;
import hanniejewelry.vn.order.dto.OrderRequest;
import hanniejewelry.vn.order.entity.Order;


public interface ShippingFacade {
    

    void setShippingAddress(Order order, OrderRequest.OrderInfo orderInfo, CustomerAddress defaultAddress);
    

    void setBillingAddress(Order order, OrderRequest.OrderInfo orderInfo, CustomerAddress defaultAddress);
} 