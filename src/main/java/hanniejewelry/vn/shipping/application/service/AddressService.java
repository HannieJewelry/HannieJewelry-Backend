package hanniejewelry.vn.shipping.application.service;

import hanniejewelry.vn.customer.entity.CustomerAddress;
import hanniejewelry.vn.order.dto.OrderRequest;
import hanniejewelry.vn.order.entity.Order;
import hanniejewelry.vn.shipping.infrastructure.persistence.entity.OrderBillingAddress;
import hanniejewelry.vn.shipping.infrastructure.persistence.entity.OrderShippingAddress;
import hanniejewelry.vn.shipping.api.ShippingFacade;
import hanniejewelry.vn.shipping.domain.model.District;
import hanniejewelry.vn.shipping.domain.model.Province;
import hanniejewelry.vn.shipping.domain.model.Ward;
import hanniejewelry.vn.shipping.domain.repository.DistrictRepositoryPort;
import hanniejewelry.vn.shipping.domain.repository.ProvinceRepositoryPort;
import hanniejewelry.vn.shipping.domain.repository.WardRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AddressService implements ShippingFacade {
    private final ProvinceRepositoryPort provinceRepository;
    private final DistrictRepositoryPort districtRepository;
    private final WardRepositoryPort wardRepository;
    @Override
    public void setShippingAddress(Order order, OrderRequest.OrderInfo orderInfo, CustomerAddress defaultAddress) {
        OrderShippingAddress shippingAddress = null;
        
        if (defaultAddress != null) {
            shippingAddress = createShippingAddressFromCustomerAddress(order, defaultAddress);
        } else if (orderInfo.getShippingAddress() != null) {
            shippingAddress = createShippingAddressFromOrderInfo(order, orderInfo.getShippingAddress());
        }
        
        if (shippingAddress != null) {
            order.setShippingAddress(shippingAddress);
        }
    }
    

    private OrderShippingAddress createShippingAddressFromCustomerAddress(Order order, CustomerAddress address) {
        return OrderShippingAddress.builder()
                .order(order)
                .address1(address.getAddress1())
                .address2(address.getAddress2())
                .firstName(address.getFirstName())
                .lastName(address.getLastName())
                .company(address.getCompany())
                .phone(address.getPhone())
                .zip(address.getZip())
                .provinceCode(address.getProvinceCode())
                .countryCode(address.getCountryCode())
                .districtCode(address.getDistrictCode())
                .wardCode(address.getWardCode())
                .name(address.getName())
                .country(address.getCountry())
                .province(address.getProvince())
                .district(address.getDistrict())
                .ward(address.getWard())
                .build();
    }
    

    private OrderShippingAddress createShippingAddressFromOrderInfo(Order order, OrderRequest.AddressInfo addressInfo) {
        String province = null;
        String district = null;
        String ward = null;
        String country = "Vietnam";
        
        if (addressInfo.getProvinceCode() != null) {
            Optional<Province> provinceObj = provinceRepository.findByCode(addressInfo.getProvinceCode());
            if (provinceObj.isPresent()) {
                province = provinceObj.get().getName();
            }
        }
        
        if (addressInfo.getDistrictCode() != null) {
            Optional<District> districtObj = districtRepository.findByCode(addressInfo.getDistrictCode());
            if (districtObj.isPresent()) {
                district = districtObj.get().getName();
            }
        }
        
        if (addressInfo.getWardCode() != null) {
            Optional<Ward> wardObj = wardRepository.findByCode(addressInfo.getWardCode());
            if (wardObj.isPresent()) {
                ward = wardObj.get().getName();
            }
        }
        
        return OrderShippingAddress.builder()
                .order(order)
                .address1(addressInfo.getAddress1())
                .address2(addressInfo.getAddress2())
                .firstName(addressInfo.getFirstName())
                .lastName(addressInfo.getLastName())
                .company(addressInfo.getCompany())
                .phone(addressInfo.getPhone())
                .zip(addressInfo.getZip())
                .provinceCode(addressInfo.getProvinceCode())
                .countryCode(addressInfo.getCountryCode())
                .districtCode(addressInfo.getDistrictCode())
                .wardCode(addressInfo.getWardCode())
                .name(addressInfo.getFirstName() + " " + addressInfo.getLastName())
                .country(country)
                .province(province)
                .district(district)
                .ward(ward)
                .build();
    }
    

    @Override
    public void setBillingAddress(Order order, OrderRequest.OrderInfo orderInfo, CustomerAddress defaultAddress) {
        OrderBillingAddress billingAddress = null;
        
        if (orderInfo.getBillingAddress() != null) {
            if (order.getShippingAddress() != null) {
                billingAddress = createBillingAddressFromShippingAddress(order, order.getShippingAddress(), 
                                                                        orderInfo.getBillingAddress().getZip());
            } else {
                billingAddress = OrderBillingAddress.builder()
                        .order(order)
                        .zip(orderInfo.getBillingAddress().getZip())
                        .defaultAddress(true)
                        .build();
            }
        } else if (order.getShippingAddress() != null) {
            billingAddress = createBillingAddressFromShippingAddress(order, order.getShippingAddress(), null);
        } else if (defaultAddress != null) {
            billingAddress = createBillingAddressFromCustomerAddress(order, defaultAddress);
        }
        
        if (billingAddress != null) {
            order.setBillingAddress(billingAddress);
        }
    }
    

    private OrderBillingAddress createBillingAddressFromShippingAddress(Order order, OrderShippingAddress shippingAddress, 
                                                                       String overrideZip) {
        OrderBillingAddress billingAddress = OrderBillingAddress.builder()
                .order(order)
                .address1(shippingAddress.getAddress1())
                .address2(shippingAddress.getAddress2())
                .firstName(shippingAddress.getFirstName())
                .lastName(shippingAddress.getLastName())
                .company(shippingAddress.getCompany())
                .phone(shippingAddress.getPhone())
                .provinceCode(shippingAddress.getProvinceCode())
                .countryCode(shippingAddress.getCountryCode())
                .districtCode(shippingAddress.getDistrictCode())
                .wardCode(shippingAddress.getWardCode())
                .name(shippingAddress.getName())
                .country(shippingAddress.getCountry())
                .province(shippingAddress.getProvince())
                .district(shippingAddress.getDistrict())
                .ward(shippingAddress.getWard())
                .defaultAddress(true)
                .build();
        
        if (overrideZip != null) {
            billingAddress.setZip(overrideZip);
        } else {
            billingAddress.setZip(shippingAddress.getZip());
        }
        
        return billingAddress;
    }
    

    private OrderBillingAddress createBillingAddressFromCustomerAddress(Order order, CustomerAddress address) {
        return OrderBillingAddress.builder()
                .order(order)
                .address1(address.getAddress1())
                .address2(address.getAddress2())
                .firstName(address.getFirstName())
                .lastName(address.getLastName())
                .company(address.getCompany())
                .phone(address.getPhone())
                .zip(address.getZip())
                .provinceCode(address.getProvinceCode())
                .countryCode(address.getCountryCode())
                .districtCode(address.getDistrictCode())
                .wardCode(address.getWardCode())
                .name(address.getName())
                .country(address.getCountry())
                .province(address.getProvince())
                .district(address.getDistrict())
                .ward(address.getWard())
                .defaultAddress(true)
                .build();
    }
} 