package hanniejewelry.vn.customer.usecase.segment;

import hanniejewelry.vn.customer.entity.CustomerSegment;
import hanniejewelry.vn.customer.repository.CustomerSegmentRepository;
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
public class DeleteSegmentUseCase {

    private final CustomerSegmentRepository segmentRepository;

    public void execute(Long id) {
        CustomerSegment segment = segmentRepository.findById(id)
                .orElseThrow(() -> new BizException(BaseMessageType.NOT_FOUND, "Segment not found"));

        segmentRepository.delete(segment);
        log.info("Deleted segment with ID: {}", id);
    }
} 