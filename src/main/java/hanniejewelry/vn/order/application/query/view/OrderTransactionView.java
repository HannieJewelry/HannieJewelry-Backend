package hanniejewelry.vn.order.application.query.view;

import com.blazebit.persistence.view.*;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import hanniejewelry.vn.order.domain.model.entity.OrderTransaction;

import java.math.BigDecimal;
import java.util.UUID;

@EntityView(OrderTransaction.class)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public interface OrderTransactionView {
    @IdMapping
    UUID getId();
    BigDecimal getAmount();
    String getAuthCode();
    String getClientId();
    UUID getCreatedUser();
    String getCurrency();
    String getDeviceId();
    String getExternalTransactionId();
    String getGateway();
    String getHaravanTransactionId();
    String getKind();
    UUID getLocationId();
    UUID getParentId();
    String getPaymentDetails();
    UUID getPaymentMethodId();
    String getReceipt();
    Boolean getSendEmail();
    String getStatus();
    Boolean getTest();
    Integer getTransactionTypeId();
    UUID getUserId();
}
