package hanniejewelry.vn.customer.usecase.address;

import com.blazebit.persistence.view.EntityViewManager;
import hanniejewelry.vn.customer.entity.Customer;
import hanniejewelry.vn.customer.entity.CustomerAddress;
import hanniejewelry.vn.customer.repository.CustomerAddressRepository;
import hanniejewelry.vn.customer.view.CustomerAddressView;
import hanniejewelry.vn.shared.exception.BaseMessageType;
import hanniejewelry.vn.shared.exception.BizException;
import hanniejewelry.vn.shared.utils.SecurityUtils;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetCustomerAddressByIdForCurrentUserUseCase {

    private final EntityViewManager evm;
    private final EntityManager em;
    private final CustomerAddressRepository customerAddressRepository;

    public CustomerAddressView execute(UUID addressId) {
        Customer customer = SecurityUtils.getCurrentCustomer();

        CustomerAddress address = customerAddressRepository.findById(addressId)
                .orElseThrow(() -> new BizException(BaseMessageType.NOT_FOUND, "Address not found with ID: {0}", addressId));

        if (!address.getCustomer().getId().equals(customer.getId())) {
            throw new BizException(BaseMessageType.FORBIDDEN, "You do not have permission to access this address");
        }

        return evm.find(em, CustomerAddressView.class, address.getId());
    }
}