package hanniejewelry.vn.shipping.domain.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Province {
    private Integer id;
    private String name;
    private String code;
    private Integer countryId;
}
