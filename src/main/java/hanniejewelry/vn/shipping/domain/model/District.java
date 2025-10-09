package hanniejewelry.vn.shipping.domain.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class District {
    private Integer id;
    private Integer provinceId;
    private String name;
    private String code;
}
