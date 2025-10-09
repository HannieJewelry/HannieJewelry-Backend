package hanniejewelry.vn.customer.usecase.address;

import com.blazebit.persistence.view.EntityViewManager;
import hanniejewelry.vn.customer.dto.CustomerAddressRequest;
import hanniejewelry.vn.customer.entity.Customer;
import hanniejewelry.vn.customer.entity.CustomerAddress;
import hanniejewelry.vn.customer.repository.CustomerAddressRepository;
import hanniejewelry.vn.customer.view.CustomerAddressView;
import hanniejewelry.vn.shared.utils.SecurityUtils;
import hanniejewelry.vn.shipping.infrastructure.persistence.repository.CountryRepository;
import hanniejewelry.vn.shipping.infrastructure.persistence.repository.DistrictRepository;
import hanniejewelry.vn.shipping.infrastructure.persistence.repository.ProvinceRepository;
import hanniejewelry.vn.shipping.infrastructure.persistence.repository.WardRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class CreateCustomerAddressForCurrentUserUseCase {

    private final EntityViewManager evm;
    private final EntityManager em;
    private final CustomerAddressRepository customerAddressRepository;
    private final CountryRepository countryRepository;
    private final ProvinceRepository provinceRepository;
    private final DistrictRepository districtRepository;
    private final WardRepository wardRepository;

    public CustomerAddressView execute(CustomerAddressRequest request) {
        Customer customer = SecurityUtils.getCurrentCustomer();

        boolean hasDefault = customerAddressRepository.findByCustomerIdAndIsDefaultTrue(customer.getId()).isPresent();

        boolean isDefault = !hasDefault || Boolean.TRUE.equals(request.getIsDefault());

        if (isDefault && hasDefault) {
            customerAddressRepository.findByCustomerIdAndIsDefaultTrue(customer.getId())
                    .ifPresent(defaultAddress -> customerAddressRepository.save(
                            defaultAddress.toBuilder().isDefault(false).build()));
        }

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
                .isDefault(isDefault)
                .district(districtName)
                .districtCode(request.getDistrictCode())
                .ward(wardName)
                .wardCode(request.getWardCode())
                .customer(customer)
                .build());

        customerAddressRepository.flush();
        return evm.find(em, CustomerAddressView.class, saved.getId());
    }

    private String generateFullName(String firstName, String lastName) {
        if (firstName == null && lastName == null) return null;
        if (firstName == null) return lastName;
        if (lastName == null) return firstName;
        return lastName + " " + firstName;
    }
}
