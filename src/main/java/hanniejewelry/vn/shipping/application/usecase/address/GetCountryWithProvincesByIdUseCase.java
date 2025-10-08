package hanniejewelry.vn.shipping.application.usecase.address;

import hanniejewelry.vn.shared.exception.BaseMessageType;
import hanniejewelry.vn.shared.exception.BizException;
import hanniejewelry.vn.shipping.application.dto.CountryResponseDTO;
import hanniejewelry.vn.shipping.application.mapper.address.AddressResponseMapper;
import hanniejewelry.vn.shipping.domain.repository.CountryRepositoryPort;
import hanniejewelry.vn.shipping.domain.repository.ProvinceRepositoryPort;
import io.vavr.control.Option;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class GetCountryWithProvincesByIdUseCase {

    private final CountryRepositoryPort countryRepository;
    private final ProvinceRepositoryPort provinceRepository;
    private final AddressResponseMapper addressResponseMapper;

    public CountryResponseDTO execute(Integer countryId) {
        return Option.when(countryRepository.existsById(countryId), countryId)
                .flatMap(id -> Option.ofOptional(countryRepository.getCountryById(id)))
                .map(country -> addressResponseMapper.toCountryResponseFromDomain(
                        country,
                        provinceRepository.findByCountryId(countryId)
                ))
                .getOrElseThrow(() -> new BizException(BaseMessageType.NOT_FOUND, "Country not found with id: " + countryId));
    }


}
