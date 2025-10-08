package hanniejewelry.vn.shipping.application.mapper.address;

import hanniejewelry.vn.shipping.application.dto.CountryResponseDTO;
import hanniejewelry.vn.shipping.application.dto.CountriesListResponseDTO;
import hanniejewelry.vn.shipping.application.dto.DistrictsResponseDTO;
import hanniejewelry.vn.shipping.application.dto.WardsResponseDTO;
import hanniejewelry.vn.shipping.domain.model.Country;
import hanniejewelry.vn.shipping.domain.model.Province;
import hanniejewelry.vn.shipping.domain.model.District;
import hanniejewelry.vn.shipping.domain.model.Ward;
import org.mapstruct.*;
import java.util.List;

@Mapper(componentModel = "spring")
public interface AddressResponseMapper {

    CountryResponseDTO.ProvinceDetailsDTO toProvinceDetailsFromDomain(Province province);

    DistrictsResponseDTO.DistrictDetailsDTO toDistrictDetailsFromDomain(District district);

    WardsResponseDTO.WardDetailsDTO toWardDetailsFromDomain(Ward ward);

    List<CountryResponseDTO.ProvinceDetailsDTO> toProvinceDetailsFromDomain(List<Province> provinces);

    List<DistrictsResponseDTO.DistrictDetailsDTO> toDistrictDetailsFromDomain(List<District> districts);

    List<WardsResponseDTO.WardDetailsDTO> toWardDetailsFromDomain(List<Ward> wards);

    CountriesListResponseDTO.CountrySimpleDTO toCountrySimpleFromDomain(Country country);

    List<CountriesListResponseDTO.CountrySimpleDTO> toCountrySimpleListFromDomain(List<Country> countries);

    default CountriesListResponseDTO toCountriesListResponseFromDomain(List<Country> countries) {
        return CountriesListResponseDTO.builder()
                .countries(toCountrySimpleListFromDomain(countries))
                .build();
    }

    default CountryResponseDTO toCountryResponseFromDomain(Country country, List<Province> provinces) {
        return CountryResponseDTO.builder()
                .country(CountryResponseDTO.CountryWithProvincesDTO.builder()
                        .id(country.getId())
                        .code(country.getCode())
                        .name(country.getName())
                        .provinces(toProvinceDetailsFromDomain(provinces))
                        .build())
                .build();
    }

    default DistrictsResponseDTO toDistrictsResponseFromDomain(List<District> districts) {
        return DistrictsResponseDTO.builder()
                .districts( toDistrictDetailsFromDomain(districts))
                .build();
    }

    default WardsResponseDTO toWardsResponseFromDomain(List<Ward> wards) {
        return WardsResponseDTO.builder()
                .wards(toWardDetailsFromDomain(wards))
                .build();
    }
}
