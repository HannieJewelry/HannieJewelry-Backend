package hanniejewelry.vn.customer.usecase.segment;

import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.PagedList;
import com.blazebit.persistence.view.EntityViewManager;
import com.blazebit.persistence.view.EntityViewSetting;
import hanniejewelry.vn.customer.entity.CustomerSegment;
import hanniejewelry.vn.customer.view.CustomerSegmentView;
import hanniejewelry.vn.shared.filter.GenericBlazeFilterApplier;
import hanniejewelry.vn.shared.dto.GenericFilterRequest;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetAllSegmentsWithBlazeFilterUseCase {
    private final EntityViewManager evm;
    private final CriteriaBuilderFactory cbf;
    private final EntityManager em;

    private final GenericBlazeFilterApplier<CustomerSegmentView> filterApplier = new GenericBlazeFilterApplier<>() {};

    public PagedList<CustomerSegmentView> execute(GenericFilterRequest filter) {
        var setting = EntityViewSetting.create(CustomerSegmentView.class, filter.getPage(), filter.getSize());
        filterApplier.applyFilters(setting, filter);
        var cb = cbf.create(em, CustomerSegment.class);
        filterApplier.applySort(cb, filter);
        return evm.applySetting(setting, cb).getResultList();
    }
} 