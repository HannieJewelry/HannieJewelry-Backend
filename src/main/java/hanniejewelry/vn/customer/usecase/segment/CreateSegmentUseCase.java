package hanniejewelry.vn.customer.usecase.segment;

import hanniejewelry.vn.customer.dto.SegmentRequest;
import hanniejewelry.vn.customer.entity.CustomerSegment;
import hanniejewelry.vn.customer.repository.CustomerSegmentRepository;
import hanniejewelry.vn.customer.view.CustomerSegmentView;
import hanniejewelry.vn.shared.exception.BizException;
import hanniejewelry.vn.shared.exception.BaseMessageType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CreateSegmentUseCase {

    private final CustomerSegmentRepository segmentRepository;
    private final GetSegmentByIdUseCase getSegmentByIdUseCase;

    public CustomerSegmentView execute(SegmentRequest request) {
        // Check if segment name already exists
        if (segmentRepository.findByName(request.getName()).isPresent()) {
            throw new BizException(BaseMessageType.ENTITY_ALREADY_EXISTS, "Segment name already exists");
        }

        CustomerSegment segment = CustomerSegment.builder()
                .name(request.getName())
                .description(request.getDescription())
                .type(request.getType())
                .definition(request.getDefinition())
                .build();

        CustomerSegment savedSegment = segmentRepository.save(segment);
        log.info("Created new segment with ID: {}", savedSegment.getId());

        return getSegmentByIdUseCase.execute(savedSegment.getId());
    }
} 