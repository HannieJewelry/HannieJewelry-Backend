package hanniejewelry.vn.customer.view;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import hanniejewelry.vn.customer.entity.EInvoiceInfo;

import java.util.UUID;

@EntityView(EInvoiceInfo.class)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public interface EInvoiceInfoView {

    @IdMapping
    UUID getId();

    String getName();

    String getTaxCode();

    String getEmail();

    String getAddress();

    Boolean getIsCompany();
} 