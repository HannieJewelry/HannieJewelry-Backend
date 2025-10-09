package hanniejewelry.vn.shared.config;

import com.blazebit.persistence.Criteria;
import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.spi.CriteriaBuilderConfiguration;
import com.blazebit.persistence.view.EntityViewManager;
import com.blazebit.persistence.view.EntityViews;
import com.blazebit.persistence.view.spi.EntityViewConfiguration;
import hanniejewelry.vn.inventory.view.LocationView;
import hanniejewelry.vn.inventory.view.ProductInventoryView;
import hanniejewelry.vn.inventory.view.SupplierView;
import hanniejewelry.vn.order.application.query.view.*;
import hanniejewelry.vn.product.view.*;
import hanniejewelry.vn.customer.view.*;
import hanniejewelry.vn.employee.view.*;
import hanniejewelry.vn.shopping_cart.view.ShoppingCartView;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceUnit;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BlazePersistenceConfiguration {
  @PersistenceUnit
  private EntityManagerFactory entityManagerFactory;

  @Bean
  public CriteriaBuilderFactory createCriteriaBuilderFactory() {
    final CriteriaBuilderConfiguration config = Criteria.getDefault();
    return config.createCriteriaBuilderFactory(entityManagerFactory);
  }

  @Bean
  public EntityViewManager entityViewManager(final CriteriaBuilderFactory cbf) {
    final EntityViewConfiguration config = EntityViews.createDefaultConfiguration();
    config.addEntityView(ProductView.class);
    config.addEntityView(ProductVariantView.class);
    config.addEntityView(ProductOptionView.class);
    config.addEntityView(ProductImageView.class);
    config.addEntityView(InventoryAdvanceView.class);
    config.addEntityView(VendorView.class);

    // Register new views for collections
    config.addEntityView(CollectView.class);
    config.addEntityView(CustomCollectionSlimView.class);
    
    // Register ProductClientView and its nested views
    
    config.addEntityView(CustomCollectionView.class);
    config.addEntityView(CustomCollectionView.CollectionImageView.class);

    config.addEntityView(CustomerView.class);
    config.addEntityView(CustomerAddressView.class);
    config.addEntityView(CustomerSegmentView.class);
    config.addEntityView(EmployeeView.class);
    config.addEntityView(EmployeeView.UserView.class);
    config.addEntityView(EInvoiceInfoView.class);

    config.addEntityView(OrderBillingAddressView.class);
    config.addEntityView(OrderDiscountCodeView.class);
    config.addEntityView(OrderLineItemView.class);
    config.addEntityView(OrderNoteAttributeView.class);
    config.addEntityView(OrderShippingAddressView.class);
    config.addEntityView(OrderShippingLineView.class);
    config.addEntityView(OrderTransactionView.class);
    config.addEntityView(OrderView.class);
    config.addEntityView(EmployeeGroupView.class);
    config.addEntityView(EmployeeInGroupView.class);

    // Register ShoppingCart views
    config.addEntityView(ShoppingCartView.CartItemView.class); // Add the nested CartItemView interface
    config.addEntityView(ShoppingCartView.class);
    
    // Register Inventory views
    config.addEntityView(LocationView.class);
    config.addEntityView(SupplierView.class);
    config.addEntityView(ProductInventoryView.class);
    config.addEntityView(ProductInventoryView.ProductView.class);
    
    return config.createEntityViewManager(cbf);
  }
}
