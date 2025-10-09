package hanniejewelry.vn.shipping.domain.repository;

import hanniejewelry.vn.shipping.domain.model.District;

import java.util.List;
import java.util.Optional;

public interface DistrictRepositoryPort {
    List<District> findAll();
    List<District> findAllByProvinceId(Integer provinceId);
    Optional<District> getDistrictById(Integer id);
    Optional<District> findByCode(String code);
    District save(District district);
}