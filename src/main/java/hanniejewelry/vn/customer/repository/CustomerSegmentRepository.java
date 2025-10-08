package hanniejewelry.vn.customer.repository;

import hanniejewelry.vn.customer.entity.CustomerSegment;
import hanniejewelry.vn.shared.infrastructure.impl.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerSegmentRepository extends BaseRepository<CustomerSegment, Long> {
    
    Optional<CustomerSegment> findByName(String name);
    
}