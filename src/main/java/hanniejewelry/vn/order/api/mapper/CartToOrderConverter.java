package hanniejewelry.vn.order.api.mapper;

import hanniejewelry.vn.order.dto.OrderRequest;
import hanniejewelry.vn.shopping_cart.view.ShoppingCartView;
import hanniejewelry.vn.shopping_cart.dto.CheckoutSessionRequest;
import hanniejewelry.vn.shared.utils.SecurityUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CartToOrderConverter {

    public static OrderRequest convertToOrderRequest(ShoppingCartView cart, CheckoutSessionRequest checkoutSession) {
        OrderRequest orderRequest = new OrderRequest();
        OrderRequest.OrderInfo orderInfo = new OrderRequest.OrderInfo();

        Map<String, String> attributes = cart.getAttributes();

        orderInfo.setCartToken(cart.getToken());

        // 🔒 Bắt buộc user phải đăng nhập => lấy email từ SecurityUtils
        if (SecurityUtils.isAuthenticated() && SecurityUtils.getCurrentUser().getEmail() != null) {
            orderInfo.setEmail(SecurityUtils.getCurrentUser().getEmail());
        } else {
            throw new IllegalStateException("User must be logged in to place an order");
        }

        if (checkoutSession != null && checkoutSession.getNote() != null) {
            orderInfo.setNote(checkoutSession.getNote());
        } else {
            orderInfo.setNote("");
        }

        List<OrderRequest.LineItem> lineItems = cart.getItems().stream()
                .map(item -> {
                    OrderRequest.LineItem lineItem = new OrderRequest.LineItem();
                    lineItem.setVariantId(item.getVariantId());
                    lineItem.setQuantity(item.getQuantity());
                    return lineItem;
                })
                .collect(Collectors.toList());
        orderInfo.setLineItems(lineItems);

        // Shipping address: ưu tiên lấy từ checkoutSession nếu có, không thì từ attributes
        if (checkoutSession != null && checkoutSession.getShippingAddress() != null) {
            OrderRequest.AddressInfo shippingAddress = new OrderRequest.AddressInfo();
            shippingAddress.setAddress1(checkoutSession.getShippingAddress().getAddress());
            shippingAddress.setCompany(checkoutSession.getShippingAddress().getCompany());
            shippingAddress.setZip(checkoutSession.getShippingAddress().getZipCode());

            if (checkoutSession.getFullName() != null) {
                String[] nameParts = checkoutSession.getFullName().split(" ", 2);
                shippingAddress.setFirstName(nameParts[0]);
                shippingAddress.setLastName(nameParts.length > 1 ? nameParts[1] : "");
            }

            if (checkoutSession.getShippingAddress().getCountryId() != null) {
                shippingAddress.setCountryCode(checkoutSession.getShippingAddress().getCountryId().toString());
            }
            if (checkoutSession.getShippingAddress().getProvinceId() != null) {
                shippingAddress.setProvinceCode(checkoutSession.getShippingAddress().getProvinceId().toString());
            }
            if (checkoutSession.getShippingAddress().getDistrictId() != null) {
                shippingAddress.setDistrictCode(checkoutSession.getShippingAddress().getDistrictId().toString());
            }
            if (checkoutSession.getShippingAddress().getWardId() != null) {
                shippingAddress.setWardCode(checkoutSession.getShippingAddress().getWardId().toString());
            }

            orderInfo.setShippingAddress(shippingAddress);
            orderInfo.setBillingAddress(shippingAddress);
        } else if (attributes != null) {
            OrderRequest.AddressInfo shippingAddress = buildAddressFromAttributes(attributes);
            if (shippingAddress != null) {
                orderInfo.setShippingAddress(shippingAddress);
                orderInfo.setBillingAddress(shippingAddress);
            }
        }

        // Shipping method
        if (checkoutSession != null && checkoutSession.getShippingMethodId() != null) {
            List<OrderRequest.ShippingLine> shippingLines = new ArrayList<>();
            OrderRequest.ShippingLine shippingLine = new OrderRequest.ShippingLine();
            shippingLine.setCode(checkoutSession.getShippingMethodId().toString());
            shippingLines.add(shippingLine);
            orderInfo.setShippingLines(shippingLines);
        } else if (attributes != null && attributes.containsKey("shipping_method_id")) {
            List<OrderRequest.ShippingLine> shippingLines = new ArrayList<>();
            OrderRequest.ShippingLine shippingLine = new OrderRequest.ShippingLine();
            shippingLine.setCode(attributes.get("shipping_method_id"));
            shippingLines.add(shippingLine);
            orderInfo.setShippingLines(shippingLines);
        }

        // Note attributes
        if (attributes != null && !attributes.isEmpty()) {
            List<OrderRequest.NoteAttribute> noteAttributes = new ArrayList<>();
            for (Map.Entry<String, String> entry : attributes.entrySet()) {
                OrderRequest.NoteAttribute noteAttribute = new OrderRequest.NoteAttribute();
                noteAttribute.setName(entry.getKey());
                noteAttribute.setValue(entry.getValue());
                noteAttributes.add(noteAttribute);
            }
            orderInfo.setNoteAttributes(noteAttributes);
        }

        if (attributes != null) {
            orderInfo.setUtmSource(attributes.get("utm_source"));
            orderInfo.setUtmMedium(attributes.get("utm_medium"));
            orderInfo.setUtmCampaign(attributes.get("utm_campaign"));
            orderInfo.setUtmTerm(attributes.get("utm_term"));
            orderInfo.setUtmContent(attributes.get("utm_content"));
        }

        orderRequest.setOrder(orderInfo);
        return orderRequest;
    }

    private static OrderRequest.AddressInfo buildAddressFromAttributes(Map<String, String> attributes) {
        if (attributes.containsKey("address") ||
                attributes.containsKey("full_name") ||
                attributes.containsKey("country_id")) {

            OrderRequest.AddressInfo address = new OrderRequest.AddressInfo();

            address.setAddress1(attributes.getOrDefault("address", null));
            address.setCompany(attributes.getOrDefault("company", null));
            address.setZip(attributes.getOrDefault("zip_code", null));

            if (attributes.containsKey("full_name")) {
                String[] nameParts = attributes.get("full_name").split(" ", 2);
                address.setFirstName(nameParts[0]);
                address.setLastName(nameParts.length > 1 ? nameParts[1] : "");
            }

            address.setPhone(attributes.getOrDefault("phone_number", null));
            address.setCountryCode(attributes.getOrDefault("country_id", null));
            address.setProvinceCode(attributes.getOrDefault("province_id", null));
            address.setDistrictCode(attributes.getOrDefault("district_id", null));
            address.setWardCode(attributes.getOrDefault("ward_id", null));

            return address;
        }
        return null;
    }
}
