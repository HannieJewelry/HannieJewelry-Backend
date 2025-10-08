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
public class UpdateSegmentUseCase {

    private final CustomerSegmentRepository segmentRepository;
    private final GetSegmentByIdUseCase getSegmentByIdUseCase;

    public CustomerSegmentView execute(Long id, SegmentRequest request) {
        CustomerSegment segment = segmentRepository.findById(id)
                .orElseThrow(() -> new BizException(BaseMessageType.NOT_FOUND, "Segment not found"));

        if (!segment.getName().equals(request.getName()) &&
            segmentRepository.findByName(request.getName()).isPresent()) {
            throw new BizException(BaseMessageType.ENTITY_ALREADY_EXISTS, "Segment name already exists");
        }

        segment.setName(request.getName());
        segment.setDescription(request.getDescription());
        segment.setType(request.getType());
        segment.setDefinition(request.getDefinition());

        CustomerSegment updatedSegment = segmentRepository.save(segment);
        log.info("Updated segment with ID: {}", updatedSegment.getId());

        return getSegmentByIdUseCase.execute(updatedSegment.getId());
    }
} 