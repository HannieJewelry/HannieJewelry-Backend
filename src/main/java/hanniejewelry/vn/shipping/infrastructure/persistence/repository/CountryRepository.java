package hanniejewelry.vn.shipping.infrastructure.persistence.repository;

import hanniejewelry.vn.shipping.infrastructure.persistence.entity.CountryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CountryRepository extends JpaRepository<CountryEntity, Integer> {
    Optional<CountryEntity> findByCode(String code);
} 