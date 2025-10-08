package hanniejewelry.vn.shipping.infrastructure.external.client;

import hanniejewelry.vn.shipping.application.dto.ExternalApiResponse;
import hanniejewelry.vn.shipping.application.dto.CountryDTO;
import hanniejewelry.vn.shipping.application.dto.DistrictDTO;
import hanniejewelry.vn.shipping.application.dto.WardDTO;
import hanniejewelry.vn.shared.utils.ExternalApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class HaravanApiClient {

    @Value("${haravan.access-token}")
    private String accessToken;

    private final ExternalApiClient externalApiClient;

    private static final String HARAVAN_BASE_URL = "https://apis.haravan.com/com";


    public ExternalApiResponse<List<CountryDTO>> getCountries() {
        return externalApiClient.get(
                HARAVAN_BASE_URL + "/countries.json",
                Map.of("Authorization", "Bearer " + accessToken),
                new ParameterizedTypeReference<>() {}
        );
    }

    public ExternalApiResponse<CountryDTO> getCountryWithProvinces(Integer country_id) {
        return externalApiClient.get(
                HARAVAN_BASE_URL + "/countries/" + country_id + ".json",
                Map.of("Authorization", "Bearer " + accessToken),
                new ParameterizedTypeReference<>() {}
        );
    }


    public ExternalApiResponse<List<DistrictDTO>> getDistricts(Integer provinceId) {
        return externalApiClient.get(
                HARAVAN_BASE_URL + "/provinces/" + provinceId + "/districts.json",
                Map.of("Authorization", "Bearer " + accessToken),
                new ParameterizedTypeReference<>() {}
        );
    }

    public ExternalApiResponse<List<WardDTO>> getWards(Integer districtId) {
        return externalApiClient.get(
                HARAVAN_BASE_URL + "/districts/" + districtId + "/wards.json",
                Map.of("Authorization", "Bearer " + accessToken),
                new ParameterizedTypeReference<>() {}
        );
    }

} 