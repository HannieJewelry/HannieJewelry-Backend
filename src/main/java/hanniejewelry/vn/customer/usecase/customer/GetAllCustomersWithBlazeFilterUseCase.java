package hanniejewelry.vn.customer.usecase.customer;

import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.PagedList;
import com.blazebit.persistence.view.EntityViewManager;
import com.blazebit.persistence.view.EntityViewSetting;
import hanniejewelry.vn.customer.entity.Customer;
import hanniejewelry.vn.customer.view.CustomerView;
import hanniejewelry.vn.shared.filter.GenericBlazeFilterApplier;

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

    public PagedList<CustomerView> execute(hanniejewelry.vn.customer.dto.CustomerSearchRequest filter) {
        var setting = EntityViewSetting.create(CustomerView.class, filter.getPage(), filter.getSize());
        filterApplier.applyFilters(setting, filter);

        var cb = cbf.create(em, Customer.class);

        // Custom filter: khách hàng có đơn chưa hoàn tất trong 7 hoặc 30 ngày
        boolean filter7 = filter.isHasUnfinishedOrderIn7Days();
        boolean filter30 = filter.isHasUnfinishedOrderIn30Days();
        if (filter7 || filter30) {
            int days = filter7 ? 7 : 30;
            var now = java.time.Instant.now();
            var from = now.minus(java.time.Duration.ofDays(days));
                        cb.innerJoin("orders", "o");
                        cb.where("o.createdAt").ge(from)
                            .where("o.createdAt").le(now)
                            .where("o.fulfillmentStatus").notEq(hanniejewelry.vn.order.domain.model.enums.FulfillmentStatus.FULFILLED);
        }

        filterApplier.applySort(cb, filter);
        return evm.applySetting(setting, cb).getResultList();
    }
}
