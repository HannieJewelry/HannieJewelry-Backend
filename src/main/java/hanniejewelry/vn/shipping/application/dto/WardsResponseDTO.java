package hanniejewelry.vn.shipping.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WardsResponseDTO {
    private List<WardDetailsDTO> wards;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WardDetailsDTO {
        private String name;
        private String code;
        private Integer districtId;
    }
} 