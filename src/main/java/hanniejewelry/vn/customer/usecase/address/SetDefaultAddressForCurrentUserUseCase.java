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
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class SetDefaultAddressForCurrentUserUseCase {

    private final EntityViewManager evm;
    private final EntityManager em;
    private final CustomerAddressRepository customerAddressRepository;

    public CustomerAddressView execute(UUID addressId) {
        Customer customer = SecurityUtils.getCurrentCustomer();

        CustomerAddress address = customerAddressRepository.findById(addressId)
                .orElseThrow(() -> new BizException(BaseMessageType.NOT_FOUND, "Address not found with ID: {0}", addressId));

        if (!address.getCustomer().getId().equals(customer.getId())) {
            throw new BizException(BaseMessageType.FORBIDDEN, "You do not have permission to update this address");
        }

        customerAddressRepository.findByCustomerIdAndIsDefaultTrue(customer.getId())
                .ifPresent(defaultAddress -> customerAddressRepository.save(
                        defaultAddress.toBuilder().isDefault(false).build()));

        CustomerAddress saved = customerAddressRepository.save(
                address.toBuilder().isDefault(true).build());

        customerAddressRepository.flush();
        return evm.find(em, CustomerAddressView.class, saved.getId());
    }
}