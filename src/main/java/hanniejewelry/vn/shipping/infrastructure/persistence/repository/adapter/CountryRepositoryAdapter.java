package hanniejewelry.vn.shipping.infrastructure.persistence.repository.adapter;

import hanniejewelry.vn.shared.utils.ExistenceChecker;
import hanniejewelry.vn.shipping.infrastructure.persistence.entity.CountryEntity;
import lombok.RequiredArgsConstructor;
import hanniejewelry.vn.shipping.domain.model.Country;
import hanniejewelry.vn.shipping.domain.repository.CountryRepositoryPort;
import hanniejewelry.vn.shipping.infrastructure.persistence.mapper.CountryEntityMapper;
import hanniejewelry.vn.shipping.infrastructure.persistence.repository.CountryRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CountryRepositoryAdapter implements CountryRepositoryPort {

    private final CountryRepository countryRepository;
    private final CountryEntityMapper countryEntityMapper;
    private final ExistenceChecker existenceChecker;
    @Override
    public List<Country> findAll() {
        return countryEntityMapper.toDomainList(countryRepository.findAll());
    }

    @Override
    public Optional<Country> getCountryById(Integer id) {
        return countryRepository.findById(id)
                .map(countryEntityMapper::toDomain);
    }

    @Override
    public Country save(Country country) {
        return countryEntityMapper.toDomain(
                countryRepository.save(
                        countryEntityMapper.toEntity(country)
                )
        );
    }

    @Override
    public boolean existsById(Integer id) {
        return existenceChecker.isExist(CountryEntity.class, Map.of("id", id));
    }
} 