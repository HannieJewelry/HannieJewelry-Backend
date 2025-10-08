package hanniejewelry.vn.shipping.application.usecase.address;

import lombok.RequiredArgsConstructor;
import hanniejewelry.vn.shipping.application.dto.DistrictsResponseDTO;
import hanniejewelry.vn.shipping.application.mapper.address.AddressResponseMapper;
import hanniejewelry.vn.shipping.domain.repository.DistrictRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetDistrictsResponseUseCase {

    private final DistrictRepositoryPort districtRepository;
    private final AddressResponseMapper addressResponseMapper;

    public DistrictsResponseDTO execute(Integer provinceId) {
        return addressResponseMapper.toDistrictsResponseFromDomain(districtRepository.findAllByProvinceId(provinceId));
    }
}