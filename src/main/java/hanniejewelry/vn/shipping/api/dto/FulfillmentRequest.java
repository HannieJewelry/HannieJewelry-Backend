package hanniejewelry.vn.shipping.api.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class FulfillmentRequest {
    
    private Long id;
    
    private String name;
    
    private Long packageId;
    
    private String packageName;
    
    private boolean isActived;
    
    private boolean isShowPackageDimension;
    
    private boolean isShowPaymentMethod;
    
    private boolean isShowPaidBy;
    
    private Integer packageHeight;
    
    private Integer packageLength;
    
    private Integer packageWidth;
    
    private boolean isShowCouponCode;
    
    private String carrierDiscountCode;
    
    private boolean isViewBefore;
    
    private boolean isShowViewBefore;
    
    private boolean isShownAllowTest;
    
    private boolean allowTest;
    
    private boolean isViewSenderInfo;
    
    private Integer transportType;
    
    private boolean hasInsurance;
    
    private boolean paidByReceiver;
    
    private boolean isPickupTime;
    
    private boolean isPickupDateTime;
    
    private boolean dropOff;
    
    private boolean partSign;
    
    private BigDecimal codFee;
    
    private BigDecimal fee;
    
    private boolean isInsurance;
    
    private BigDecimal insurancePrice;
    
    private BigDecimal failedDeliveryFee;
} 