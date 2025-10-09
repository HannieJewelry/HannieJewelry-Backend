package hanniejewelry.vn.product.dto;

import lombok.Data;

import java.util.Set;


@Data
public class UploadByUrlRequest {
    private ImageInfo image;

    @Data
    public static class ImageInfo {
        private Set<Long> variant_ids;
        private String src;
    }
}
