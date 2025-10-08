package hanniejewelry.vn.customer.view;

import com.blazebit.persistence.view.AttributeFilter;
import com.blazebit.persistence.view.AttributeFilters;
import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;
import com.blazebit.persistence.view.filter.*;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import hanniejewelry.vn.customer.entity.Customer;
import hanniejewelry.vn.customer.enums.Gender;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@EntityView(Customer.class)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public interface CustomerView {

    @IdMapping
    UUID getId();

    @AttributeFilters({
            @AttributeFilter(value = EqualFilter.class, name = "eq")
    })
    Boolean getAcceptsMarketing();

    @AttributeFilters({
            @AttributeFilter(value = EqualFilter.class, name = "eq"),
            @AttributeFilter(value = ContainsIgnoreCaseFilter.class, name = "like")
    })
    String getEmail();

    @AttributeFilters({
            @AttributeFilter(value = EqualFilter.class, name = "eq"),
            @AttributeFilter(value = ContainsIgnoreCaseFilter.class, name = "like")
    })
    String getPhone();

    @AttributeFilters({
            @AttributeFilter(value = ContainsIgnoreCaseFilter.class, name = "like")
    })
    String getFirstName();

    @AttributeFilters({
            @AttributeFilter(value = ContainsIgnoreCaseFilter.class, name = "like")
    })
    String getLastName();
    
    String getAvatarUrl();

    UUID getLastOrderId();

    String getLastOrderName();

    String getNote();

    @AttributeFilters({
            @AttributeFilter(value = EqualFilter.class, name = "eq"),
            @AttributeFilter(value = GreaterThanFilter.class, name = "gt"),
            @AttributeFilter(value = LessThanFilter.class, name = "lt")
    })
    Integer getOrdersCount();



    String getTags();

    @AttributeFilters({
            @AttributeFilter(value = GreaterThanFilter.class, name = "gt"),
            @AttributeFilter(value = LessThanFilter.class, name = "lt")
    })
    BigDecimal getTotalSpent();

    @AttributeFilters({
            @AttributeFilter(value = GreaterThanFilter.class, name = "gt"),
            @AttributeFilter(value = LessThanFilter.class, name = "lt")
    })
    BigDecimal getTotalPaid();



    String getGroupName();

    @AttributeFilters({
            @AttributeFilter(value = GreaterThanFilter.class, name = "gt"),
            @AttributeFilter(value = LessThanFilter.class, name = "lt")
    })
    Instant getBirthday();

    @AttributeFilters({
            @AttributeFilter(value = EqualFilter.class, name = "eq")
    })
    Gender getGender();

    @AttributeFilters({
            @AttributeFilter(value = GreaterThanFilter.class, name = "gt"),
            @AttributeFilter(value = LessThanFilter.class, name = "lt")
    })
    Instant getLastOrderDate();

    @AttributeFilters({
            @AttributeFilter(value = GreaterThanFilter.class, name = "gt"),
            @AttributeFilter(value = LessThanFilter.class, name = "lt")
    })
    Instant getCreatedAt();

    @AttributeFilters({
            @AttributeFilter(value = GreaterThanFilter.class, name = "gt"),
            @AttributeFilter(value = LessThanFilter.class, name = "lt")
    })
    Instant getUpdatedAt();

    List<CustomerAddressView> getAddresses();

    EInvoiceInfoView getEInvoiceInfo();

    Set<CustomerSegmentView> getSegments();
} 