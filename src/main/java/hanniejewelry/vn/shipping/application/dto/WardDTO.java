package hanniejewelry.vn.shipping.application.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

@Data
public class WardDTO {
    

    private String code;

    private String name;

    @JsonAlias({"district_id"})
    private Integer districtId;

    private Integer id;
} 