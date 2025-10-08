package hanniejewelry.vn.shared.security;


import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;

@Component
public class ApiKeyAuthFilter extends GenericFilter {

    @Value("${sepay.api-key}")
    private String sepayApiKey;

    private static final String AUTH_HEADER = "Authorization";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String path = req.getRequestURI();
        if (path.startsWith("/sepay/sepay_webhook")) {
            String authHeader = req.getHeader(AUTH_HEADER);
            if (!StringUtils.hasText(authHeader) || !authHeader.equals("Apikey " + sepayApiKey)) {
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                resp.setContentType("application/json");
                resp.getWriter().write("{\"success\": false, \"message\": \"Invalid API Key\"}");
                return;
            }
        }
        chain.doFilter(request, response);
    }
}
