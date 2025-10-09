package hanniejewelry.vn.payment.service;

import hanniejewelry.vn.customer.entity.Customer;
import hanniejewelry.vn.customer.entity.CustomerAddress;
import hanniejewelry.vn.customer.repository.CustomerRepository;
import hanniejewelry.vn.order.*;
import hanniejewelry.vn.order.api.mapper.CartToOrderConverter;
import hanniejewelry.vn.order.dto.CheckoutResponseView;
import hanniejewelry.vn.order.dto.OrderRequest;
import hanniejewelry.vn.order.dto.ShippingMethodView;
import hanniejewelry.vn.payment.dto.PaymentMethodView;
import hanniejewelry.vn.payment.mapper.CheckoutMapper;
import hanniejewelry.vn.product.entity.ProductVariant;
import hanniejewelry.vn.product.repository.ProductVariantRepository;
import hanniejewelry.vn.shopping_cart.service.ShippingMethodService;
import hanniejewelry.vn.shared.exception.BaseMessageType;
import hanniejewelry.vn.shared.exception.BizException;
import hanniejewelry.vn.shared.dto.RestResponse;
import hanniejewelry.vn.shared.utils.SecurityUtils;
import hanniejewelry.vn.shopping_cart.dto.CartAttributeRequest;
import hanniejewelry.vn.shopping_cart.dto.CheckoutSessionRequest;
import hanniejewelry.vn.shopping_cart.service.ShoppingCartService;
import hanniejewelry.vn.shopping_cart.view.ShoppingCartView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CheckoutService {

    private final ShoppingCartService shoppingCartService;
    private final ShippingMethodService shippingMethodService;
    private final PaymentMethodService paymentMethodService;
    private final ValidateCreateOrderUseCase validateCreateOrderUseCase;
    private final CheckoutMapper checkoutMapper;
    private final ProductVariantRepository productVariantRepository;
    private final CustomerRepository customerRepository;

    /** Lấy thông tin checkout */
    public CheckoutResponseView getCheckout() {
        ShoppingCartView cart = shoppingCartService.getCurrentCustomerCart();
        List<ShippingMethodView> shippingMethods = shippingMethodService.getAllActiveShippingMethods();
        List<PaymentMethodView> paymentMethods = paymentMethodService.getAllActivePaymentMethods();

        CheckoutResponseView response = checkoutMapper.toCheckoutResponseView(cart, shippingMethods, paymentMethods);
        response.setAuthEmail(SecurityUtils.getCurrentUser().getEmail());
        response.setAuthName(SecurityUtils.getCurrentUser().getEmail());
        return response;
    }

    /** Cập nhật checkout: chỉ lấy địa chỉ từ DB bằng addressId */
    @Transactional
    public CheckoutResponseView updateCheckout(CheckoutSessionRequest request) {
        UUID customerId = SecurityUtils.getCurrentCustomer().getId();
        
        Map<String, String> attributes = new HashMap<>();

        // Clear existing attributes first
        shoppingCartService.clearCartAttributesForCurrentCustomer();
        
        // Nếu có address_id, chỉ validate (không lưu vào attributes)
        if (request.getAddressId() != null) {
            Customer customer = customerRepository.findByIdWithAddresses(customerId)
                    .orElseThrow(() -> new BizException(BaseMessageType.NOT_FOUND, "Customer not found"));

            // Chỉ validate address tồn tại, không lưu vào attributes
            boolean addressExists = customer.getAddresses().stream()
                    .anyMatch(addr -> addr.getId().equals(request.getAddressId()));
            
            if (!addressExists) {
                throw new BizException(BaseMessageType.NOT_FOUND, "Không tìm thấy địa chỉ đã chọn");
            }
        }
        
        // Chỉ lưu user attributes vào cart attributes
        if (request.getAttributes() != null && !request.getAttributes().isEmpty()) {
            for (CartAttributeRequest attr : request.getAttributes()) {
                attributes.put(attr.getKey(), attr.getValue());
            }
        }

        // Cập nhật attributes nếu có
        if (!attributes.isEmpty()) {
            List<CartAttributeRequest> attributeRequests = attributes.entrySet().stream()
                    .map(e -> new CartAttributeRequest(e.getKey(), e.getValue()))
                    .collect(Collectors.toList());
            shoppingCartService.updateCartAttributesForCurrentCustomer(attributeRequests);
        }

        // Cập nhật note nếu có
        if (request.getNote() != null) {
            shoppingCartService.updateCartNoteForCurrentCustomer(request.getNote());
        }

        return getCheckout();
    }

    /** Hoàn tất checkout */
    public CompletableFuture<RestResponse<CreateOrderResult>> completeCheckout() {
        ShoppingCartView cart = validateCartAndInventory();
        OrderRequest orderRequest = CartToOrderConverter.convertToOrderRequest(cart, new CheckoutSessionRequest());
        return createOrder(orderRequest);
    }

    /** Đặt hàng trực tiếp */
    public CompletableFuture<RestResponse<CreateOrderResult>> directCheckout(OrderRequest request) {
        validateCartAndInventory();
        return createOrder(request);
    }

    /** Kiểm tra tồn kho */
    public boolean validateInventory() {
        ShoppingCartView cart = shoppingCartService.getCurrentCustomerCart();
        if (cart == null || cart.getItems() == null || cart.getItems().isEmpty()) return true;

        for (var item : cart.getItems()) {
            ProductVariant variant = productVariantRepository.findById(item.getVariantId()).orElse(null);
            if (variant == null) return false;
            if (variant.getInventoryAdvance() == null) continue;

            int requested = item.getQuantity();
            int available = Optional.ofNullable(variant.getInventoryAdvance().getQtyAvailable()).orElse(0);
            if (requested > available) return false;
        }
        return true;
    }

    // ======================== Private Helpers ========================

    private ShoppingCartView validateCartAndInventory() {
        ShoppingCartView cart = shoppingCartService.getCurrentCustomerCart();
        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            throw new BizException(BaseMessageType.BAD_REQUEST, "Shopping cart is empty");
        }

        for (ShoppingCartView.CartItemView item : cart.getItems()) {
            ProductVariant variant = productVariantRepository.findById(item.getVariantId())
                    .orElseThrow(() -> new BizException(BaseMessageType.NOT_FOUND,
                            "Variant not found with id: " + item.getVariantId()));
            int available = Optional.ofNullable(variant.getInventoryAdvance() != null
                    ? variant.getInventoryAdvance().getQtyAvailable()
                    : variant.getInventoryQuantity()).orElse(0);
            if (item.getQuantity() > available) {
                throw new BizException(BaseMessageType.BAD_REQUEST,
                        "Không đủ hàng trong kho cho sản phẩm " + item.getProductTitle() +
                                ". Chỉ còn " + available + " sản phẩm có sẵn.");
            }
        }
        return cart;
    }

    private CompletableFuture<RestResponse<CreateOrderResult>> createOrder(OrderRequest orderRequest) {
        return validateCreateOrderUseCase.createOrder(orderRequest)
                .thenApply(result -> {
                    if (result.isOk()) {
                        shoppingCartService.clearCurrentCustomerCart();
                        return new RestResponse<>(result.getData(),
                                "Order is being processed. Please query by order_id to get details.", 202);
                    } else {
                        return new RestResponse<>(null, result.getError(), 400);
                    }
                });
    }

}
