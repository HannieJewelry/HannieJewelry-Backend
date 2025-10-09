package hanniejewelry.vn.customer.repository;

import hanniejewelry.vn.customer.entity.CustomerAddress;
import hanniejewelry.vn.shared.infrastructure.impl.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerAddressRepository extends BaseRepository<CustomerAddress, UUID> {
    
    List<CustomerAddress> findByCustomerId(UUID customerId);
    
    Optional<CustomerAddress> findByCustomerIdAndId(UUID customerId, UUID addressId);
    
    Optional<CustomerAddress> findByCustomerIdAndIsDefaultTrue(UUID customerId);
    

    @Modifying
    @Query("UPDATE CustomerAddress a SET a.isDeleted = true WHERE a.id = :id")
    void softDeleteById(@Param("id") UUID id);

}