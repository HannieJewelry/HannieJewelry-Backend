package hanniejewelry.vn.shared.filter;

import com.blazebit.persistence.FullQueryBuilder;
import com.blazebit.persistence.view.EntityViewSetting;

public interface BlazeFilterApplier<EV, FILTER> {
    void applyFilters(EntityViewSetting<EV, ?> setting, FILTER filter);
    <T> void applySort(FullQueryBuilder<T, ?> cb, FILTER filter);
}