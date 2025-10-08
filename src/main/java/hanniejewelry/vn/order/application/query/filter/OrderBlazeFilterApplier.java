package hanniejewelry.vn.order.application.query.filter;

import com.blazebit.persistence.view.EntityViewSetting;
import hanniejewelry.vn.order.application.query.view.OrderView;
import hanniejewelry.vn.shared.filter.GenericBlazeFilterApplier;
import hanniejewelry.vn.shared.dto.GenericFilterRequest;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class OrderBlazeFilterApplier extends GenericBlazeFilterApplier<OrderView> {
    
    @Override
    protected Set<String> getAllowedSorts() {
        return Set.of("createdAt", "id", "totalPrice", "customerName", "status", "updatedAt");
    }
    
    @Override
    protected String getDefaultSort() {
        return "createdAt";
    }
    
    @Override
    public void applyFilters(EntityViewSetting<OrderView, ?> setting, GenericFilterRequest filter) {
        super.applyFilters(setting, filter);
    }
} 