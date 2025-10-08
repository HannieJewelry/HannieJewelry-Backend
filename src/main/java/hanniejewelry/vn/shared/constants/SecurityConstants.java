package hanniejewelry.vn.shared.constants;

import java.util.Set;

public class SecurityConstants {
    public static final String[] AUTH_WHITELIST = {
            ApiConstants.API_PREFIX + "/auth/**",
            ApiConstants.API_PREFIX + "/users/accept-invite",
            ApiConstants.API_PREFIX + "/orders/**",
            ApiConstants.API_PREFIX + "/orders",
            "/webhook/payment",
            ApiConstants.API_PREFIX + "/custom_collections/**",
            ApiConstants.API_PREFIX + "/client/collections/**",
//            ApiConstants.API_PREFIX + "/client/checkout/**",
            ApiConstants.API_PREFIX + "/client/checkout/complete",
            ApiConstants.API_PREFIX + "/client/cart/**",
            // Address API endpoints
            ApiConstants.API_PREFIX + "/countries/**",
            ApiConstants.API_PREFIX + "/countries",
            ApiConstants.API_PREFIX + "/districts",
            ApiConstants.API_PREFIX + "/wards",
            ApiConstants.API_PREFIX + "/address/**",
            ApiConstants.API_PREFIX + "/customer/addresses/**",
            // OpenAPI endpoints
            "/v3/api-docs/**",
            "/v3/api-docs.yaml",
            "/swagger-ui.html",
            "/api/vault/raw",
            "/test/sepay-key",
            "/swagger-ui/**",
            "/demo-api-key"
    };

    public static final Set<String> VALID_PERMISSIONS = Set.of(
            "com_api.product.read",
            "com_api.product.write",
            "com_api.product.delete",
            "com_api.product.export"
    );
}
