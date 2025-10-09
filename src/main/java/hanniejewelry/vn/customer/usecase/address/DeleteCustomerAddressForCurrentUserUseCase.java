package hanniejewelry.vn.customer.usecase.address;

import hanniejewelry.vn.customer.entity.Customer;
import hanniejewelry.vn.customer.entity.CustomerAddress;
import hanniejewelry.vn.customer.repository.CustomerAddressRepository;
import hanniejewelry.vn.shared.exception.BaseMessageType;
import hanniejewelry.vn.shared.exception.BizException;
import hanniejewelry.vn.shared.utils.SecurityUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class DeleteCustomerAddressForCurrentUserUseCase {

    private final CustomerAddressRepository customerAddressRepository;

    public void execute(UUID addressId) {
        Customer customer = SecurityUtils.getCurrentCustomer();
        
        CustomerAddress address = customerAddressRepository.findById(addressId)
                .orElseThrow(() -> new BizException(BaseMessageType.NOT_FOUND, "Không tìm thấy địa chỉ với ID: {0}", addressId));
        
        if (!address.getCustomer().getId().equals(customer.getId())) {
            throw new BizException(BaseMessageType.FORBIDDEN, "Bạn không có quyền xóa địa chỉ này");
        }

        if (Boolean.TRUE.equals(address.getIsDefault())) {
            long otherAddressCount = customerAddressRepository.findByCustomerId(customer.getId())
                    .stream()
                    .filter(addr -> !addr.getId().equals(addressId))
                    .count();
                    
            if (otherAddressCount == 0) {
                throw new BizException(BaseMessageType.BAD_REQUEST, 
                    "Không thể xoá địa chỉ mặc định duy nhất. Khách hàng phải có ít nhất một địa chỉ.");
            } else {
                throw new BizException(BaseMessageType.BAD_REQUEST, 
                    "Không thể xoá địa chỉ mặc định. Vui lòng đặt địa chỉ khác làm mặc định trước khi xoá. " +
                    "Khách hàng có {0} địa chỉ khác có thể được đặt làm mặc định.", otherAddressCount);
            }
        }

        customerAddressRepository.softDeleteById(addressId);
        customerAddressRepository.flush();
    }
} 