package hanniejewelry.vn.shipping.application.usecase.address;

import lombok.RequiredArgsConstructor;
import hanniejewelry.vn.shipping.application.dto.WardsResponseDTO;
import hanniejewelry.vn.shipping.application.mapper.address.AddressResponseMapper;
import hanniejewelry.vn.shipping.domain.model.Ward;
import hanniejewelry.vn.shipping.domain.repository.WardRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetWardsResponseUseCase {

    private final WardRepositoryPort wardRepository;
    private final AddressResponseMapper addressResponseMapper;

    public WardsResponseDTO execute(Integer districtId) {
        return addressResponseMapper.toWardsResponseFromDomain(wardRepository.findAllByDistrictId(districtId));
    }
}