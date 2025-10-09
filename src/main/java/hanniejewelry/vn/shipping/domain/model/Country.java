package hanniejewelry.vn.shipping.domain.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Country {
    private Integer id;
    private String code;
    private String name;
} 