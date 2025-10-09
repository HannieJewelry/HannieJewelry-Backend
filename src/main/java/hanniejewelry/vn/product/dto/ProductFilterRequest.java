package hanniejewelry.vn.product.dto;

import hanniejewelry.vn.shared.dto.GenericFilterRequest;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ProductFilterRequest extends GenericFilterRequest {
    private String filter;
}
