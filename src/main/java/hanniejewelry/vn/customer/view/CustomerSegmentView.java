package hanniejewelry.vn.customer.view;

import com.blazebit.persistence.view.AttributeFilter;
import com.blazebit.persistence.view.AttributeFilters;
import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;
import com.blazebit.persistence.view.filter.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import hanniejewelry.vn.customer.entity.CustomerSegment;

import java.time.Instant;

@EntityView(CustomerSegment.class)
public interface CustomerSegmentView {

    @IdMapping
    Long getId();

    @AttributeFilters({
            @AttributeFilter(value = EqualFilter.class, name = "eq"),
            @AttributeFilter(value = ContainsIgnoreCaseFilter.class, name = "like")
    })
    String getName();

    @AttributeFilters({
            @AttributeFilter(value = ContainsIgnoreCaseFilter.class, name = "like")
    })
    String getDescription();

    @AttributeFilters({
            @AttributeFilter(value = EqualFilter.class, name = "eq")
    })
    CustomerSegment.SegmentType getType();

    String getDefinition();

    @AttributeFilters({
            @AttributeFilter(value = GreaterThanFilter.class, name = "gt"),
            @AttributeFilter(value = LessThanFilter.class, name = "lt")
    })
    @JsonProperty("updatedDate")
    Instant getUpdatedAt();
} 