package hanniejewelry.vn.customer.service;

import hanniejewelry.vn.customer.api.CustomerFacade;
import hanniejewelry.vn.customer.entity.Customer;
import hanniejewelry.vn.customer.entity.CustomerAddress;
import hanniejewelry.vn.customer.repository.CustomerRepository;
import hanniejewelry.vn.order.dto.OrderRequest;
import hanniejewelry.vn.order.entity.Order;
import hanniejewelry.vn.shared.utils.BigDecimalUtils;
import hanniejewelry.vn.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerService implements CustomerFacade {
    private final CustomerRepository customerRepository;

    @Override
    public CustomerData findCustomerData(OrderRequest.OrderInfo orderInfo) {
        Customer customer = null;
        CustomerAddress defaultAddress = null;

        String formattedPhone = null;
        if (orderInfo.getShippingAddress() != null && orderInfo.getShippingAddress().getPhone() != null) {
            formattedPhone = formatPhoneNumber(orderInfo.getShippingAddress().getPhone());
        }
        String formattedEmail = formatEmail(orderInfo.getEmail(), formattedPhone);

        if (formattedEmail != null && !formattedEmail.isEmpty()) {
            Optional<Customer> customerByEmail = customerRepository.findByEmail(formattedEmail);
            if (customerByEmail.isPresent()) {
                customer = customerByEmail.get();
                log.info("Found customer by email: {}", formattedEmail);
            }
        }

        if (customer == null && formattedPhone != null && !formattedPhone.isEmpty()) {
            Optional<Customer> customerByPhone = customerRepository.findByPhone(formattedPhone);
            if (customerByPhone.isPresent()) {
                customer = customerByPhone.get();
                log.info("Found customer by phone: {}", formattedPhone);
                if (formattedEmail != null && !formattedEmail.isEmpty() &&
                        !formattedEmail.equals(customer.getEmail())) {
                    customer.setEmail(formattedEmail);
                    customer = customerRepository.save(customer);
                    log.info("Updated email for customer found by phone: {}", formattedEmail);
                }
            }
        }

        if (customer == null && (formattedEmail != null || formattedPhone != null)) {
            String firstName = orderInfo.getShippingAddress() != null ? orderInfo.getShippingAddress().getFirstName() : null;
            String lastName = orderInfo.getShippingAddress() != null ? orderInfo.getShippingAddress().getLastName() : null;
            customer = findOrCreateCustomer(formattedEmail, formattedPhone, firstName, lastName);
            log.info("Created new customer with email: {} and phone: {}", formattedEmail, formattedPhone);
        }

        if (customer != null) {
            for (CustomerAddress address : customer.getAddresses()) {
                if (Boolean.TRUE.equals(address.getIsDefault())) {
                    defaultAddress = address;
                    break;
                }
            }
        }
        return new CustomerData(customer, defaultAddress);
    }

    @Override
    @Transactional
    public void updateCustomerOrderInfo(Customer customer, Order order) {
        if (customer != null && order != null) {
            try {
                log.info("Updating customer {} order info: orderId={}, orderName={}, createdAt={}",
                        customer.getId(), order.getId(), order.getOrderNumber(), order.getCreatedAt());

                // Update last order information
                customer.setLastOrderId(order.getId());
                customer.setLastOrderName(order.getOrderNumber());

                // Ensure last_order_date is set
                Instant orderDate = order.getCreatedAt();
                if (orderDate == null) {
                    orderDate = Instant.now();
                    log.warn("Order {} has null createdAt, using current time", order.getOrderNumber());
                }
                customer.setLastOrderDate(orderDate);

                // Update order count - FIX: Get current count first, then increment by exactly 1
                Integer ordersCount = customer.getOrdersCount();
                if (ordersCount == null) {
                    ordersCount = 0;
                }

                // IMPORTANT: Only increment by 1, not in both CreateOrderOrchestrator and here
                customer.setOrdersCount(ordersCount + 1);
                log.info("Incrementing orders_count from {} to {}", ordersCount, ordersCount + 1);

                // Update total spent
                BigDecimal newTotalSpent = BigDecimalUtils.add(customer.getTotalSpent(), order.getTotalPrice());
                customer.setTotalSpent(newTotalSpent);

                // Save the customer
                customer = customerRepository.save(customer);

                // Set customer ID on order
                order.setUserId(customer.getId());

                log.info("Successfully updated customer {} with order info: ordersCount={}, lastOrderDate={}, totalSpent={}",
                        customer.getId(), customer.getOrdersCount(), customer.getLastOrderDate(), customer.getTotalSpent());
            } catch (Exception e) {
                log.error("Error updating customer order info: {}", e.getMessage(), e);
            }
        } else {
            log.warn("Cannot update order info: customer={}, order={}",
                    customer != null ? customer.getId() : "null",
                    order != null ? order.getId() : "null");
        }
    }

    @Transactional
    public void createCustomerProfile(User user) {
        if (user.getCustomer() != null) {
            return;
        }

        Customer customer = Customer.builder()
                .user(user)
                .email(user.getEmail())
                .phone(user.getPhone())
                .ordersCount(0)
                .totalSpent(BigDecimal.ZERO)
                .totalPaid(BigDecimal.ZERO)
                .acceptsMarketing(true)
                .build();

        customerRepository.save(customer);
    }

    @Override
    @Transactional
    public Customer findOrCreateCustomer(String email, String phone, String firstName, String lastName) {
        log.info("Finding or creating customer with email: {} or phone: {}", email, phone);
        if (email != null && !email.isEmpty()) {
            Optional<Customer> existingCustomer = customerRepository.findByEmail(email);
            if (existingCustomer.isPresent()) {
                log.info("Found existing customer by email: {}", email);
                if (phone != null && !phone.isEmpty() && !phone.equals(existingCustomer.get().getPhone())) {
                    existingCustomer.get().setPhone(phone);
                    log.info("Updated phone for existing customer: {}", phone);
                    return customerRepository.save(existingCustomer.get());
                }
                return existingCustomer.get();
            }
        }
        if (phone != null && !phone.isEmpty()) {
            Optional<Customer> existingCustomer = customerRepository.findByPhone(phone);
            if (existingCustomer.isPresent()) {
                log.info("Found existing customer by phone: {}", phone);
                if (email != null && !email.isEmpty() && !email.equals(existingCustomer.get().getEmail())) {
                    existingCustomer.get().setEmail(email);
                    log.info("Updated email for existing customer: {}", email);
                    return customerRepository.save(existingCustomer.get());
                }
                return existingCustomer.get();
            }
        }
        log.info("Creating new customer with email: {} and phone: {}", email, phone);
        Customer newCustomer = Customer.builder()
                .email(email)
                .phone(phone)
                .firstName(firstName)
                .lastName(lastName)
                .ordersCount(0)
                .totalSpent(BigDecimal.ZERO)
                .totalPaid(BigDecimal.ZERO)
                .acceptsMarketing(true)
                .build();
        return customerRepository.save(newCustomer);
    }

    private String formatPhoneNumber(String phone) {
        if (phone == null || phone.isEmpty()) {
            return phone;
        }

        if (!phone.startsWith("+")) {
            if (phone.startsWith("0")) {
                return "+84" + phone.substring(1);
            } else {
                return "+84" + phone;
            }
        }

        return phone;
    }

    private String formatEmail(String email, String phone) {
        if (email == null || email.isEmpty()) {
            if (phone != null && !phone.isEmpty()) {
                return phone + "@no-email.local";
            }
            return null;
        }

        if (!email.contains("@")) {
            return email + "@no-email.local";
        }

        return email;
    }


} 