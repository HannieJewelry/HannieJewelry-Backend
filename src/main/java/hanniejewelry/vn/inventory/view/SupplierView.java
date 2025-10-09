package hanniejewelry.vn.inventory.view;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import hanniejewelry.vn.inventory.entity.Supplier;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@EntityView(Supplier.class)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public interface SupplierView {

    @IdMapping
    UUID getId();
    
    String getFirstName();
    
    String getLastName();
    
    String getSupplierName();
    
    String getPhone();
    
    String getEmail();
    
    Integer getStatus();
    
    String getZipCode();
    
    Integer getCountryId();
    
    String getLocation();
    
    String getAddress();
    
    Integer getProvinceId();
    
    Integer getDistrictId();
    
    String getCity();
    
    String getCountryName();
    
    String getProvinceName();
    
    String getDistrictName();
    
    Integer getWardId();
    
    String getWardName();
    
    Instant getCreatedDate();
    
    String getCreatedName();
    
    BigDecimal getDebt();
    
    BigDecimal getTotalPurchase();
    
    String getCompany();
    
    String getTaxCode();
    
    String getNotes();
    
    String getTags();
} 