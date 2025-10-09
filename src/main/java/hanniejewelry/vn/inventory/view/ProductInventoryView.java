package hanniejewelry.vn.inventory.view;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;
import com.blazebit.persistence.view.Mapping;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import hanniejewelry.vn.product.entity.Product;
import hanniejewelry.vn.product.entity.ProductVariant;

import java.math.BigDecimal;
import java.util.UUID;

@EntityView(ProductVariant.class)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public interface ProductInventoryView {

    @IdMapping
    Long getId();
    
    String getSku();
    
    String getBarcode();
    
    String getTitle();
    
    BigDecimal getPrice();
    
    BigDecimal getCompareAtPrice();
    
    @Mapping("inventoryAdvance.qtyAvailable")
    Integer getQtyAvailable();
    
    @Mapping("inventoryAdvance.qtyOnhand")
    Integer getQtyOnhand();
    
    @Mapping("inventoryAdvance.qtyCommited")
    Integer getQtyCommited();
    
    @Mapping("inventoryAdvance.qtyIncoming")
    Integer getQtyIncoming();
    
    @Mapping("product")
    ProductView getProduct();
    
    @EntityView(Product.class)
    interface ProductView {
        
        @IdMapping
        UUID getId();
        
        String getTitle();
        
        String getVendor();
        
        String getProductType();
    }
} 