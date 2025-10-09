package hanniejewelry.vn.user.dto;

import lombok.Builder;
import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
@Builder
public class UserInfoResponse {
    private Map<String, Object> user;
    private boolean isProduction;
    private String authority;
    private String loginUrl;
    private String domainHost;
    private String basePath;
    private boolean isWebView;
    private boolean isNewbie;
    private List<String> scopes;
    private boolean isAdmin;
}
