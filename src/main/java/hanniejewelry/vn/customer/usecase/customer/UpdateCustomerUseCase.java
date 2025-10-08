package hanniejewelry.vn.customer.usecase.customer;

import hanniejewelry.vn.customer.dto.CustomerRequest;
import hanniejewelry.vn.customer.entity.CustomerAddress;
import hanniejewelry.vn.customer.entity.EInvoiceInfo;
import hanniejewelry.vn.customer.repository.CustomerAddressRepository;
import hanniejewelry.vn.customer.repository.CustomerRepository;
import hanniejewelry.vn.customer.repository.EInvoiceInfoRepository;
import hanniejewelry.vn.customer.view.CustomerView;
import hanniejewelry.vn.shared.exception.BaseMessageType;
import hanniejewelry.vn.shared.exception.BizException;
import com.blazebit.persistence.view.EntityViewManager;
import io.vavr.control.Option;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class UpdateCustomerUseCase {
    private final CustomerRepository customerRepository;
    private final CustomerAddressRepository customerAddressRepository;
    private final EInvoiceInfoRepository eInvoiceInfoRepository;
    private final EntityViewManager evm;
    private final EntityManager em;

    public CustomerView execute(UUID id, CustomerRequest req) {
        var existing = customerRepository.findById(id)
                .orElseThrow(() -> new BizException(BaseMessageType.NOT_FOUND, "Customer not found with id: {0}", id));

        Option.of(req.getEmail()).filter(email -> !email.equals(existing.getEmail()))
                .filter(email -> customerRepository.findByEmail(email).isPresent())
                .forEach(email -> { throw new BizException(BaseMessageType.ENTITY_ALREADY_EXISTS, "Customer with email {0} already exists", email); });

        Option.of(req.getPhone()).filter(phone -> !phone.equals(existing.getPhone()))
                .filter(phone -> customerRepository.findByPhone(phone).isPresent())
                .forEach(phone -> { throw new BizException(BaseMessageType.ENTITY_ALREADY_EXISTS, "Customer with phone {0} already exists", phone); });

        var customer = customerRepository.save(existing.toBuilder()
                .acceptsMarketing(Option.of(req.getIsAcceptMarketing()).getOrElse(existing.getAcceptsMarketing()))
                .email(Option.of(req.getEmail()).getOrElse(existing.getEmail()))
                .phone(Option.of(req.getPhone()).getOrElse(existing.getPhone()))
                .firstName(Option.of(req.getFirstName()).getOrElse(existing.getFirstName()))
                .lastName(Option.of(req.getLastName()).getOrElse(existing.getLastName()))
                .note(Option.of(req.getNote()).getOrElse(existing.getNote()))
                .tags(Option.of(req.getTags()).getOrElse(existing.getTags()))
                .birthday(Option.of(req.getBirthday()).getOrElse(existing.getBirthday()))
                .gender(Option.of(req.getGender()).getOrElse(existing.getGender()))
                .build());

        customerRepository.flush();

        Option.of(req.getAddresses()).filter(addresses -> !addresses.isEmpty())
                .peek(addresses -> customerAddressRepository.findByCustomerIdAndIsDefaultTrue(customer.getId())
                        .ifPresent(address -> {
                            address.setIsDefault(false);
                            customerAddressRepository.save(address);
                        }))
                .peek(addresses -> {
                    var index = new AtomicInteger(0);
                    addresses.forEach(addr -> customerAddressRepository.save(CustomerAddress.builder()
                            .address1(addr.getAddress1())
                            .address2(addr.getAddress2())
                            .city(addr.getCity())
                            .company(addr.getCompany())
                            .country(addr.getCountry())
                            .firstName(addr.getFirstName())
                            .lastName(addr.getLastName())
                            .phone(addr.getPhone())
                            .province(addr.getProvince())
                            .zip(addr.getZip())
                            .name(addr.getName())
                            .provinceCode(addr.getProvinceCode())
                            .countryCode(addr.getCountryCode())
                            .countryName(addr.getCountryName())
                            .isDefault(index.getAndIncrement() == 0)
                            .district(addr.getDistrict())
                            .districtCode(addr.getDistrictCode())
                            .ward(addr.getWard())
                            .wardCode(addr.getWardCode())
                            .customer(customer)
                            .build()));
                });

        Option.of(req.getEInvoiceInfo()).peek(eInvoiceReq -> {
            var existingEInvoice = eInvoiceInfoRepository.findByCustomerId(customer.getId()).orElse(null);
            
            if (existingEInvoice != null) {
                eInvoiceInfoRepository.save(existingEInvoice.toBuilder()
                        .name(Option.of(eInvoiceReq.getName()).getOrElse(existingEInvoice.getName()))
                        .taxCode(Option.of(eInvoiceReq.getTaxCode()).getOrElse(existingEInvoice.getTaxCode()))
                        .email(Option.of(eInvoiceReq.getEmail()).getOrElse(existingEInvoice.getEmail()))
                        .address(Option.of(eInvoiceReq.getAddress()).getOrElse(existingEInvoice.getAddress()))
                        .isCompany(Option.of(eInvoiceReq.getIsCompany()).getOrElse(existingEInvoice.getIsCompany()))
                        .build());
            } else {
                eInvoiceInfoRepository.save(EInvoiceInfo.builder()
                        .name(eInvoiceReq.getName())
                        .taxCode(eInvoiceReq.getTaxCode())
                        .email(eInvoiceReq.getEmail())
                        .address(eInvoiceReq.getAddress())
                        .isCompany(Option.of(eInvoiceReq.getIsCompany()).getOrElse(false))
                        .customer(customer)
                        .build());
            }
        });

        return evm.find(em, CustomerView.class, customer.getId());
    }
}
