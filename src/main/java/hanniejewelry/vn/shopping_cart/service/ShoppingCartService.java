package hanniejewelry.vn.shopping_cart.service;

import com.blazebit.persistence.view.EntityViewManager;
import hanniejewelry.vn.customer.entity.Customer;
import hanniejewelry.vn.product.entity.Product;
import hanniejewelry.vn.product.entity.ProductVariant;
import hanniejewelry.vn.product.repository.ProductVariantRepository;
import hanniejewelry.vn.shared.exception.BaseMessageType;
import hanniejewelry.vn.shared.exception.BizException;
import hanniejewelry.vn.shared.utils.SecurityUtils;
import hanniejewelry.vn.shopping_cart.dto.CartAttributeRequest;
import hanniejewelry.vn.shopping_cart.view.ShoppingCartView;
import hanniejewelry.vn.shopping_cart.dto.CartChangeRequest;
import hanniejewelry.vn.shopping_cart.dto.CartItemRequest;
import hanniejewelry.vn.shopping_cart.entity.CartItem;
import hanniejewelry.vn.shopping_cart.entity.ShoppingCart;
import hanniejewelry.vn.shopping_cart.repository.CartItemRepository;
import hanniejewelry.vn.shopping_cart.repository.ShoppingCartRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductVariantRepository productVariantRepository;
    private final EntityViewManager evm;
    private final EntityManager em;

    private static final int MAX_RETRY_ATTEMPTS = 3;

    @Retryable(
            value = {OptimisticLockingFailureException.class},
            maxAttempts = MAX_RETRY_ATTEMPTS,
            backoff = @Backoff(delay = 200)
    )
    private ShoppingCart getOrCreateCustomerCart(UUID customerId) {
        return shoppingCartRepository.findByCustomerId(customerId)
                .orElseGet(() -> {
                    ShoppingCart newCart = ShoppingCart.builder()
                            .token(UUID.randomUUID().toString())
                            .itemCount(0)
                            .totalPrice(BigDecimal.ZERO)
                            .totalWeight(BigDecimal.ZERO)
                            .requiresShipping(false)
                            .customer(em.getReference(Customer.class, customerId))
                            .build();
                    return shoppingCartRepository.save(newCart);
                });
    }

    public ShoppingCartView getCurrentCustomerCart() {
        return getCustomerCartView(SecurityUtils.getCurrentCustomer().getId());
    }

    public ShoppingCartView addItemToCurrentCustomerCart(CartItemRequest request) {
        UUID customerId = SecurityUtils.getCurrentCustomer().getId();
        ShoppingCart cart = getOrCreateCustomerCart(customerId);
        addOrUpdateCartItem(cart, request);
        return getCustomerCartView(customerId);
    }

    public ShoppingCartView changeItemInCurrentCustomerCart(CartChangeRequest request) {
        UUID customerId = SecurityUtils.getCurrentCustomer().getId();
        ShoppingCart cart = getOrCreateCustomerCart(customerId);
        changeCartItem(cart, request);
        return getCustomerCartView(customerId);
    }

    public ShoppingCartView clearCurrentCustomerCart() {
        UUID customerId = SecurityUtils.getCurrentCustomer().getId();
        ShoppingCart cart = getOrCreateCustomerCart(customerId);
        clearCart(cart);
        return getCustomerCartView(customerId);
    }

    public ShoppingCartView updateCartNoteForCurrentCustomer(String note) {
        UUID customerId = SecurityUtils.getCurrentCustomer().getId();
        ShoppingCart cart = getOrCreateCustomerCart(customerId);
        cart.setNote(note);
        shoppingCartRepository.save(cart);
        return getCustomerCartView(customerId);
    }

    public ShoppingCartView updateCartAttributesForCurrentCustomer(List<CartAttributeRequest> attributes) {
        UUID customerId = SecurityUtils.getCurrentCustomer().getId();
        ShoppingCart cart = getOrCreateCustomerCart(customerId);
        setCartAttributes(cart, attributes);
        return getCustomerCartView(customerId);
    }

    public ShoppingCartView getAnonymousCart(String cartToken) {
        ShoppingCart cart = getCartByToken(cartToken);
        return getCartView(cart);
    }

    public ShoppingCartView addItemToAnonymousCart(String cartToken, CartItemRequest request) {
        ShoppingCart cart = getCartByToken(cartToken);
        addOrUpdateCartItem(cart, request);
        return getCartView(cart);
    }

    public ShoppingCartView changeItemInAnonymousCart(String cartToken, CartChangeRequest request) {
        ShoppingCart cart = getCartByToken(cartToken);
        changeCartItem(cart, request);
        return getCartView(cart);
    }

    public ShoppingCartView clearAnonymousCart(String cartToken) {
        ShoppingCart cart = getCartByToken(cartToken);
        clearCart(cart);
        return getCartView(cart);
    }

    public ShoppingCartView updateCartNoteForAnonymousCart(String cartToken, String note) {
        ShoppingCart cart = getCartByToken(cartToken);
        cart.setNote(note);
        shoppingCartRepository.save(cart);
        return getCartView(cart);
    }

    public ShoppingCartView updateCartAttributesForAnonymousCart(String cartToken, List<CartAttributeRequest> attributes) {
        ShoppingCart cart = getCartByToken(cartToken);
        setCartAttributes(cart, attributes);
        return getCartView(cart);
    }

    public ShoppingCartView clearCartAttributesForCurrentCustomer() {
        UUID customerId = SecurityUtils.getCurrentCustomer().getId();
        ShoppingCart cart = getOrCreateCustomerCart(customerId);
        clearCartAttributes(cart);
        return getCustomerCartView(customerId);
    }

    // --------- PRIVATE REUSABLE METHODS ----------

    private ShoppingCart getCartByToken(String cartToken) {
        if (cartToken == null || cartToken.trim().isEmpty()) {
            throw new BizException(BaseMessageType.NOT_FOUND, "Không tìm thấy cart_token hoặc cart_token rỗng");
        }
        return shoppingCartRepository.findByToken(cartToken)
                .orElseThrow(() -> new BizException(BaseMessageType.NOT_FOUND, "Không tìm thấy giỏ hàng theo cart_token"));
    }

    private ShoppingCartView getCustomerCartView(UUID customerId) {
        ShoppingCart cart = getOrCreateCustomerCart(customerId);
        return getCartView(cart);
    }

    private ShoppingCartView getCartView(ShoppingCart cart) {
        return evm.find(em, ShoppingCartView.class, cart.getId());
    }

    private void addOrUpdateCartItem(ShoppingCart cart, CartItemRequest request) {
        ProductVariant variant = productVariantRepository.findById(request.getVariantId())
                .orElseThrow(() -> new BizException(BaseMessageType.NOT_FOUND, "Variant not found"));
        Product product = variant.getProduct();

        if (request.getProductId() != null && !product.getId().equals(request.getProductId())) {
            throw new BizException(BaseMessageType.VALIDATION_ERROR, "Product ID does not match the variant's product");
        }

        int availableQuantity = Optional.ofNullable(variant.getInventoryAdvance())
                .map(inv -> inv.getQtyAvailable())
                .orElse(Optional.ofNullable(variant.getInventoryQuantity()).orElse(0));

        CartItem existingItem = cartItemRepository.findByCartIdAndProductIdAndVariantIdValue(
                cart.getId(), product.getId(), variant.getId()).orElse(null);

        int totalRequested = request.getQuantity() + (existingItem != null ? existingItem.getQuantity() : 0);

        if (totalRequested > availableQuantity) {
            throw new BizException(BaseMessageType.VALIDATION_ERROR,
                    "Không đủ hàng trong kho. Chỉ còn " + availableQuantity + " sản phẩm có sẵn.");
        }

        if (existingItem != null) {
            existingItem.updateQuantity(totalRequested);
            cartItemRepository.save(existingItem);
        } else {
            CartItem newItem = CartItem.builder()
                    .cart(cart)
                    .product(product)
                    .variant(variant)
                    .title(product.getTitle())
                    .variantTitle(variant.getTitle())
                    .price(variant.getPrice())
                    .linePrice(variant.getPrice().multiply(BigDecimal.valueOf(request.getQuantity())))
                    .priceOriginal(variant.getPrice())
                    .linePriceOriginal(variant.getPrice().multiply(BigDecimal.valueOf(request.getQuantity())))
                    .quantity(request.getQuantity())
                    .sku(variant.getSku())
                    .barcode(variant.getBarcode())
                    .grams(variant.getGrams())
                    .lineWeight(variant.getGrams().multiply(BigDecimal.valueOf(request.getQuantity())))
                    .productType(product.getProductType())
                    .vendor(product.getVendor())
                    .image(getProductImage(product))
                    .handle(product.getHandle())
                    .requiresShipping(true)
                    .giftCard(false)
                    .notAllowPromotion(false)
                    .build();
            if (request.getProperties() != null) {
                newItem.getProperties().putAll(request.getProperties());
            }
            cart.addItem(newItem);
            cartItemRepository.save(newItem);
        }
        cart.recalculateTotals();
        shoppingCartRepository.save(cart);
    }

    private void changeCartItem(ShoppingCart cart, CartChangeRequest request) {
        if (request.getLine() <= 0) throw new BizException(BaseMessageType.VALIDATION_ERROR, "Line number không hợp lệ");
        List<CartItem> items = cart.getItems();
        if (items == null || items.isEmpty())
            throw new BizException(BaseMessageType.VALIDATION_ERROR, "Giỏ hàng trống");

        int idx = request.getLine() - 1;
        if (idx >= items.size())
            throw new BizException(BaseMessageType.VALIDATION_ERROR, "Line number vượt quá số lượng sản phẩm trong giỏ");

        CartItem item = items.get(idx);
        ProductVariant variant = item.getVariant();
        int available = Optional.ofNullable(variant.getInventoryAdvance())
                .map(inv -> inv.getQtyAvailable())
                .orElse(Optional.ofNullable(variant.getInventoryQuantity()).orElse(0));

        if (request.getQuantity() == 0) {
            cart.removeItem(item);
            cartItemRepository.delete(item);
        } else {
            if (request.getQuantity() > available) {
                throw new BizException(BaseMessageType.VALIDATION_ERROR,
                        "Không đủ hàng. Chỉ còn " + available + " sản phẩm.");
            }
            item.updateQuantity(request.getQuantity());
            cartItemRepository.save(item);
        }
        cart.recalculateTotals();
        shoppingCartRepository.save(cart);
    }

    private void clearCart(ShoppingCart cart) {
        cartItemRepository.deleteByCartId(cart.getId());
        cart.getItems().clear();
        cart.recalculateTotals();
        shoppingCartRepository.save(cart);
    }

    private void setCartAttributes(ShoppingCart cart, List<CartAttributeRequest> attributes) {
        log.debug("Adding attributes incrementally to cart {}: {}", cart.getId(), attributes);
        log.debug("Existing attributes before update: {}", cart.getAttributes());
        
        // Add attributes incrementally (preserves existing attributes)
        attributes.forEach(attr -> {
            cart.getAttributes().put(attr.getKey(), attr.getValue());
            log.debug("Added attribute: {} = {}", attr.getKey(), attr.getValue());
        });
        
        log.debug("Final attributes after update: {}", cart.getAttributes());
        shoppingCartRepository.save(cart);
    }

    private void clearCartAttributes(ShoppingCart cart) {
        log.debug("Clearing all attributes for cart {}", cart.getId());
        cart.getAttributes().clear();
        shoppingCartRepository.save(cart);
    }

    private String getProductImage(Product product) {
        return (product.getImages() != null && !product.getImages().isEmpty()) ?
                product.getImages().get(0).getSrc() : null;
    }


}
