package hanniejewelry.vn.customer.usecase.address;

import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.PagedList;
import com.blazebit.persistence.view.EntityViewManager;
import com.blazebit.persistence.view.EntityViewSetting;
import hanniejewelry.vn.customer.entity.Customer;
import hanniejewelry.vn.customer.entity.CustomerAddress;
import hanniejewelry.vn.customer.view.CustomerAddressView;
import hanniejewelry.vn.shared.filter.GenericBlazeFilterApplier;
import hanniejewelry.vn.shared.dto.GenericFilterRequest;
import hanniejewelry.vn.shared.utils.SecurityUtils;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class GetAllCustomerAddressesByCurrentUserUseCase {

    private final EntityViewManager evm;
    private final CriteriaBuilderFactory cbf;
    private final EntityManager em;

    private final GenericBlazeFilterApplier<CustomerAddressView> filterApplier = new GenericBlazeFilterApplier<>() {};

    public PagedList<CustomerAddressView> execute(GenericFilterRequest filter) {
        Customer customer = SecurityUtils.getCurrentCustomer();
        
        var setting = EntityViewSetting.create(CustomerAddressView.class, filter.getPage(), filter.getSize());
        filterApplier.applyFilters(setting, filter);

        var cb = cbf.create(em, CustomerAddress.class)
                .where("customer.id").eq(customer.getId());
        filterApplier.applySort(cb, filter);

        return evm.applySetting(setting, cb).getResultList();
    }
} 