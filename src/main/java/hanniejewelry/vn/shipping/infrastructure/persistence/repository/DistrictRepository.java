package hanniejewelry.vn.shipping.infrastructure.persistence.repository;

import hanniejewelry.vn.shipping.infrastructure.persistence.entity.DistrictEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DistrictRepository extends JpaRepository<DistrictEntity, Integer> {
    List<DistrictEntity> findAllByProvinceId(Integer provinceId);
    Optional<DistrictEntity> findByCode(String code);
}