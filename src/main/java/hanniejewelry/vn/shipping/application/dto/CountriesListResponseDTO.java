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
public class CountriesListResponseDTO {
    private List<CountrySimpleDTO> countries;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CountrySimpleDTO {
        private Integer id;
        private String code;
        private String name;
    }
} 