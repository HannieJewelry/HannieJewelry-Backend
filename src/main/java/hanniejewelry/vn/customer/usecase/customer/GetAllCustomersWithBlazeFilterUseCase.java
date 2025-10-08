package hanniejewelry.vn.customer.usecase.customer;

import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.PagedList;
import com.blazebit.persistence.view.EntityViewManager;
import com.blazebit.persistence.view.EntityViewSetting;
import hanniejewelry.vn.customer.entity.Customer;
import hanniejewelry.vn.customer.view.CustomerView;
import hanniejewelry.vn.shared.filter.GenericBlazeFilterApplier;
import hanniejewelry.vn.shared.dto.GenericFilterRequest;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class GetAllCustomersWithBlazeFilterUseCase {
    private final EntityViewManager evm;
    private final CriteriaBuilderFactory cbf;
    private final EntityManager em;

    private final GenericBlazeFilterApplier<CustomerView> filterApplier = new GenericBlazeFilterApplier<>() {
        @Override
        protected Set<String> getAllowedSorts() {
            return Set.of("createdAt", "updatedAt", "firstName", "lastName", "email");
        }

        @Override
        protected String getDefaultSort() {
            return "createdAt";
        }
    };

    public PagedList<CustomerView> execute(GenericFilterRequest filter) {
        var setting = EntityViewSetting.create(CustomerView.class, filter.getPage(), filter.getSize());
        filterApplier.applyFilters(setting, filter);
        
        var cb = cbf.create(em, Customer.class);
        filterApplier.applySort(cb, filter);
        
        return evm.applySetting(setting, cb).getResultList();
    }
}
