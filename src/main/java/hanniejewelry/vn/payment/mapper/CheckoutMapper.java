package hanniejewelry.vn.payment.mapper;

import hanniejewelry.vn.payment.dto.PaymentMethodView;
import hanniejewelry.vn.order.api.dto.LineItemDTO;
import hanniejewelry.vn.order.api.mapper.LineItemMapper;
import hanniejewelry.vn.order.dto.CheckoutResponseView;
import hanniejewelry.vn.order.dto.ShippingMethodView;
import hanniejewelry.vn.shopping_cart.view.ShoppingCartView;
import org.mapstruct.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Mapper(componentModel = "spring",
        imports = {ArrayList.class, Instant.class, BigDecimal.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CheckoutMapper {

    @Mapping(target = "id", source = "cart.token")
    @Mapping(target = "lineItems", expression = "java(mapLineItems(cart))")
    @Mapping(target = "shippingMethods", source = "shippingMethods")
    @Mapping(target = "paymentMethods", source = "paymentMethods")
    @Mapping(target = "attributes", source = "cart.attributes")
    @Mapping(target = "note", source = "cart.note")
    @Mapping(target = "requiresShipping", constant = "true")
    @Mapping(target = "createdAt", expression = "java(Instant.now())")
    @Mapping(target = "updatedAt", expression = "java(Instant.now())")
    @Mapping(target = "completedAt", expression = "java(Instant.now())")
    @Mapping(target = "errors", expression = "java(new ArrayList<>())")
    @Mapping(target = "warnings", expression = "java(new ArrayList<>())")
    @Mapping(target = "userErrors", expression = "java(new ArrayList<>())")
    @Mapping(target = "ready", constant = "true")
    @Mapping(target = "discountAllocations", expression = "java(new ArrayList<>())")
    @Mapping(target = "giftCards", expression = "java(new ArrayList<>())")
    @Mapping(target = "subTotal", source = "cart.totalPrice")
    @Mapping(target = "subTotalBeforeTax", source = "cart.totalPrice")
    @Mapping(target = "subTotalTax", expression = "java(BigDecimal.ZERO)")
    @Mapping(target = "shipping", expression = "java(BigDecimal.ZERO)")
    @Mapping(target = "shippingBeforeTax", source = "cart.totalPrice")
    @Mapping(target = "shippingTax", expression = "java(BigDecimal.ZERO)")
    @Mapping(target = "totalTaxIncluded", constant = "0")
    @Mapping(target = "totalTaxNotIncluded", expression = "java(BigDecimal.ZERO)")
    @Mapping(target = "total", source = "cart.totalPrice")
    @Mapping(target = "discount", expression = "java(BigDecimal.ZERO)")
    @Mapping(target = "depositAmount", expression = "java(BigDecimal.ZERO)")
    @Mapping(target = "depositCODAmount", constant = "0")
    @Mapping(target = "payDepositOnly", constant = "false")
    @Mapping(target = "depositRequired", constant = "false")
    @Mapping(target = "channel", constant = "web")
    @Mapping(target = "disallowLoyaltyProgram", constant = "false")
    @Mapping(target = "sendNotify", constant = "true")
    @Mapping(target = "sendReceipt", constant = "true")
    @Mapping(target = "allowPickAtLocation", constant = "false")
    @Mapping(target = "pickAtLocation", constant = "false")
    @Mapping(target = "utmParameters", expression = "java(createDefaultUtmParameters())")
    @Mapping(target = "locationId", ignore = true)
    CheckoutResponseView toCheckoutResponseView(ShoppingCartView cart, 
                                              List<ShippingMethodView> shippingMethods,
                                              List<PaymentMethodView> paymentMethods);

    @AfterMapping
    default void updateCheckoutResponseFromAttributes(@MappingTarget CheckoutResponseView response, ShoppingCartView cart,
                                                    List<ShippingMethodView> shippingMethods, List<PaymentMethodView> paymentMethods) {
        Map<String, String> attributes = cart.getAttributes();
        if (attributes != null) {
            // No mapping needed - attributes will only contain user-provided data

            // E-Invoice information
            if (attributes.containsKey("e_invoice_request")) {
                response.setEInvoiceRequest(Boolean.parseBoolean(attributes.get("e_invoice_request")));
            }
            
            // Create E-Invoice info if needed
            if (response.getEInvoiceRequest() != null && response.getEInvoiceRequest()) {
                CheckoutResponseView.EInvoiceInfo eInvoiceInfo = new CheckoutResponseView.EInvoiceInfo();
                eInvoiceInfo.setCompany(attributes.containsKey("e_invoice_company") ? 
                        Boolean.parseBoolean(attributes.get("e_invoice_company")) : false);
                eInvoiceInfo.setName(attributes.getOrDefault("e_invoice_name", ""));
                eInvoiceInfo.setTaxCode(attributes.getOrDefault("e_invoice_tax_code", ""));
                eInvoiceInfo.setAddress(attributes.getOrDefault("e_invoice_address", ""));
                eInvoiceInfo.setEmail(attributes.getOrDefault("e_invoice_email", ""));
                eInvoiceInfo.setSave(attributes.containsKey("e_invoice_save") ? 
                        Boolean.parseBoolean(attributes.get("e_invoice_save")) : false);
                eInvoiceInfo.setSubmit(attributes.containsKey("e_invoice_submit") ? 
                        Boolean.parseBoolean(attributes.get("e_invoice_submit")) : false);
                
                response.setEInvoiceInfo(eInvoiceInfo);
            }
            
            // Set default phone country code
            response.setPhoneCountryCode("+84");
        }
    }

    default CheckoutResponseView.UtmParameters createDefaultUtmParameters() {
        return CheckoutResponseView.UtmParameters.builder()
                .landingSite("/")
                .build();
    }

    default List<LineItemDTO> mapLineItems(ShoppingCartView cart) {
        // Use LineItemMapper to directly map cart items to LineItemDTOs
        return LineItemMapper.fromCartItemViewList(cart.getItems());
    }
} 