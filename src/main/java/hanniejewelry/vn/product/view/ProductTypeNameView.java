package hanniejewelry.vn.product.view;


import com.blazebit.persistence.view.EntityView;
import hanniejewelry.vn.product.entity.Product;

@EntityView(Product.class)
public interface ProductTypeNameView {
    String getProductType();
}