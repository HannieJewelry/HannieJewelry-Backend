package hanniejewelry.vn.inventory.api;

import hanniejewelry.vn.inventory.dto.LocationRequest;
import hanniejewelry.vn.inventory.dto.SupplierRequest;
import hanniejewelry.vn.inventory.service.InventoryService;
import hanniejewelry.vn.inventory.service.LocationService;
import hanniejewelry.vn.inventory.service.SupplierService;
import hanniejewelry.vn.inventory.view.InventorySummaryView;
import hanniejewelry.vn.inventory.view.LocationView;
import hanniejewelry.vn.inventory.view.ProductInventoryView;
import hanniejewelry.vn.inventory.view.SupplierView;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InventoryFacadeImpl implements InventoryFacade {

    private final LocationService locationService;
    private final SupplierService supplierService;
    private final InventoryService inventoryService;

    // Location operations
    @Override
    public LocationView getLocationById(UUID id) {
        return locationService.getLocationViewById(id);
    }

    @Override
    public List<LocationView> getShippingLocations() {
        return locationService.getShippingLocations();
    }

    @Override
    public LocationView createLocation(LocationRequest request) {
        return locationService.createLocationAndReturnView(request);
    }

    @Override
    public LocationView updateLocation(UUID id, LocationRequest request) {
        return locationService.updateLocationAndReturnView(id, request);
    }

    @Override
    public void deleteLocation(UUID id) {
        locationService.deleteLocation(id);
    }

    @Override
    public LocationView setPrimaryLocation(UUID id) {
        return locationService.setPrimaryLocation(id);
    }

    // Supplier operations
    @Override
    public SupplierView getSupplierById(UUID id) {
        return supplierService.getSupplierViewById(id);
    }

    @Override
    public SupplierView createSupplier(SupplierRequest request) {
        return supplierService.createSupplierAndReturnView(request);
    }

    @Override
    public SupplierView updateSupplier(UUID id, SupplierRequest request) {
        return supplierService.updateSupplierAndReturnView(id, request);
    }

    @Override
    public void deleteSupplier(UUID id) {
        supplierService.deleteSupplier(id);
    }

    @Override
    public SupplierView updateSupplierDebt(UUID id, BigDecimal debt) {
        return supplierService.updateSupplierDebt(id, debt);
    }

    @Override
    public SupplierView updateSupplierTotalPurchase(UUID id, BigDecimal totalPurchase) {
        return supplierService.updateSupplierTotalPurchase(id, totalPurchase);
    }

    // Inventory operations
    @Override
    public ProductInventoryView getProductInventoryByVariantId(Long variantId) {
        return inventoryService.getProductInventoryByVariantId(variantId);
    }

    @Override
    public List<ProductInventoryView> getProductInventoryByProductId(Long productId) {
        return inventoryService.getProductInventoryByProductId(productId);
    }

    @Override
    public InventorySummaryView getInventorySummary() {
        return inventoryService.getInventorySummary();
    }
} 