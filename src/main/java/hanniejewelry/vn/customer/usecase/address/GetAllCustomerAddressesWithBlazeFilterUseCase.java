package hanniejewelry.vn.customer.usecase.address;

import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.PagedList;
import com.blazebit.persistence.view.EntityViewManager;
import com.blazebit.persistence.view.EntityViewSetting;
import hanniejewelry.vn.customer.entity.CustomerAddress;
import hanniejewelry.vn.customer.repository.CustomerRepository;
import hanniejewelry.vn.customer.view.CustomerAddressView;
import hanniejewelry.vn.shared.exception.BaseMessageType;
import hanniejewelry.vn.shared.exception.BizException;
import hanniejewelry.vn.shared.filter.GenericBlazeFilterApplier;
import hanniejewelry.vn.shared.dto.GenericFilterRequest;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetAllCustomerAddressesWithBlazeFilterUseCase {

    private final EntityViewManager evm;
    private final CriteriaBuilderFactory cbf;
    private final EntityManager em;
    private final CustomerRepository customerRepository;

    private final GenericBlazeFilterApplier<CustomerAddressView> filterApplier = new GenericBlazeFilterApplier<>() {};

    public PagedList<CustomerAddressView> execute(UUID customerId, GenericFilterRequest filter) {
        if (!customerRepository.existsById(customerId)) {
            throw new BizException(BaseMessageType.NOT_FOUND, "Customer not found with id: {0}", customerId);
        }

        var setting = EntityViewSetting.create(CustomerAddressView.class, filter.getPage(), filter.getSize());
        filterApplier.applyFilters(setting, filter);

        var cb = cbf.create(em, CustomerAddress.class)
                .where("customer.id").eq(customerId);
        filterApplier.applySort(cb, filter);

        return evm.applySetting(setting, cb).getResultList();
    }
} 