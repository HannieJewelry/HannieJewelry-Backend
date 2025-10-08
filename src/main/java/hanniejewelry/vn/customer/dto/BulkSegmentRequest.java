package hanniejewelry.vn.customer.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BulkSegmentRequest {

    @NotNull(message = "Bulk set is required")
    @Valid
    @JsonProperty("bulk_set")
    private BulkSet bulkSet;

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BulkSet {
        
        @NotNull(message = "Customer ID is required")
        @JsonProperty("customer_id")
        private UUID customerId;

        @NotNull(message = "Segment IDs are required")
        @JsonProperty("segment_ids")
        private Long[] segmentIds;
    }
} 