package hanniejewelry.vn.inventory.service;

import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.PagedList;
import com.blazebit.persistence.view.EntityViewManager;
import com.blazebit.persistence.view.EntityViewSetting;
import hanniejewelry.vn.inventory.dto.LocationRequest;
import hanniejewelry.vn.inventory.entity.Location;
import hanniejewelry.vn.inventory.repository.LocationRepository;
import hanniejewelry.vn.inventory.view.LocationView;
import hanniejewelry.vn.shared.exception.BaseMessageType;
import hanniejewelry.vn.shared.exception.BizException;
import hanniejewelry.vn.shared.filter.GenericBlazeFilterApplier;
import hanniejewelry.vn.shared.dto.GenericFilterRequest;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LocationService {

    EntityViewManager evm;
    LocationRepository locationRepository;
    CriteriaBuilderFactory cbf;
    EntityManager em;
    private final GenericBlazeFilterApplier<LocationView> filterApplier = new GenericBlazeFilterApplier<>();

    public PagedList<LocationView> getAllLocationsWithBlazeFilter(GenericFilterRequest filter) {
        var setting = EntityViewSetting.create(LocationView.class, filter.getPage(), filter.getSize());
        filterApplier.applyFilters(setting, filter);

        var cb = cbf.create(em, Location.class);
        filterApplier.applySort(cb, filter);

        return evm.applySetting(setting, cb).getResultList();
    }

    public LocationView getLocationViewById(UUID id) {
        locationRepository.findById(id)
                .orElseThrow(() -> new BizException(BaseMessageType.NOT_FOUND, "Location not found with id: " + id));
        return evm.find(em, LocationView.class, id);
    }

    public LocationView createLocationAndReturnView(LocationRequest request) {
        Location location = Location.builder()
                .name(request.getName())
                .locationName(request.getLocationName())
                .address(request.getAddress())
                .email(request.getEmail())
                .addressCont(request.getAddressCont())
                .phone(request.getPhone())
                .street(request.getStreet())
                .city(request.getCity())
                .countryId(request.getCountryId())
                .provinceId(request.getProvinceId())
                .postalZipCode(request.getPostalZipCode())
                .isPrimaryLocation(false) // Default to false, use setPrimaryLocation method to set as primary
                .isShippingLocation(request.getIsShippingLocation())
                .isUnavailableQty(request.getIsUnavailableQty() != null && request.getIsUnavailableQty())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .districtId(request.getDistrictId())
                .wardId(request.getWardId())
                .holdStock(request.getHoldStock() != null && request.getHoldStock())
                .userIds(request.getUserIds())
                .groupIds(request.getGroupIds())
                .typeId(request.getTypeId())
                .areaId(request.getAreaId())
                .status(request.getStatus() != null ? request.getStatus() : 1)
                .build();
                
        Location saved = locationRepository.save(location);
        locationRepository.flush();
        return evm.find(em, LocationView.class, saved.getId());
    }

    public LocationView updateLocationAndReturnView(UUID id, LocationRequest request) {
        Location existingLocation = locationRepository.findById(id)
                .orElseThrow(() -> new BizException(BaseMessageType.NOT_FOUND, "Location not found with id: " + id));
                
        Location updatedLocation = existingLocation.toBuilder()
                .name(request.getName())
                .locationName(request.getLocationName())
                .address(request.getAddress())
                .email(request.getEmail())
                .addressCont(request.getAddressCont())
                .phone(request.getPhone())
                .street(request.getStreet())
                .city(request.getCity())
                .countryId(request.getCountryId())
                .provinceId(request.getProvinceId())
                .postalZipCode(request.getPostalZipCode())
                .isShippingLocation(request.getIsShippingLocation())
                .isUnavailableQty(request.getIsUnavailableQty() != null && request.getIsUnavailableQty())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .districtId(request.getDistrictId())
                .wardId(request.getWardId())
                .holdStock(request.getHoldStock() != null && request.getHoldStock())
                .userIds(request.getUserIds())
                .groupIds(request.getGroupIds())
                .typeId(request.getTypeId())
                .areaId(request.getAreaId())
                .status(request.getStatus() != null ? request.getStatus() : existingLocation.getStatus())
                .build();
                
        Location saved = locationRepository.save(updatedLocation);
        locationRepository.flush();
        return evm.find(em, LocationView.class, saved.getId());
    }

    public void deleteLocation(UUID id) {
        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new BizException(BaseMessageType.NOT_FOUND, "Location not found with id: " + id));
        
        if (location.isPrimaryLocation()) {
            throw new BizException(BaseMessageType.CONFLICT, "Cannot delete the primary location");
        }
        
        locationRepository.softDeleteById(id);
        locationRepository.flush();
    }

    public LocationView setPrimaryLocation(UUID id) {
        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new BizException(BaseMessageType.NOT_FOUND, "Location not found with id: " + id));
        
        // Find current primary location if exists
        locationRepository.findByIsPrimaryLocationTrue().ifPresent(primaryLocation -> {
            primaryLocation.setPrimaryLocation(false);
            locationRepository.save(primaryLocation);
        });
        
        // Set new primary location
        location.setPrimaryLocation(true);
        Location saved = locationRepository.save(location);
        locationRepository.flush();
        
        return evm.find(em, LocationView.class, saved.getId());
    }

    public List<LocationView> getShippingLocations() {
        var setting = EntityViewSetting.create(LocationView.class);
        var cb = cbf.create(em, Location.class);
        cb.where("isShippingLocation").eq(true);
        
        return evm.applySetting(setting, cb).getResultList();
    }
} 