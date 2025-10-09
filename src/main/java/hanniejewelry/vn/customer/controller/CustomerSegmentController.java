package hanniejewelry.vn.customer.controller;

import hanniejewelry.vn.customer.dto.BulkSegmentRequest;
import hanniejewelry.vn.customer.dto.SegmentRequest;
import hanniejewelry.vn.customer.usecase.segment.*;
import hanniejewelry.vn.customer.view.CustomerSegmentView;
import hanniejewelry.vn.shared.dto.GenericFilterRequest;
import hanniejewelry.vn.shared.dto.RestResponse;
import hanniejewelry.vn.shared.utils.RestResponseUtils;
import hanniejewelry.vn.shared.dto.PagedResponse;
import hanniejewelry.vn.shared.utils.ResponseUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/app/segments")
@RequiredArgsConstructor
public final class CustomerSegmentController {

    private final GetAllSegmentsWithBlazeFilterUseCase getAllSegmentsUseCase;
    private final GetSegmentByIdUseCase getSegmentByIdUseCase;
    private final CreateSegmentUseCase createSegmentUseCase;
    private final UpdateSegmentUseCase updateSegmentUseCase;
    private final DeleteSegmentUseCase deleteSegmentUseCase;
    private final BulkAssignSegmentsUseCase bulkAssignSegmentsUseCase;
    private final CountSegmentsUseCase countSegmentsUseCase;

    @GetMapping
    public ResponseEntity<RestResponse<PagedResponse<CustomerSegmentView>>> getSegments(
            @ModelAttribute GenericFilterRequest filter) {
        return RestResponseUtils.successResponse(
                ResponseUtils.pagedSuccess(getAllSegmentsUseCase.execute(filter), filter)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestResponse<Map<String, CustomerSegmentView>>> getSegmentById(@PathVariable Long id) {
        CustomerSegmentView segment = getSegmentByIdUseCase.execute(id);
        return RestResponseUtils.successResponse(Map.of("data", segment));
    }

    @PostMapping
    public ResponseEntity<RestResponse<Map<String, CustomerSegmentView>>> createSegment(
            @Valid @RequestBody SegmentRequest request) {
        CustomerSegmentView segment = createSegmentUseCase.execute(request);
        return RestResponseUtils.createdResponse(Map.of("data", segment));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RestResponse<Map<String, CustomerSegmentView>>> updateSegment(
            @PathVariable Long id, @Valid @RequestBody SegmentRequest request) {
        CustomerSegmentView segment = updateSegmentUseCase.execute(id, request);
        return RestResponseUtils.successResponse(Map.of("data", segment));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RestResponse<Void>> deleteSegment(@PathVariable Long id) {
        deleteSegmentUseCase.execute(id);
        return RestResponseUtils.successResponse(null);
    }

    @PutMapping("/bulk")
    public ResponseEntity<RestResponse<Map<String, String>>> bulkAssignSegments(
            @Valid @RequestBody BulkSegmentRequest request) {
        bulkAssignSegmentsUseCase.execute(request);
        return RestResponseUtils.successResponse(Map.of("message", "Segments assigned successfully"));
    }

    @GetMapping("/count")
    public ResponseEntity<RestResponse<Map<String, Long>>> countSegments() {
        long count = countSegmentsUseCase.execute();
        return RestResponseUtils.successResponse(Map.of("count", count));
    }
} 