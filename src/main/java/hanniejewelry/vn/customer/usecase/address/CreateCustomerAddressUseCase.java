package hanniejewelry.vn.customer.usecase.address;

import com.blazebit.persistence.view.EntityViewManager;
import hanniejewelry.vn.customer.dto.CustomerAddressRequest;
import hanniejewelry.vn.customer.entity.Customer;
import hanniejewelry.vn.customer.entity.CustomerAddress;
import hanniejewelry.vn.customer.repository.CustomerAddressRepository;
import hanniejewelry.vn.customer.repository.CustomerRepository;
import hanniejewelry.vn.customer.view.CustomerAddressView;
import hanniejewelry.vn.shared.exception.BaseMessageType;
import hanniejewelry.vn.shared.exception.BizException;
import hanniejewelry.vn.shipping.infrastructure.persistence.repository.CountryRepository;
import hanniejewelry.vn.shipping.infrastructure.persistence.repository.DistrictRepository;
import hanniejewelry.vn.shipping.infrastructure.persistence.repository.ProvinceRepository;
import hanniejewelry.vn.shipping.infrastructure.persistence.repository.WardRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class CreateCustomerAddressUseCase {

    private final EntityViewManager evm;
    private final EntityManager em;
    private final CustomerAddressRepository customerAddressRepository;
    private final CustomerRepository customerRepository;
    private final CountryRepository countryRepository;
    private final ProvinceRepository provinceRepository;
    private final DistrictRepository districtRepository;
    private final WardRepository wardRepository;

    public CustomerAddressView execute(UUID customerId, CustomerAddressRequest request) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new BizException(BaseMessageType.NOT_FOUND, "Customer not found with id: {0}", customerId));

        String countryName = request.getCountryCode() == null ? null :
                countryRepository.findByCode(request.getCountryCode().toUpperCase())
                        .map(entity -> entity.getName()).orElse(null);

        String provinceName = request.getProvinceCode() == null ? null :
                provinceRepository.findByCode(request.getProvinceCode())
                        .map(entity -> entity.getName()).orElse(null);

        String districtName = request.getDistrictCode() == null ? null :
                districtRepository.findByCode(request.getDistrictCode())
                        .map(entity -> entity.getName()).orElse(null);

        String wardName = request.getWardCode() == null ? null :
                wardRepository.findByCode(request.getWardCode())
                        .map(entity -> entity.getName()).orElse(null);

        String fullName = generateFullName(request.getFirstName(), request.getLastName());

        // 🚩 Bước 1: luôn tạo mới với isDefault = false
        CustomerAddress saved = customerAddressRepository.save(CustomerAddress.builder()
                .address1(request.getAddress1())
                .address2(request.getAddress2())
                .city(request.getCity())
                .company(request.getCompany())
                .country(countryName)
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phone(request.getPhone())
                .province(provinceName)
                .zip(request.getZip())
                .name(fullName)
                .provinceCode(request.getProvinceCode())
                .countryCode(request.getCountryCode() != null ? request.getCountryCode().toLowerCase() : null)
                .countryName(countryName)
                .isDefault(false) // luôn false khi tạo
                .district(districtName)
                .districtCode(request.getDistrictCode())
                .ward(wardName)
                .wardCode(request.getWardCode())
                .customer(customer)
                .build());

        customerAddressRepository.flush();

        customerAddressRepository.findByCustomerIdAndIsDefaultTrue(customerId)
                .or(() -> Optional.of(saved))
                .ifPresent(addr -> {
                    if (addr.getId().equals(saved.getId())) {
                        customerAddressRepository.save(addr.toBuilder().isDefault(true).build());
                        customerAddressRepository.flush();
                    }
                });

        return evm.find(em, CustomerAddressView.class, saved.getId());
    }


    private String generateFullName(String firstName, String lastName) {
        if (firstName == null && lastName == null) return null;
        if (firstName == null) return lastName;
        if (lastName == null) return firstName;
        return lastName + " " + firstName;
    }
}
