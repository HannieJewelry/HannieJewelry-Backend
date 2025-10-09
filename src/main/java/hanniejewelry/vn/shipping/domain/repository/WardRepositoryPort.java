package hanniejewelry.vn.shipping.domain.repository;

import hanniejewelry.vn.shipping.domain.model.Ward;

import java.util.List;
import java.util.Optional;

public interface WardRepositoryPort {
    List<Ward> findAll();
    List<Ward> findAllByDistrictId(Integer districtId);
    Optional<Ward> getWardById(String id);
    Optional<Ward> findByCode(String code);
    Ward save(Ward ward);
}