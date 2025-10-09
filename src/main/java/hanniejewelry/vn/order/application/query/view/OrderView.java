package hanniejewelry.vn.order.application.query.view;

import com.blazebit.persistence.view.*;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import hanniejewelry.vn.order.entity.Order;
import hanniejewelry.vn.order.domain.model.enums.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@EntityView(Order.class)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public interface OrderView {
    @IdMapping
    UUID getId();
    String getOrderCode();
    String getOrderNumber();
    String getName();
    String getEmail();
    BigDecimal getTotalPrice();
    BigDecimal getSubtotalPrice();
    BigDecimal getTotalDiscounts();
    BigDecimal getTotalLineItemsPrice();
    BigDecimal getTotalTax();
    Double getTotalWeight();
    FinancialStatus getFinancialStatus();
    FulfillmentStatus getFulfillmentStatus();
    OrderProcessingStatus getOrderProcessingStatus();
    String getTags();
    String getGateway();
    String getGatewayCode();
    String getNote();
    String getSource();
    String getSourceName();
    String getReferringSite();
    String getLandingSite();
    String getLandingSiteRef();
    Instant getClosedAt();
    Instant getCancelledAt();
    String getCancelledReason();
    ClosedStatus getClosedStatus();
    CancelledStatus getCancelledStatus();
    ConfirmedStatus getConfirmedStatus();
    String getConfirmUser();
    Instant getConfirmedAt();
    UUID getUserId();
    String getDeviceId();
    UUID getLocationId();
    String getLocationName();
    String getContactEmail();
    String getUtmSource();
    String getUtmMedium();
    String getUtmCampaign();
    String getUtmTerm();
    String getUtmContent();
    Boolean getIsDeleted();
    String getCartToken();
    String getCheckoutToken();
    String getToken();
    String getBrowserIp();
    Boolean getBuyerAcceptsMarketing();
    String getCurrency();
    Boolean getTaxesIncluded();
    String getCancelReason();
    String getProcessingMethod();

    // Relationships
    List<OrderLineItemView> getLineItems();
    List<OrderShippingLineView> getShippingLines();
    List<OrderDiscountCodeView> getDiscountCodes();
    List<OrderTransactionView> getTransactions();
    List<OrderNoteAttributeView> getNoteAttributes();
    OrderShippingAddressView getShippingAddress();
    OrderBillingAddressView getBillingAddress();
}
