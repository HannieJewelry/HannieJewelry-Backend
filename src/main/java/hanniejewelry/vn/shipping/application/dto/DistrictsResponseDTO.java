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
public class DistrictsResponseDTO {
    private List<DistrictDetailsDTO> districts;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DistrictDetailsDTO {
        private Integer id;
        private String name;
        private String code;
        private Integer provinceId;
    }
} 