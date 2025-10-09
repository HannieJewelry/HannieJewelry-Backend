package hanniejewelry.vn.shipping.infrastructure.persistence.repository.adapter;

import lombok.RequiredArgsConstructor;
import hanniejewelry.vn.shipping.domain.model.District;
import hanniejewelry.vn.shipping.domain.repository.DistrictRepositoryPort;
import hanniejewelry.vn.shipping.infrastructure.persistence.mapper.DistrictEntityMapper;
import hanniejewelry.vn.shipping.infrastructure.persistence.repository.DistrictRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DistrictRepositoryAdapter implements DistrictRepositoryPort {

    private final DistrictRepository districtRepository;
    private final DistrictEntityMapper districtEntityMapper;

    @Override
    public List<District> findAll() {
        return districtEntityMapper.toDomainList(districtRepository.findAll());
    }

    @Override
    public List<District> findAllByProvinceId(Integer provinceId) {
        return districtEntityMapper.toDomainList(districtRepository.findAllByProvinceId(provinceId));
    }

    @Override
    public Optional<District> getDistrictById(Integer id) {
        return districtRepository.findById(id)
                .map(districtEntityMapper::toDomain);
    }
    
    @Override
    public Optional<District> findByCode(String code) {
        return districtRepository.findByCode(code)
                .map(districtEntityMapper::toDomain);
    }

    @Override
    public District save(District district) {
        return districtEntityMapper.toDomain(
                districtRepository.save(
                        districtEntityMapper.toEntity(district)
                )
        );
    }
}