package hanniejewelry.vn.shipping.application.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;


@Data
public class DistrictDTO {
    

    private Integer id;

    private String name;

    private String code;

    @JsonAlias({"province_id"})
    private Integer provinceId;
} 