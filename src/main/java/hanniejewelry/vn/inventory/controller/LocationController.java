package hanniejewelry.vn.inventory.controller;

import hanniejewelry.vn.inventory.dto.LocationRequest;
import hanniejewelry.vn.inventory.service.LocationService;
import hanniejewelry.vn.inventory.view.LocationView;
import hanniejewelry.vn.shared.constants.ApiConstants;
import hanniejewelry.vn.shared.dto.GenericFilterRequest;
import hanniejewelry.vn.shared.dto.RestResponse;
import hanniejewelry.vn.shared.utils.RestResponseUtils;
import hanniejewelry.vn.shared.dto.PagedResponse;
import hanniejewelry.vn.shared.utils.ResponseUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(ApiConstants.API_PREFIX + "/locations")
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;

    @GetMapping
    public ResponseEntity<RestResponse<PagedResponse<LocationView>>> getLocations(
            @ModelAttribute GenericFilterRequest filter) {
        return RestResponseUtils.successResponse(
                ResponseUtils.pagedSuccess(locationService.getAllLocationsWithBlazeFilter(filter), filter)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestResponse<LocationView>> getLocationById(@PathVariable UUID id) {
        return RestResponseUtils.successResponse(locationService.getLocationViewById(id));
    }

    @PostMapping
    public ResponseEntity<RestResponse<LocationView>> createLocation(@Valid @RequestBody LocationRequest request) {
        return RestResponseUtils.createdResponse(locationService.createLocationAndReturnView(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RestResponse<LocationView>> updateLocation(
            @PathVariable UUID id, @Valid @RequestBody LocationRequest request) {
        return RestResponseUtils.successResponse(locationService.updateLocationAndReturnView(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RestResponse<Void>> deleteLocation(@PathVariable UUID id) {
        locationService.deleteLocation(id);
        return RestResponseUtils.successResponse(null);
    }

    @PutMapping("/{id}/primary")
    public ResponseEntity<RestResponse<LocationView>> setPrimaryLocation(@PathVariable UUID id) {
        return RestResponseUtils.successResponse(locationService.setPrimaryLocation(id));
    }

    @GetMapping("/shipping")
    public ResponseEntity<RestResponse<List<LocationView>>> getShippingLocations() {
        return RestResponseUtils.successResponse(locationService.getShippingLocations());
    }
} 