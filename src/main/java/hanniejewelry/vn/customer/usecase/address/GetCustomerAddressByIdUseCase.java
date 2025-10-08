package hanniejewelry.vn.customer.usecase.address;

import com.blazebit.persistence.view.EntityViewManager;
import hanniejewelry.vn.customer.repository.CustomerAddressRepository;
import hanniejewelry.vn.customer.view.CustomerAddressView;
import hanniejewelry.vn.shared.exception.BaseMessageType;
import hanniejewelry.vn.shared.exception.BizException;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetCustomerAddressByIdUseCase {

    private final EntityViewManager evm;
    private final EntityManager em;
    private final CustomerAddressRepository customerAddressRepository;

    public CustomerAddressView execute(UUID customerId, UUID addressId) {
        return customerAddressRepository.findByCustomerIdAndId(customerId, addressId)
                .map(address -> evm.find(em, CustomerAddressView.class, address.getId()))
                .orElseThrow(() -> new BizException(BaseMessageType.NOT_FOUND, 
                    "Address not found with id: {0} for customer: {1}", addressId, customerId));
    }
} 