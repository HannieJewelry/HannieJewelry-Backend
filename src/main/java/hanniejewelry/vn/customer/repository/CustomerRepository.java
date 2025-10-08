package hanniejewelry.vn.customer.repository;

import hanniejewelry.vn.customer.entity.Customer;
import hanniejewelry.vn.shared.infrastructure.impl.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository extends BaseRepository<Customer, UUID> {
    
    Optional<Customer> findByEmail(String email);
    
    Optional<Customer> findByPhone(String phone);

    @Query("SELECT c FROM Customer c LEFT JOIN FETCH c.addresses WHERE c.id = :id")
    Optional<Customer> findByIdWithAddresses(@Param("id") UUID id);

}