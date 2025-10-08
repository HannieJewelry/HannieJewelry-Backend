package hanniejewelry.vn.inventory.api;

import hanniejewelry.vn.inventory.dto.LocationRequest;
import hanniejewelry.vn.inventory.dto.SupplierRequest;
import hanniejewelry.vn.inventory.view.InventorySummaryView;
import hanniejewelry.vn.inventory.view.LocationView;
import hanniejewelry.vn.inventory.view.ProductInventoryView;
import hanniejewelry.vn.inventory.view.SupplierView;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;


public interface InventoryFacade {

    // Location operations
    LocationView getLocationById(UUID id);
    List<LocationView> getShippingLocations();
    LocationView createLocation(LocationRequest request);
    LocationView updateLocation(UUID id, LocationRequest request);
    void deleteLocation(UUID id);
    LocationView setPrimaryLocation(UUID id);
    
    // Supplier operations
    SupplierView getSupplierById(UUID id);
    SupplierView createSupplier(SupplierRequest request);
    SupplierView updateSupplier(UUID id, SupplierRequest request);
    void deleteSupplier(UUID id);
    SupplierView updateSupplierDebt(UUID id, BigDecimal debt);
    SupplierView updateSupplierTotalPurchase(UUID id, BigDecimal totalPurchase);
    
    // Inventory operations
    ProductInventoryView getProductInventoryByVariantId(Long variantId);
    List<ProductInventoryView> getProductInventoryByProductId(Long productId);
    InventorySummaryView getInventorySummary();
} 