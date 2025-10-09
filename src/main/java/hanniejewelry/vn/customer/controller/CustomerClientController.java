package hanniejewelry.vn.customer.controller;

import hanniejewelry.vn.customer.dto.CustomerProfileUpdateRequest;
import hanniejewelry.vn.customer.usecase.customer.GetCustomerByIdUseCase;
import hanniejewelry.vn.customer.usecase.customer.UpdateCustomerProfileUseCase;
import hanniejewelry.vn.customer.view.CustomerView;
import hanniejewelry.vn.shared.constants.ApiConstants;
import hanniejewelry.vn.shared.dto.RestResponse;
import hanniejewelry.vn.shared.utils.RestResponseUtils;
import hanniejewelry.vn.shared.utils.SecurityUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping(ApiConstants.API_PREFIX + "/client/customers/profile")
@RequiredArgsConstructor
@Slf4j
public class CustomerClientController {

    private final UpdateCustomerProfileUseCase updateCustomerProfileUseCase;
    private final GetCustomerByIdUseCase getCustomerByIdUseCase;

    @GetMapping
    public ResponseEntity<RestResponse<Map<String, CustomerView>>> getCustomerProfile() {
        UUID customerId = SecurityUtils.getCurrentCustomer().getId();
        log.info("Fetching customer profile for customer ID: {}", customerId);
        
        CustomerView customerView = getCustomerByIdUseCase.execute(customerId);
        
        log.info("Retrieved customer profile: id={}, email={}, phone={}, orders_count={}, last_order_date={}, total_spent={}",
                customerView.getId(), customerView.getEmail(), customerView.getPhone(), 
                customerView.getOrdersCount(), customerView.getLastOrderDate(), customerView.getTotalSpent());
        
        return RestResponseUtils.successResponse(Map.of("customer", customerView));
    }

    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<RestResponse<Map<String, CustomerView>>> updateCustomerProfile(
            @Valid @ModelAttribute CustomerProfileUpdateRequest request) {
        log.info("Updating customer profile with request: {}", request);
        
        CustomerView updatedCustomer = updateCustomerProfileUseCase.execute(request);
        
        log.info("Customer profile updated successfully: id={}, email={}, orders_count={}",
                updatedCustomer.getId(), updatedCustomer.getEmail(), updatedCustomer.getOrdersCount());
        
        return RestResponseUtils.successResponse(Map.of("customer", updatedCustomer));
    }
}
