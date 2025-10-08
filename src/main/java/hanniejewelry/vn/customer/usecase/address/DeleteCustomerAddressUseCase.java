package hanniejewelry.vn.customer.usecase.address;

import hanniejewelry.vn.customer.entity.CustomerAddress;
import hanniejewelry.vn.customer.repository.CustomerAddressRepository;
import hanniejewelry.vn.shared.exception.BaseMessageType;
import hanniejewelry.vn.shared.exception.BizException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class DeleteCustomerAddressUseCase {

    private final CustomerAddressRepository customerAddressRepository;

    public void execute(UUID customerId, UUID addressId) {
        CustomerAddress address = customerAddressRepository.findByCustomerIdAndId(customerId, addressId)
                .orElseThrow(() -> new BizException(BaseMessageType.NOT_FOUND,
                        "Address not found with id: {0} for customer: {1}", addressId, customerId));

        if (Boolean.TRUE.equals(address.getIsDefault())) {
            long otherAddressCount = customerAddressRepository.findByCustomerId(customerId)
                    .stream()
                    .filter(addr -> !addr.getId().equals(addressId))
                    .count();

            if (otherAddressCount == 0) {
                throw new BizException(BaseMessageType.BAD_REQUEST,
                        "Cannot delete the only default address. Customer must have at least one address.");
            } else {
                throw new BizException(BaseMessageType.BAD_REQUEST,
                        "Cannot delete default address. Please set another address as default before deleting. " +
                                "Customer has {0} other addresses that can be set as default.", otherAddressCount);
            }
        }

        customerAddressRepository.softDeleteById(addressId);
        customerAddressRepository.flush();
    }
}