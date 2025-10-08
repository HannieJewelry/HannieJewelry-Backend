package hanniejewelry.vn.shipping.application.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;


@Data
public class ProvinceDTO {

    @JsonAlias({"province_id"})
    private Integer id;

    private String name;

    private String code;

    @JsonAlias({"country_id"})
    private Integer countryId;

}