package hanniejewelry.vn.customer.controller;

import hanniejewelry.vn.customer.dto.CustomerProfileUpdateRequest;
import hanniejewelry.vn.customer.usecase.customer.*;
import hanniejewelry.vn.customer.usecase.tags.AddCustomerTagsUseCase;
import hanniejewelry.vn.customer.usecase.tags.DeleteCustomerTagsUseCase;
import hanniejewelry.vn.shared.dto.RestResponse;
import hanniejewelry.vn.shared.utils.RestResponseUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import hanniejewelry.vn.customer.dto.CustomerRequest;
import hanniejewelry.vn.customer.dto.CustomerTagsRequest;
import hanniejewelry.vn.customer.view.CustomerView;
import hanniejewelry.vn.customer.dto.CustomerSearchRequest;
import hanniejewelry.vn.shared.dto.PagedResponse;
import hanniejewelry.vn.shared.utils.ResponseUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import hanniejewelry.vn.shared.constants.ApiConstants;

@RestController
@RequestMapping(ApiConstants.API_PREFIX + "/customers")
@RequiredArgsConstructor
@Slf4j
public class CustomerController {

    private final GetAllCustomersWithBlazeFilterUseCase getAllCustomersWithBlazeFilterUseCase;
    private final SearchCustomersUseCase searchCustomersUseCase;
    private final CountCustomersUseCase countCustomersUseCase;
    private final GetCustomerByIdUseCase getCustomerByIdUseCase;
    private final CreateCustomerUseCase createCustomerUseCase;
    private final UpdateCustomerUseCase updateCustomerUseCase;
    private final DeleteCustomerUseCase deleteCustomerUseCase;
    private final AddCustomerTagsUseCase addCustomerTagsUseCase;
    private final DeleteCustomerTagsUseCase deleteCustomerTagsUseCase;
    private final UpdateCustomerProfileUseCase updateCustomerProfileUseCase;

    @GetMapping
    public ResponseEntity<RestResponse<PagedResponse<CustomerView>>> getCustomers(
        @ModelAttribute CustomerSearchRequest filter) {
    return RestResponseUtils.successResponse(
        ResponseUtils.pagedSuccess(getAllCustomersWithBlazeFilterUseCase.execute(filter), filter)
    );
    }

    @GetMapping("/search")
    public ResponseEntity<RestResponse<PagedResponse<CustomerView>>> searchCustomers(
            @RequestParam String query,
            @ModelAttribute CustomerSearchRequest filter) {
        var result = searchCustomersUseCase.execute(query, filter);
        return RestResponseUtils.successResponse(ResponseUtils.pagedSuccess(result, filter));
    }

    @GetMapping("/count")
    public ResponseEntity<RestResponse<Map<String, Long>>> countCustomers() {
        return RestResponseUtils.successResponse(Map.of("count", countCustomersUseCase.execute()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestResponse<Map<String, CustomerView>>> getCustomerById(@PathVariable UUID id) {
        return RestResponseUtils.successResponse(Map.of("customer", getCustomerByIdUseCase.execute(id)));
    }

    @PostMapping
    public ResponseEntity<RestResponse<Map<String, CustomerView>>> createCustomer(@Valid @RequestBody CustomerRequest request) {
        return RestResponseUtils.createdResponse(Map.of("customer", createCustomerUseCase.execute(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RestResponse<Map<String, CustomerView>>> updateCustomer(
            @PathVariable UUID id, @Valid @RequestBody CustomerRequest request) {
        return RestResponseUtils.successResponse(Map.of("customer", updateCustomerUseCase.execute(id, request))
        );
    }
    
    @PutMapping(value = "/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<RestResponse<Map<String, CustomerView>>> updateProfile(
            @ModelAttribute CustomerProfileUpdateRequest request) {
        return RestResponseUtils.successResponse(Map.of("customer", updateCustomerProfileUseCase.execute(request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RestResponse<Void>> deleteCustomer(@PathVariable UUID id) {
        deleteCustomerUseCase.execute(id);
        return RestResponseUtils.successResponse(null);
    }

    @PostMapping("/{id}/tags")
    public ResponseEntity<RestResponse<Map<String, CustomerView>>> addCustomerTags(
            @PathVariable UUID id, @Valid @RequestBody CustomerTagsRequest request) {
        return RestResponseUtils.successResponse(Map.of("customer", addCustomerTagsUseCase.execute(id, request))
        );
    }

    @DeleteMapping("/{id}/tags")
    public ResponseEntity<RestResponse<Map<String, CustomerView>>> deleteCustomerTags(
            @PathVariable UUID id,
            @RequestBody(required = false) CustomerTagsRequest request) {
        return RestResponseUtils.successResponse(Map.of("customer", deleteCustomerTagsUseCase.execute(id, request)));
    }
}
