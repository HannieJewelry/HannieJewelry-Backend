package hanniejewelry.vn.shipping.domain.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ward {
    private String code;
    private Integer districtId;
    private String name;
}
