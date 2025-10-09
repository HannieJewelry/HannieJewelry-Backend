package hanniejewelry.vn.shipping.infrastructure.external.client;

import hanniejewelry.vn.shipping.application.dto.ExternalApiResponse;
import hanniejewelry.vn.shipping.application.dto.ProvinceDTO;
import hanniejewelry.vn.shipping.application.dto.DistrictDTO;
import hanniejewelry.vn.shipping.application.dto.WardDTO;
import lombok.RequiredArgsConstructor;
import hanniejewelry.vn.shared.utils.ExternalApiClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class GhnApiClient {

    @Value("${ghn.token}")
    private String ghnToken;
    @Value("${ghn.url.province}")
    private String ghnProvinceUrl;
    @Value("${ghn.url.district}")
    private String ghnDistrictUrl;
    @Value("${ghn.url.ward}")
    private String ghnWardUrl;

    private final ExternalApiClient externalApiClient;

    public ExternalApiResponse<List<ProvinceDTO>> getProvinces() {
        return externalApiClient.get(
                ghnProvinceUrl,
                Map.of("token", ghnToken),
                new ParameterizedTypeReference<>() {}
        );
    }

    public ExternalApiResponse<List<DistrictDTO>> getDistricts(int provinceId) {
        return externalApiClient.post(
                ghnDistrictUrl,
                Map.of("token", ghnToken),
                Map.of("province_id", provinceId),
                new ParameterizedTypeReference<>() {}
        );
    }

    public ExternalApiResponse<List<WardDTO>> getWards(int districtId) {
        return externalApiClient.post(
                ghnWardUrl,
                Map.of("token", ghnToken),
                Map.of("district_id", districtId),
                new ParameterizedTypeReference<>() {}
        );
    }

}
