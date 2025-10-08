package hanniejewelry.vn.shared.filter;

import com.blazebit.persistence.view.EntityViewSetting;
import com.blazebit.persistence.FullQueryBuilder;
import hanniejewelry.vn.shared.dto.GenericFilterRequest;

import java.util.Set;

public class GenericBlazeFilterApplier<EV> implements BlazeFilterApplier<EV, GenericFilterRequest> {

    protected Set<String> getAllowedSorts() {
        return Set.of("createdAt", "name", "id");
    }

    protected String getDefaultSort() {
        return "createdAt";
    }

    @Override
    public void applyFilters(EntityViewSetting<EV, ?> setting, GenericFilterRequest filter) {
        filter.getFilters().forEach((key, value) -> {
            if (value != null && !value.isBlank()) {
                int idx = key.lastIndexOf('_');
                if (idx > 0) {
                    String field = key.substring(0, idx);
                    String op = key.substring(idx + 1);
                    setting.addAttributeFilter(field, op, value);
                } else {
                    setting.addAttributeFilter(key, value);
                }
            }
        });
    }

    @Override
    public <T> void applySort(FullQueryBuilder<T, ?> cb, GenericFilterRequest filter) {
        String sortProperty = filter.getSortProperty();
        if (!getAllowedSorts().contains(sortProperty)) sortProperty = getDefaultSort();
        String direction = filter.getDirection();
        if ("ASC".equalsIgnoreCase(direction)) {
            cb.orderByAsc(sortProperty);
            cb.orderByAsc("id");
        } else {
            cb.orderByDesc(sortProperty);
            cb.orderByDesc("id");
        }
    }
}
