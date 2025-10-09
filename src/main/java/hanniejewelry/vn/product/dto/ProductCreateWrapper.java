// ProductCreateWrapper.java
package hanniejewelry.vn.product.dto;

import jakarta.validation.Valid;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductCreateWrapper {
    @Valid
    private ProductRequest product;
}
