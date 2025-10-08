package hanniejewelry.vn.product.view;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;
import hanniejewelry.vn.product.entity.Vendor;

import java.util.UUID;

@EntityView(Vendor.class)
public interface VendorView {
    @IdMapping
    UUID getId();
    String getName();
}
