package hanniejewelry.vn.inventory.service;

import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.PagedList;
import com.blazebit.persistence.view.EntityViewManager;
import com.blazebit.persistence.view.EntityViewSetting;
import hanniejewelry.vn.inventory.view.InventorySummaryView;
import hanniejewelry.vn.inventory.view.ProductInventoryView;
import hanniejewelry.vn.product.entity.ProductVariant;
import hanniejewelry.vn.product.repository.ProductVariantRepository;
import hanniejewelry.vn.shared.exception.BaseMessageType;
import hanniejewelry.vn.shared.exception.BizException;
import hanniejewelry.vn.shared.filter.GenericBlazeFilterApplier;
import hanniejewelry.vn.shared.dto.GenericFilterRequest;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class InventoryService {

    EntityViewManager evm;
    ProductVariantRepository productVariantRepository;
    CriteriaBuilderFactory cbf;
    EntityManager em;
    private final GenericBlazeFilterApplier<ProductInventoryView> filterApplier = new GenericBlazeFilterApplier<>();


    public PagedList<ProductInventoryView> getAllProductInventoryWithBlazeFilter(GenericFilterRequest filter) {
        var setting = EntityViewSetting.create(ProductInventoryView.class, filter.getPage(), filter.getSize());
        filterApplier.applyFilters(setting, filter);

        var cb = cbf.create(em, ProductVariant.class);
        filterApplier.applySort(cb, filter);

        return evm.applySetting(setting, cb).getResultList();
    }


    public ProductInventoryView getProductInventoryByVariantId(Long variantId) {
        productVariantRepository.findById(variantId)
                .orElseThrow(() -> new BizException(BaseMessageType.NOT_FOUND, "Product variant not found with id: " + variantId));
        return evm.find(em, ProductInventoryView.class, variantId);
    }


    public List<ProductInventoryView> getProductInventoryByProductId(Long productId) {
        var setting = EntityViewSetting.create(ProductInventoryView.class);
        var cb = cbf.create(em, ProductVariant.class);
        cb.where("product.id").eq(productId);
        
        return evm.applySetting(setting, cb).getResultList();
    }

    public InventorySummaryView getInventorySummary() {
        Query totalAmountQuery = em.createNativeQuery(
            "SELECT COALESCE(SUM(pv.cost_price * COALESCE(pv.inventory_quantity, 0)), 0) " +
            "FROM product_variant pv " +
            "WHERE pv.is_deleted = false"
        );
        
        Query totalOnHandQuery = em.createNativeQuery(
            "SELECT COALESCE(SUM(COALESCE(pv.inventory_quantity, 0)), 0) " +
            "FROM product_variant pv " +
            "WHERE pv.is_deleted = false"
        );
        
        Query totalPriceQuery = em.createNativeQuery(
            "SELECT COALESCE(SUM(pv.price * COALESCE(pv.inventory_quantity, 0)), 0) " +
            "FROM product_variant pv " +
            "WHERE pv.is_deleted = false"
        );
        
        final BigDecimal totalAmount = (BigDecimal) totalAmountQuery.getSingleResult();
        final Integer totalOnHand = ((Number) totalOnHandQuery.getSingleResult()).intValue();
        final BigDecimal totalPrice = (BigDecimal) totalPriceQuery.getSingleResult();
        final BigDecimal totalProfit = totalPrice.subtract(totalAmount);
        
        log.info("Inventory Summary - totalAmount: {}, totalOnHand: {}, totalPrice: {}, totalProfit: {}",
                totalAmount, totalOnHand, totalPrice, totalProfit);
        
        return new InventorySummaryView() {
            @Override
            public BigDecimal getTotalAmount() {
                return totalAmount;
            }

            @Override
            public Integer getTotalOnHand() {
                return totalOnHand;
            }

            @Override
            public BigDecimal getTotalPrice() {
                return totalPrice;
            }

            @Override
            public BigDecimal getTotalProfit() {
                return totalProfit;
            }
        };
    }
} 