package hanniejewelry.vn.shipping.domain.repository;

import hanniejewelry.vn.shipping.domain.model.Country;

import java.util.List;
import java.util.Optional;

public interface CountryRepositoryPort {
    List<Country> findAll();
    Optional<Country> getCountryById(Integer id);
    Country save(Country country);
    boolean existsById(Integer id);
} 