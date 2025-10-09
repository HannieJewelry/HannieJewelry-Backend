package hanniejewelry.vn.shipping.infrastructure.persistence.repository.adapter;

import lombok.RequiredArgsConstructor;
import hanniejewelry.vn.shipping.domain.model.Ward;
import hanniejewelry.vn.shipping.domain.repository.WardRepositoryPort;
import hanniejewelry.vn.shipping.infrastructure.persistence.mapper.WardEntityMapper;
import hanniejewelry.vn.shipping.infrastructure.persistence.repository.WardRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class WardRepositoryAdapter implements WardRepositoryPort {

    private final WardRepository wardRepository;
    private final WardEntityMapper wardEntityMapper;

    @Override
    public List<Ward> findAll() {
        return wardEntityMapper.toDomainList(wardRepository.findAll());
    }

    @Override
    public List<Ward> findAllByDistrictId(Integer districtId) {
        return wardEntityMapper.toDomainList(wardRepository.findAllByDistrictId(districtId));
    }

    @Override
    public Optional<Ward> getWardById(String id) {
        return wardRepository.findById(id)
                .map(wardEntityMapper::toDomain);
    }
    
    @Override
    public Optional<Ward> findByCode(String code) {
        return wardRepository.findByCode(code)
                .map(wardEntityMapper::toDomain);
    }

    @Override
    public Ward save(Ward ward) {
        return wardEntityMapper.toDomain(
                wardRepository.save(
                        wardEntityMapper.toEntity(ward)
                )
        );
    }
}