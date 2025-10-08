package hanniejewelry.vn.shipping.infrastructure.persistence.repository;

import hanniejewelry.vn.shipping.infrastructure.persistence.entity.ProvinceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProvinceRepository extends JpaRepository<ProvinceEntity, Integer> {
    List<ProvinceEntity> findByCountryId(Integer countryId);
    Optional<ProvinceEntity> findByCode(String code);
}