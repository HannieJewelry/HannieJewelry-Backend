package hanniejewelry.vn.product.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
public class ProductRequest {
    @NotBlank
    private String title;
    
    @JsonProperty("body_html")
    private String bodyHtml;
    
    @JsonProperty("body_plain")
    private String bodyPlain;
    
    private String vendor;
    
    @JsonProperty("product_type")
    private String productType;
    
    private String tags;
    
    @JsonProperty("template_suffix")
    private String templateSuffix;
    
    @JsonProperty("published_at")
    private Instant publishedAt;
    
    @JsonProperty("published_scope")
    private String publishedScope;
    
    private List<@Valid ProductVariantRequest> variants;
    
    private List<@Valid ProductOptionRequest> options;
    
    @JsonProperty("only_hide_from_list")
    private boolean onlyHideFromList;
    
    @JsonProperty("not_allow_promotion")
    private boolean notAllowPromotion;
    
    @JsonProperty("collection_model")
    private List<CollectionModel> collectionModel;
    
    @JsonProperty("collection_ids")
    private String collectionIds;
}
