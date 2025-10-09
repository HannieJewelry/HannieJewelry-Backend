package hanniejewelry.vn.shipping.infrastructure.persistence.repository.adapter;

import lombok.RequiredArgsConstructor;
import hanniejewelry.vn.shipping.domain.model.Province;
import hanniejewelry.vn.shipping.domain.repository.ProvinceRepositoryPort;
import hanniejewelry.vn.shipping.infrastructure.persistence.mapper.ProvinceEntityMapper;
import hanniejewelry.vn.shipping.infrastructure.persistence.repository.ProvinceRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProvinceRepositoryAdapter implements ProvinceRepositoryPort {

    private final ProvinceRepository provinceRepository;
    private final ProvinceEntityMapper provinceEntityMapper;

    @Override
    public List<Province> findAll() {
        return provinceEntityMapper.toDomainList(provinceRepository.findAll());
    }

    @Override
    public List<Province> findByCountryId(Integer countryId) {
        return provinceEntityMapper.toDomainList(provinceRepository.findByCountryId(countryId));
    }

    @Override
    public Optional<Province> getProvinceById(Integer id) {
        return provinceRepository.findById(id)
                .map(provinceEntityMapper::toDomain);
    }
    
    @Override
    public Optional<Province> findByCode(String code) {
        return provinceRepository.findByCode(code)
                .map(provinceEntityMapper::toDomain);
    }

    @Override
    public Province save(Province province) {
        return provinceEntityMapper.toDomain(
                provinceRepository.save(
                        provinceEntityMapper.toEntity(province)
                )
        );
    }
}