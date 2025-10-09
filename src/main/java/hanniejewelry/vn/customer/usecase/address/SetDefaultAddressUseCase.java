package hanniejewelry.vn.customer.usecase.address;

import com.blazebit.persistence.view.EntityViewManager;
import hanniejewelry.vn.customer.entity.CustomerAddress;
import hanniejewelry.vn.customer.repository.CustomerAddressRepository;
import hanniejewelry.vn.customer.view.CustomerAddressView;
import hanniejewelry.vn.shared.exception.BaseMessageType;
import hanniejewelry.vn.shared.exception.BizException;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class SetDefaultAddressUseCase {

    private final EntityViewManager evm;
    private final EntityManager em;
    private final CustomerAddressRepository customerAddressRepository;

    public CustomerAddressView execute(UUID customerId, UUID addressId) {
        CustomerAddress address = customerAddressRepository.findByCustomerIdAndId(customerId, addressId)
                .orElseThrow(() -> new BizException(BaseMessageType.NOT_FOUND, 
                    "Address not found with id: {0} for customer: {1}", addressId, customerId));

        customerAddressRepository.findByCustomerIdAndIsDefaultTrue(customerId)
                .ifPresent(defaultAddress -> customerAddressRepository.save(
                        defaultAddress.toBuilder().isDefault(false).build()));

        CustomerAddress saved = customerAddressRepository.save(
                address.toBuilder().isDefault(true).build());
                
        customerAddressRepository.flush();
        return evm.find(em, CustomerAddressView.class, saved.getId());
    }
} 