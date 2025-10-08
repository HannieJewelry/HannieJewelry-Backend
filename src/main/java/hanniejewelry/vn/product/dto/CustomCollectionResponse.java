package hanniejewelry.vn.product.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CustomCollectionResponse {
    private UUID id;
    private String bodyHtml;
    private Instant createdAt;
    private String handle;
    private ImageResponse image;
    private Integer itemsCount;
    private Boolean published;
    private Instant publishedAt;
    private String publishedScope;
    private String sortOrder;
    private String templateSuffix;
    private String title;
    private Instant updatedAt;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class ImageResponse {
        private Instant createdAt;
        private String src;
    }
} 