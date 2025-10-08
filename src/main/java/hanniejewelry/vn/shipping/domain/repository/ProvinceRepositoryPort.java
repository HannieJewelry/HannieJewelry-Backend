package hanniejewelry.vn.shipping.domain.repository;

import hanniejewelry.vn.shipping.domain.model.Province;

import java.util.List;
import java.util.Optional;

public interface ProvinceRepositoryPort {
    List<Province> findAll();
    List<Province> findByCountryId(Integer countryId);
    Optional<Province> getProvinceById(Integer id);
    Optional<Province> findByCode(String code);
    Province save(Province province);
}