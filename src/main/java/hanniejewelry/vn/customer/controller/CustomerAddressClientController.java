package hanniejewelry.vn.customer.controller;

import hanniejewelry.vn.shared.dto.RestResponse;
import hanniejewelry.vn.shared.utils.RestResponseUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import hanniejewelry.vn.customer.dto.CustomerAddressRequest;
import hanniejewelry.vn.customer.usecase.address.*;
import hanniejewelry.vn.customer.view.CustomerAddressView;
import hanniejewelry.vn.shared.dto.GenericFilterRequest;
import hanniejewelry.vn.shared.dto.PagedResponse;
import hanniejewelry.vn.shared.utils.ResponseUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import hanniejewelry.vn.shared.constants.ApiConstants;

@RestController
@RequestMapping(ApiConstants.API_PREFIX + "/client/addresses")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Customer Addresses", description = "API để quản lý địa chỉ của khách hàng")
public class CustomerAddressClientController {

    private final GetAllCustomerAddressesByCurrentUserUseCase getAllCustomerAddressesByCurrentUserUseCase;
    private final GetCustomerAddressByIdForCurrentUserUseCase getCustomerAddressByIdForCurrentUserUseCase;
    private final CreateCustomerAddressForCurrentUserUseCase createCustomerAddressForCurrentUserUseCase;
    private final UpdateCustomerAddressForCurrentUserUseCase updateCustomerAddressForCurrentUserUseCase;
    private final DeleteCustomerAddressForCurrentUserUseCase deleteCustomerAddressForCurrentUserUseCase;
    private final SetDefaultAddressForCurrentUserUseCase setDefaultAddressForCurrentUserUseCase;

    @GetMapping
    @Operation(summary = "Lấy danh sách địa chỉ của khách hàng hiện tại", description = "Trả về danh sách địa chỉ của khách hàng đã đăng nhập")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Thành công", content = @Content(schema = @Schema(implementation = PagedResponse.class))),
            @ApiResponse(responseCode = "401", description = "Chưa xác thực", content = @Content),
            @ApiResponse(responseCode = "403", description = "Không có quyền truy cập", content = @Content)
    })
    public ResponseEntity<RestResponse<PagedResponse<CustomerAddressView>>> getCustomerAddresses(
            @Parameter(description = "Thông tin phân trang và lọc") @ModelAttribute GenericFilterRequest filter) {
        var addresses = getAllCustomerAddressesByCurrentUserUseCase.execute(filter);
        return RestResponseUtils.successResponse(
                ResponseUtils.pagedSuccess(addresses, filter)
        );
    }

    @GetMapping("/{addressId}")
    @Operation(summary = "Lấy thông tin chi tiết địa chỉ", description = "Trả về thông tin chi tiết của một địa chỉ cụ thể")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Thành công"),
            @ApiResponse(responseCode = "401", description = "Chưa xác thực", content = @Content),
            @ApiResponse(responseCode = "403", description = "Không có quyền truy cập", content = @Content),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy địa chỉ", content = @Content)
    })
    public ResponseEntity<RestResponse<Map<String, CustomerAddressView>>> getCustomerAddressById(
            @Parameter(description = "ID của địa chỉ") @PathVariable UUID addressId) {
        return RestResponseUtils.successResponse(
                Map.of("address", getCustomerAddressByIdForCurrentUserUseCase.execute(addressId))
        );
    }

    @PostMapping
    @Operation(summary = "Tạo địa chỉ mới", description = "Tạo một địa chỉ mới cho khách hàng hiện tại")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Đã tạo thành công"),
            @ApiResponse(responseCode = "400", description = "Dữ liệu không hợp lệ", content = @Content),
            @ApiResponse(responseCode = "401", description = "Chưa xác thực", content = @Content),
            @ApiResponse(responseCode = "403", description = "Không có quyền truy cập", content = @Content)
    })
    public ResponseEntity<RestResponse<Map<String, CustomerAddressView>>> createCustomerAddress(
            @Parameter(description = "Thông tin địa chỉ mới") @Valid @RequestBody Map<String, CustomerAddressRequest> requestWrapper) {
        CustomerAddressRequest request = requestWrapper.get("address");
        return RestResponseUtils.createdResponse(
                Map.of("address", createCustomerAddressForCurrentUserUseCase.execute(request))
        );
    }

    @PutMapping("/{addressId}")
    @Operation(summary = "Cập nhật địa chỉ", description = "Cập nhật thông tin của một địa chỉ hiện có")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cập nhật thành công"),
            @ApiResponse(responseCode = "400", description = "Dữ liệu không hợp lệ", content = @Content),
            @ApiResponse(responseCode = "401", description = "Chưa xác thực", content = @Content),
            @ApiResponse(responseCode = "403", description = "Không có quyền truy cập", content = @Content),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy địa chỉ", content = @Content)
    })
    public ResponseEntity<RestResponse<Map<String, CustomerAddressView>>> updateCustomerAddress(
            @Parameter(description = "ID của địa chỉ") @PathVariable UUID addressId, 
            @Parameter(description = "Thông tin cập nhật") @Valid @RequestBody Map<String, CustomerAddressRequest> requestWrapper) {
        CustomerAddressRequest request = requestWrapper.get("address");
        return RestResponseUtils.successResponse(
                Map.of("address", updateCustomerAddressForCurrentUserUseCase.execute(addressId, request))
        );
    }

    @DeleteMapping("/{addressId}")
    @Operation(summary = "Xóa địa chỉ", description = "Xóa một địa chỉ hiện có")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Xóa thành công"),
            @ApiResponse(responseCode = "401", description = "Chưa xác thực", content = @Content),
            @ApiResponse(responseCode = "403", description = "Không có quyền truy cập", content = @Content),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy địa chỉ", content = @Content)
    })
    public ResponseEntity<RestResponse<Void>> deleteCustomerAddress(
            @Parameter(description = "ID của địa chỉ") @PathVariable UUID addressId) {
        deleteCustomerAddressForCurrentUserUseCase.execute(addressId);
        return RestResponseUtils.successResponse(null);
    }

    @PutMapping("/{addressId}/default")
    @Operation(summary = "Đặt địa chỉ mặc định", description = "Đặt một địa chỉ làm địa chỉ mặc định")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cập nhật thành công"),
            @ApiResponse(responseCode = "401", description = "Chưa xác thực", content = @Content),
            @ApiResponse(responseCode = "403", description = "Không có quyền truy cập", content = @Content),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy địa chỉ", content = @Content)
    })
    public ResponseEntity<RestResponse<Map<String, CustomerAddressView>>> setDefaultAddress(
            @Parameter(description = "ID của địa chỉ") @PathVariable UUID addressId) {
        return RestResponseUtils.successResponse(
                Map.of("address", setDefaultAddressForCurrentUserUseCase.execute(addressId))
        );
    }
} 