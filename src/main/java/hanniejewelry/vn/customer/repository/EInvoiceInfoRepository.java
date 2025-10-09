package hanniejewelry.vn.customer.repository;

import hanniejewelry.vn.customer.entity.EInvoiceInfo;
import hanniejewelry.vn.shared.infrastructure.impl.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface EInvoiceInfoRepository extends BaseRepository<EInvoiceInfo, UUID> {
    Optional<EInvoiceInfo> findByCustomerId(UUID customerId);
} 