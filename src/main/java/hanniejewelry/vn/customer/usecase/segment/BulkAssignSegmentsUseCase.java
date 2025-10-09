package hanniejewelry.vn.customer.usecase.segment;

import hanniejewelry.vn.customer.dto.BulkSegmentRequest;
import hanniejewelry.vn.customer.entity.Customer;
import hanniejewelry.vn.customer.entity.CustomerSegment;
import hanniejewelry.vn.customer.repository.CustomerRepository;
import hanniejewelry.vn.customer.repository.CustomerSegmentRepository;
import hanniejewelry.vn.shared.exception.BizException;
import hanniejewelry.vn.shared.exception.BaseMessageType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class BulkAssignSegmentsUseCase {

    private final CustomerRepository customerRepository;
    private final CustomerSegmentRepository segmentRepository;

    public void execute(BulkSegmentRequest request) {
        Customer customer = customerRepository.findById(request.getBulkSet().getCustomerId())
                .orElseThrow(() -> new BizException(BaseMessageType.NOT_FOUND, "Customer not found"));

        List<Long> segmentIds = Arrays.asList(request.getBulkSet().getSegmentIds());
        
        List<CustomerSegment> segments = segmentRepository.findAllById(segmentIds);
        
        if (segments.size() != segmentIds.size()) {
            throw new BizException(BaseMessageType.NOT_FOUND, "One or more segments not found");
        }

        customer.getSegments().clear();
        customer.getSegments().addAll(segments);
        
        customerRepository.save(customer);
        log.info("Bulk assigned {} segments to customer {}", segments.size(), customer.getId());
    }
} 