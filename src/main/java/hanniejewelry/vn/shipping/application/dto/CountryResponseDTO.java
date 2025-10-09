package hanniejewelry.vn.shipping.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CountryResponseDTO {
    private CountryWithProvincesDTO country;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CountryWithProvincesDTO {
        private String code;
        private Integer id;
        private String name;
        private List<ProvinceDetailsDTO> provinces;

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProvinceDetailsDTO {
        private String code;
        private Integer countryId;
        private Integer id;
        private String name;

    }
} 