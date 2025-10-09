package hanniejewelry.vn.customer.usecase.address;

import com.blazebit.persistence.view.EntityViewManager;
import hanniejewelry.vn.customer.dto.CustomerAddressRequest;
import hanniejewelry.vn.customer.entity.CustomerAddress;
import hanniejewelry.vn.customer.repository.CustomerAddressRepository;
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

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class UpdateCustomerAddressUseCase {

    private final EntityViewManager evm;
    private final EntityManager em;
    private final CustomerAddressRepository customerAddressRepository;
    private final CountryRepository countryRepository;
    private final ProvinceRepository provinceRepository;
    private final DistrictRepository districtRepository;
    private final WardRepository wardRepository;

    public CustomerAddressView execute(UUID customerId, UUID addressId, CustomerAddressRequest request) {
        CustomerAddress existing = customerAddressRepository.findByCustomerIdAndId(customerId, addressId)
                .orElseThrow(() -> new BizException(BaseMessageType.NOT_FOUND, 
                    "Address not found with id: {0} for customer: {1}", addressId, customerId));

        if (Boolean.TRUE.equals(request.getIsDefault()) && !Boolean.TRUE.equals(existing.getIsDefault())) {
            customerAddressRepository.findByCustomerIdAndIsDefaultTrue(customerId)
                    .ifPresent(defaultAddress -> customerAddressRepository.save(
                            defaultAddress.toBuilder().isDefault(false).build()));
        }

        String countryName = request.getCountryCode() == null ? existing.getCountry() : 
                countryRepository.findByCode(request.getCountryCode().toUpperCase())
                        .map(entity -> entity.getName()).orElse(null);
                        
        String provinceName = request.getProvinceCode() == null ? existing.getProvince() :
                provinceRepository.findByCode(request.getProvinceCode())
                        .map(entity -> entity.getName()).orElse(null);
                        
        String districtName = request.getDistrictCode() == null ? existing.getDistrict() :
                districtRepository.findByCode(request.getDistrictCode())
                        .map(entity -> entity.getName()).orElse(null);
                        
        String wardName = request.getWardCode() == null ? existing.getWard() :
                wardRepository.findByCode(request.getWardCode())
                        .map(entity -> entity.getName()).orElse(null);

        String firstName = request.getFirstName() != null ? request.getFirstName() : existing.getFirstName();
        String lastName = request.getLastName() != null ? request.getLastName() : existing.getLastName();
        String fullName = (request.getFirstName() != null || request.getLastName() != null) ? 
                (firstName == null && lastName == null ? null : 
                 firstName == null ? lastName : 
                 lastName == null ? firstName : 
                 lastName + " " + firstName) : existing.getName();

        CustomerAddress saved = customerAddressRepository.save(existing.toBuilder()
                .address1(request.getAddress1() != null ? request.getAddress1() : existing.getAddress1())
                .address2(request.getAddress2() != null ? request.getAddress2() : existing.getAddress2())
                .city(request.getCity() != null ? request.getCity() : existing.getCity())
                .company(request.getCompany() != null ? request.getCompany() : existing.getCompany())
                .country(countryName)
                .firstName(firstName)
                .lastName(lastName)
                .phone(request.getPhone() != null ? request.getPhone() : existing.getPhone())
                .province(provinceName)
                .zip(request.getZip() != null ? request.getZip() : existing.getZip())
                .name(fullName)
                .provinceCode(request.getProvinceCode() != null ? request.getProvinceCode() : existing.getProvinceCode())
                .countryCode(request.getCountryCode() != null ? request.getCountryCode().toLowerCase() : existing.getCountryCode())
                .countryName(countryName)
                .isDefault(request.getIsDefault() != null ? request.getIsDefault() : existing.getIsDefault())
                .district(districtName)
                .districtCode(request.getDistrictCode() != null ? request.getDistrictCode() : existing.getDistrictCode())
                .ward(wardName)
                .wardCode(request.getWardCode() != null ? request.getWardCode() : existing.getWardCode())
                .build());

        customerAddressRepository.flush();
        return evm.find(em, CustomerAddressView.class, saved.getId());
    }
} 