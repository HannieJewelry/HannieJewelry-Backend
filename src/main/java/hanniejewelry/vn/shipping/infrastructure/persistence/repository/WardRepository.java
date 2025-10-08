package hanniejewelry.vn.shipping.infrastructure.persistence.repository;

import hanniejewelry.vn.shipping.infrastructure.persistence.entity.WardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WardRepository extends JpaRepository<WardEntity, String> {
    List<WardEntity> findAllByDistrictId(Integer districtId);
    Optional<WardEntity> findByCode(String code);
}