package hanniejewelry.vn.order.config;

import hanniejewelry.vn.payment.entity.PaymentMethod;
import hanniejewelry.vn.order.ShippingMethod;
import hanniejewelry.vn.order.repository.PaymentMethodRepository;
import hanniejewelry.vn.order.repository.ShippingMethodRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class CheckoutDataInitializer {

    private final ShippingMethodRepository shippingMethodRepository;
    private final PaymentMethodRepository paymentMethodRepository;

    @Bean
    public CommandLineRunner initCheckoutData() {
        return args -> {
            if (shippingMethodRepository.count() == 0) {
                log.info("Initializing default shipping methods");

                ShippingMethod homeDelivery = ShippingMethod.builder()
                        .name("Home Delivery")
                        .description("Home delivery within 2-3 business days")
                        .price(BigDecimal.ZERO)
                        .code("home_delivery")
                        .active(true)
                        .displayOrder(1)
                        .build();

                shippingMethodRepository.save(homeDelivery);

                log.info("Created default shipping methods");
            }

            if (paymentMethodRepository.count() == 0) {
                log.info("Initializing default payment methods");

                PaymentMethod cod = PaymentMethod.builder()
                        .name("Cash on Delivery (COD)")
                        .description("Pay with cash when receiving the goods")
                        .code("cod")
                        .online(false)
                        .systemTypeId(1)
                        .active(true)
                        .displayOrder(1)
                        .build();

                PaymentMethod bankTransfer = PaymentMethod.builder()
                        .name("Bank Transfer")
                        .description("Transfer via bank account")
                        .code("bank_transfer")
                        .online(false)
                        .systemTypeId(2)
                        .active(true)
                        .displayOrder(2)
                        .build();

                paymentMethodRepository.save(cod);
                paymentMethodRepository.save(bankTransfer);

                log.info("Created default payment methods");
            }
        };
    }
}