package hanniejewelry.vn.shipping.application.usecase.address;

import lombok.RequiredArgsConstructor;
import hanniejewelry.vn.shipping.application.dto.CountriesListResponseDTO;
import hanniejewelry.vn.shipping.application.mapper.address.AddressResponseMapper;
import hanniejewelry.vn.shipping.domain.repository.CountryRepositoryPort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetAllCountriesUseCase {

    private final CountryRepositoryPort countryRepository;
    private final AddressResponseMapper addressResponseMapper;

    public CountriesListResponseDTO execute() {
        return addressResponseMapper.toCountriesListResponseFromDomain(
                countryRepository.findAll()
        );
    }
} 