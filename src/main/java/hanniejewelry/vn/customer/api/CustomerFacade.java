package hanniejewelry.vn.customer.api;

import hanniejewelry.vn.customer.entity.Customer;
import hanniejewelry.vn.customer.entity.CustomerAddress;
import hanniejewelry.vn.order.dto.OrderRequest;
import hanniejewelry.vn.order.entity.Order;
import lombok.Getter;


public interface CustomerFacade {
    
    CustomerData findCustomerData(OrderRequest.OrderInfo orderInfo);
    
    void updateCustomerOrderInfo(Customer customer, Order order);
    
    Customer findOrCreateCustomer(String email, String phone, String firstName, String lastName);
    

    @Getter
    class CustomerData {
        private final Customer customer;
        private final CustomerAddress defaultAddress;

        public CustomerData(Customer customer, CustomerAddress defaultAddress) {
            this.customer = customer;
            this.defaultAddress = defaultAddress;
        }

        public boolean hasCustomer() {
            return customer != null;
        }

    }
} 