package hanniejewelry.vn.shopping_cart.controller;

import hanniejewelry.vn.shared.constants.ApiConstants;
import hanniejewelry.vn.shared.exception.BaseMessageType;
import hanniejewelry.vn.shared.exception.BizException;
import hanniejewelry.vn.shared.dto.RestResponse;
import hanniejewelry.vn.shared.utils.RestResponseUtils;
import hanniejewelry.vn.shared.utils.SecurityUtils;
import hanniejewelry.vn.shopping_cart.dto.CartAttributeRequest;
import hanniejewelry.vn.shopping_cart.dto.CartChangeRequest;
import hanniejewelry.vn.shopping_cart.dto.CartItemRequest;
import hanniejewelry.vn.shopping_cart.dto.ShoppingCartDTO;
import hanniejewelry.vn.shopping_cart.service.ShoppingCartService;
import hanniejewelry.vn.shopping_cart.view.ShoppingCartView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping(ApiConstants.API_PREFIX + "/client/cart")
@RequiredArgsConstructor
public class ShoppingCartClientController {

    private final ShoppingCartService shoppingCartService;
    private final CartTokenUtils cartTokenUtils;

    /** Lấy giỏ hàng của user hoặc anonymous */
    @GetMapping
    public ResponseEntity<RestResponse<ShoppingCartDTO>> getCart(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        ShoppingCartView cartView;
        if (SecurityUtils.isAuthenticated()) {
            cartView = shoppingCartService.getCurrentCustomerCart();
        } else {
            String cartToken = cartTokenUtils.getOrCreateCartToken(request, response);
            cartView = shoppingCartService.getAnonymousCart(cartToken);
        }
        return RestResponseUtils.successResponse(ShoppingCartDTO.fromShoppingCartView(cartView));
    }

    /** Thêm sản phẩm vào giỏ */
    @PostMapping("/add")
    public ResponseEntity<RestResponse<ShoppingCartDTO>> addItem(
            @Valid @RequestBody CartItemRequest request,
            HttpServletRequest servletRequest,
            HttpServletResponse response
    ) {
        ShoppingCartView cartView;
        if (SecurityUtils.isAuthenticated()) {
            log.info("Đã đăng nhập: UserId={} - Thêm sản phẩm: {}", SecurityUtils.getCurrentCustomer().getId(), request);
            cartView = shoppingCartService.addItemToCurrentCustomerCart(request);
        } else {
            String cartToken = cartTokenUtils.getOrCreateCartToken(servletRequest, response);
            log.info("Chưa đăng nhập: cart_token={} - Thêm sản phẩm: {}", cartToken, request);
            cartView = shoppingCartService.addItemToAnonymousCart(cartToken, request);
        }
        return RestResponseUtils.successResponse(ShoppingCartDTO.fromShoppingCartView(cartView));
    }

    /** Sửa số lượng/sp trong giỏ */
    @PostMapping("/change")
    public ResponseEntity<RestResponse<ShoppingCartDTO>> changeItem(
            @Valid @RequestBody CartChangeRequest request,
            HttpServletRequest servletRequest,
            HttpServletResponse response
    ) {
        ShoppingCartView cartView;
        if (SecurityUtils.isAuthenticated()) {
            cartView = shoppingCartService.changeItemInCurrentCustomerCart(request);
        } else {
            String cartToken = cartTokenUtils.getOrCreateCartToken(servletRequest, response);
            cartView = shoppingCartService.changeItemInAnonymousCart(cartToken, request);
        }
        return RestResponseUtils.successResponse(ShoppingCartDTO.fromShoppingCartView(cartView));
    }

    /** Xoá hết giỏ */
    @PostMapping("/clear")
    public ResponseEntity<RestResponse<ShoppingCartDTO>> clearCart(
            HttpServletRequest servletRequest,
            HttpServletResponse response
    ) {
        ShoppingCartView cartView;
        if (SecurityUtils.isAuthenticated()) {
            cartView = shoppingCartService.clearCurrentCustomerCart();
        } else {
            // Không tạo mới, chỉ lấy token hiện có!
            String cartToken = cartTokenUtils.getCartToken(servletRequest);
            if (cartToken == null || cartToken.trim().isEmpty()) {
                throw new BizException(BaseMessageType.NOT_FOUND, "Không tìm thấy cart_token để clear giỏ hàng");
            }
            cartView = shoppingCartService.clearAnonymousCart(cartToken);
        }
        return RestResponseUtils.successResponse(ShoppingCartDTO.fromShoppingCartView(cartView));
    }

    /** Ghi chú cho giỏ hàng */
    @PutMapping("/note")
    public ResponseEntity<RestResponse<ShoppingCartDTO>> updateCartNote(
            @RequestBody String note,
            HttpServletRequest servletRequest,
            HttpServletResponse response
    ) {
        ShoppingCartView cartView;
        if (SecurityUtils.isAuthenticated()) {
            cartView = shoppingCartService.updateCartNoteForCurrentCustomer(note);
        } else {
            String cartToken = cartTokenUtils.getOrCreateCartToken(servletRequest, response);
            cartView = shoppingCartService.updateCartNoteForAnonymousCart(cartToken, note);
        }
        return RestResponseUtils.successResponse(ShoppingCartDTO.fromShoppingCartView(cartView));
    }

    /** Update thuộc tính mở rộng cho giỏ hàng */
    @PutMapping("/attributes")
    public ResponseEntity<RestResponse<ShoppingCartDTO>> updateCartAttributes(
            @Valid @RequestBody List<CartAttributeRequest> attributes,
            HttpServletRequest servletRequest,
            HttpServletResponse response
    ) {
        ShoppingCartView cartView;
        if (SecurityUtils.isAuthenticated()) {
            cartView = shoppingCartService.updateCartAttributesForCurrentCustomer(attributes);
        } else {
            String cartToken = cartTokenUtils.getOrCreateCartToken(servletRequest, response);
            cartView = shoppingCartService.updateCartAttributesForAnonymousCart(cartToken, attributes);
        }
        return RestResponseUtils.successResponse(ShoppingCartDTO.fromShoppingCartView(cartView));
    }
}
