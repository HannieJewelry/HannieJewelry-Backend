package hanniejewelry.vn.customer.usecase.segment;

import hanniejewelry.vn.customer.repository.CustomerSegmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CountSegmentsUseCase {

    private final CustomerSegmentRepository segmentRepository;

    public long execute() {
        return segmentRepository.count();
    }
} 