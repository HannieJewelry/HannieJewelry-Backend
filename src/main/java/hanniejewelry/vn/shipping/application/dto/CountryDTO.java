package hanniejewelry.vn.shipping.application.dto;

import lombok.Data;

import java.util.List;

/**
 * Unified Country DTO that handles Haravan API format
 */
@Data
public class CountryDTO {
    
    private Integer id;
    
    private String code;
    
    private String name;
    
    private List<ProvinceDTO> provinces;
}