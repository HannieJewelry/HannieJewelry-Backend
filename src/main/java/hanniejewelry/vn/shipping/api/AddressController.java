package hanniejewelry.vn.shipping.api;

import hanniejewelry.vn.shared.dto.RestResponse;
import lombok.RequiredArgsConstructor;
import hanniejewelry.vn.shared.utils.RestResponseUtils;
import hanniejewelry.vn.shipping.application.dto.*;
import hanniejewelry.vn.shipping.application.usecase.address.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import static hanniejewelry.vn.shared.utils.RestResponseUtils.handleSync;
import hanniejewelry.vn.shared.constants.ApiConstants;

@RestController
@RequestMapping(ApiConstants.API_PREFIX)
@RequiredArgsConstructor
public class AddressController {
    private final GetCountryWithProvincesByIdUseCase getCountryWithProvincesByIdUseCase;
    private final GetAllCountriesUseCase getAllCountriesUseCase;
    private final GetDistrictsResponseUseCase getDistrictsResponseUseCase;
    private final GetWardsResponseUseCase getWardsResponseUseCase;
    private final SyncAddressDataUseCase syncAddressDataUseCase;

    @GetMapping("/countries")
    public ResponseEntity<RestResponse<CountriesListResponseDTO>> getCountries() {
        return RestResponseUtils.successResponse(
                getAllCountriesUseCase.execute()
        );
    }

    @GetMapping("/countries/{id}/provinces")
    public ResponseEntity<RestResponse<CountryResponseDTO>> getCountryWithProvinces(@PathVariable Integer id) {
        return RestResponseUtils.successResponse(
                getCountryWithProvincesByIdUseCase.execute(id)
        );
    }


    @GetMapping("/districts")
    public ResponseEntity<RestResponse<DistrictsResponseDTO>> getDistricts(
            @RequestParam(value = "province_id") Integer provinceId) {
        return RestResponseUtils.successResponse(
                getDistrictsResponseUseCase.execute(provinceId)
        );
    }

    @GetMapping("/wards")
    public ResponseEntity<RestResponse<WardsResponseDTO>> getWards(
            @RequestParam(value = "district_id") Integer districtId) {
        return RestResponseUtils.successResponse(
                getWardsResponseUseCase.execute(districtId)
        );
    }

    @PostMapping("address/sync")
    public ResponseEntity<RestResponse<String>> syncAllAddressData() {
        return handleSync(syncAddressDataUseCase::execute, "Đã đồng bộ hoàn tất toàn bộ dữ liệu địa chỉ từ Haravan (Countries/Provinces/Districts/Wards)!");
    }
}
