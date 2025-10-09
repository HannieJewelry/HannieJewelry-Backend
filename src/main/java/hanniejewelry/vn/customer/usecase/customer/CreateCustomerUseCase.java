package hanniejewelry.vn.customer.usecase.customer;

import hanniejewelry.vn.customer.dto.CustomerRequest;
import hanniejewelry.vn.customer.entity.Customer;
import hanniejewelry.vn.customer.entity.CustomerAddress;
import hanniejewelry.vn.customer.entity.EInvoiceInfo;
import hanniejewelry.vn.customer.repository.CustomerAddressRepository;
import hanniejewelry.vn.customer.repository.CustomerRepository;
import hanniejewelry.vn.customer.repository.EInvoiceInfoRepository;
import hanniejewelry.vn.customer.view.CustomerView;
import hanniejewelry.vn.shipping.infrastructure.persistence.repository.CountryRepository;
import hanniejewelry.vn.shipping.infrastructure.persistence.repository.DistrictRepository;
import hanniejewelry.vn.shipping.infrastructure.persistence.repository.ProvinceRepository;
import hanniejewelry.vn.shipping.infrastructure.persistence.repository.WardRepository;
import hanniejewelry.vn.shared.exception.BaseMessageType;
import hanniejewelry.vn.shared.exception.BizException;
import com.blazebit.persistence.view.EntityViewManager;
import io.vavr.control.Option;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class CreateCustomerUseCase {
    private final CustomerRepository customerRepository;
    private final CustomerAddressRepository customerAddressRepository;
    private final EInvoiceInfoRepository eInvoiceInfoRepository;
    private final CountryRepository countryRepository;
    private final ProvinceRepository provinceRepository;
    private final DistrictRepository districtRepository;
    private final WardRepository wardRepository;
    private final EntityViewManager evm;
    private final EntityManager em;

    public CustomerView execute(CustomerRequest req) {
        Option.of(req.getEmail()).filter(email -> customerRepository.findByEmail(email).isPresent())
                .forEach(email -> { throw new BizException(BaseMessageType.ENTITY_ALREADY_EXISTS, "Customer with email {0} already exists", email); });
        
        Option.of(req.getPhone()).filter(phone -> customerRepository.findByPhone(phone).isPresent())
                .forEach(phone -> { throw new BizException(BaseMessageType.ENTITY_ALREADY_EXISTS, "Customer with phone {0} already exists", phone); });

        var customer = customerRepository.save(Customer.builder()
                .acceptsMarketing(Option.of(req.getIsAcceptMarketing()).getOrElse(false))
                .email(req.getEmail())
                .phone(req.getPhone())
                .firstName(req.getFirstName())
                .lastName(req.getLastName())
                .note(req.getNote())
                .ordersCount(0)
                .tags(req.getTags())
                .totalSpent(java.math.BigDecimal.ZERO)
                .totalPaid(java.math.BigDecimal.ZERO)
                .birthday(req.getBirthday())
                .gender(req.getGender())
                .build());

        customerRepository.flush();

        Option.of(req.getAddresses()).filter(addresses -> !addresses.isEmpty())
                .peek(addresses -> {
                    var index = new AtomicInteger(0);
                    addresses.forEach(addr -> {
                        var countryName = Option.of(addr.getCountryName())
                                .orElse(Option.of(addr.getCountryCode())
                                        .flatMap(code -> Option.ofOptional(countryRepository.findByCode(code)))
                                        .map(entity -> entity.getName()))
                                .getOrElse(addr.getCountry());
                        
                        var provinceName = Option.of(addr.getProvince())
                                .orElse(Option.of(addr.getProvinceCode())
                                        .flatMap(code -> Option.ofOptional(provinceRepository.findByCode(code)))
                                        .map(entity -> entity.getName()))
                                .getOrElse(addr.getProvince());
                        
                        var districtName = Option.of(addr.getDistrict())
                                .orElse(Option.of(addr.getDistrictCode())
                                        .flatMap(code -> Option.ofOptional(districtRepository.findByCode(code)))
                                        .map(entity -> entity.getName()))
                                .getOrElse(addr.getDistrict());
                        
                        var wardName = Option.of(addr.getWard())
                                .orElse(Option.of(addr.getWardCode())
                                        .flatMap(code -> Option.ofOptional(wardRepository.findByCode(code)))
                                        .map(entity -> entity.getName()))
                                .getOrElse(addr.getWard());

                        customerAddressRepository.save(CustomerAddress.builder()
                                .address1(addr.getAddress1())
                                .address2(addr.getAddress2())
                                .city(addr.getCity())
                                .company(addr.getCompany())
                                .country(countryName)
                                .firstName(addr.getFirstName())
                                .lastName(addr.getLastName())
                                .phone(addr.getPhone())
                                .province(provinceName)
                                .zip(addr.getZip())
                                .name(addr.getName())
                                .provinceCode(addr.getProvinceCode())
                                .countryCode(addr.getCountryCode())
                                .countryName(countryName)
                                .isDefault(index.getAndIncrement() == 0)
                                .district(districtName)
                                .districtCode(addr.getDistrictCode())
                                .ward(wardName)
                                .wardCode(addr.getWardCode())
                                .customer(customer)
                                .build());
                    });
                });

        // Create EInvoiceInfo
        Option.of(req.getEInvoiceInfo()).peek(eInvoiceReq -> 
                eInvoiceInfoRepository.save(EInvoiceInfo.builder()
                        .name(eInvoiceReq.getName())
                        .taxCode(eInvoiceReq.getTaxCode())
                        .email(eInvoiceReq.getEmail())
                        .address(eInvoiceReq.getAddress())
                        .isCompany(Option.of(eInvoiceReq.getIsCompany()).getOrElse(false))
                        .customer(customer)
                        .build()));

        return evm.find(em, CustomerView.class, customer.getId());
    }
}
