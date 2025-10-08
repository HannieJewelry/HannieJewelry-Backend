package hanniejewelry.vn.customer.controller;

import hanniejewelry.vn.shared.utils.RestResponseUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import hanniejewelry.vn.customer.dto.CustomerAddressRequest;
import hanniejewelry.vn.customer.usecase.address.*;
import hanniejewelry.vn.customer.view.CustomerAddressView;
import hanniejewelry.vn.shared.dto.GenericFilterRequest;
import hanniejewelry.vn.shared.dto.RestResponse;
import hanniejewelry.vn.shared.dto.PagedResponse;
import hanniejewelry.vn.shared.utils.ResponseUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;
import hanniejewelry.vn.shared.constants.ApiConstants;

@RestController
@RequestMapping(ApiConstants.API_PREFIX + "/customers/{customerId}/addresses")
@RequiredArgsConstructor
public final class CustomerAddressController {

    private final GetAllCustomerAddressesWithBlazeFilterUseCase getAllCustomerAddressesWithBlazeFilterUseCase;
    private final GetCustomerAddressByIdUseCase getCustomerAddressByIdUseCase;
    private final CreateCustomerAddressUseCase createCustomerAddressUseCase;
    private final UpdateCustomerAddressUseCase updateCustomerAddressUseCase;
    private final DeleteCustomerAddressUseCase deleteCustomerAddressUseCase;
    private final SetDefaultAddressUseCase setDefaultAddressUseCase;

    @GetMapping
    public ResponseEntity<RestResponse<PagedResponse<CustomerAddressView>>> getCustomerAddresses(
            @PathVariable UUID customerId,
            @ModelAttribute GenericFilterRequest filter) {
        var addresses = getAllCustomerAddressesWithBlazeFilterUseCase.execute(customerId, filter);
        return RestResponseUtils.successResponse(
                ResponseUtils.pagedSuccess(addresses, filter)
        );
    }

    @GetMapping("/{addressId}")
    public ResponseEntity<RestResponse<Map<String, CustomerAddressView>>> getCustomerAddressById(
            @PathVariable UUID customerId, @PathVariable UUID addressId) {
        return RestResponseUtils.successResponse(
                Map.of("address", getCustomerAddressByIdUseCase.execute(customerId, addressId))
        );
    }

    @PostMapping
    public ResponseEntity<RestResponse<Map<String, CustomerAddressView>>> createCustomerAddress(
            @PathVariable UUID customerId, @Valid @RequestBody Map<String, CustomerAddressRequest> requestWrapper) {
        CustomerAddressRequest request = requestWrapper.get("address");
        return RestResponseUtils.createdResponse(
                Map.of("address", createCustomerAddressUseCase.execute(customerId, request))
        );
    }

    @PutMapping("/{addressId}")
    public ResponseEntity<RestResponse<Map<String, CustomerAddressView>>> updateCustomerAddress(
            @PathVariable UUID customerId, 
            @PathVariable UUID addressId, 
            @Valid @RequestBody Map<String, CustomerAddressRequest> requestWrapper) {
        CustomerAddressRequest request = requestWrapper.get("address");
        return RestResponseUtils.successResponse(
                Map.of("address", updateCustomerAddressUseCase.execute(customerId, addressId, request))
        );
    }

    @DeleteMapping("/{addressId}")
    public ResponseEntity<RestResponse<Void>> deleteCustomerAddress(
            @PathVariable UUID customerId, @PathVariable UUID addressId) {
        deleteCustomerAddressUseCase.execute(customerId, addressId);
        return RestResponseUtils.successResponse(null);
    }

    @PutMapping("/{addressId}/default")
    public ResponseEntity<RestResponse<Map<String, CustomerAddressView>>> setDefaultAddress(
            @PathVariable UUID customerId, @PathVariable UUID addressId) {
        return RestResponseUtils.successResponse(
                Map.of("address", setDefaultAddressUseCase.execute(customerId, addressId))
        );
    }
}