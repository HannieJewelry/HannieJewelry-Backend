package hanniejewelry.vn.inventory.view;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import hanniejewelry.vn.inventory.entity.Location;

import java.util.List;
import java.util.UUID;

@EntityView(Location.class)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public interface LocationView {

    @IdMapping
    UUID getId();
    
    String getName();
    
    String getLocationName();
    
    String getAddress();
    
    String getEmail();
    
    String getAddressCont();
    
    String getPhone();
    
    String getStreet();
    
    String getCity();
    
    Integer getCountryId();
    
    String getCountryName();
    
    Integer getProvinceId();
    
    String getProvinceName();
    
    String getPostalZipCode();
    
    boolean getIsPrimaryLocation();
    
    boolean getIsShippingLocation();
    
    boolean getIsUnavailableQty();
    
    Double getLatitude();
    
    Double getLongitude();
    
    Integer getDistrictId();
    
    Integer getWardId();
    
    String getWardName();
    
    String getWardCode();
    
    String getAddressDisplay();
    
    String getDistrictName();
    
    boolean getHoldStock();
    
    String getFlexShipHubCode();
    
    String getProShipHubCode();
    
    String getGhtkHubCode();
    
    String getGhnHubCode();
    
    String getGhNv3HubCode();
    
    String getShipChungHubCode();
    
    String getViettelPostHubCode();
    
    String getDhlHubCode();
    
    String getViettelPost2018HubCode();
    
    List<Long> getUserIds();
    
    List<Long> getGroupIds();
    
    Integer getTypeId();
    
    Integer getAreaId();
    
    String getAreaName();
    
    Integer getStatus();
} 