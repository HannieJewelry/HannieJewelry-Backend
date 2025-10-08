package hanniejewelry.vn.shipping.application.usecase.address;

import hanniejewelry.vn.shipping.application.dto.ExternalApiResponse;
import hanniejewelry.vn.shipping.application.dto.CountryDTO;
import hanniejewelry.vn.shipping.domain.model.Country;
import hanniejewelry.vn.shipping.domain.model.District;
import hanniejewelry.vn.shipping.domain.model.Province;
import hanniejewelry.vn.shipping.domain.model.Ward;
import hanniejewelry.vn.shipping.domain.repository.CountryRepositoryPort;
import hanniejewelry.vn.shipping.domain.repository.DistrictRepositoryPort;
import hanniejewelry.vn.shipping.domain.repository.ProvinceRepositoryPort;
import hanniejewelry.vn.shipping.domain.repository.WardRepositoryPort;
import hanniejewelry.vn.shipping.infrastructure.external.client.HaravanApiClient;
import io.vavr.CheckedFunction0;
import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SyncAddressDataUseCase {

    private final HaravanApiClient haravanApiClient;
    private final CountryRepositoryPort countryRepository;
    private final ProvinceRepositoryPort provinceRepository;
    private final DistrictRepositoryPort districtRepository;
    private final WardRepositoryPort wardRepository;

    public void execute() {
        System.out.println("🌍 Starting synchronization of all address data from Haravan...");

        retry(haravanApiClient::getCountries)
                .onSuccess(resp ->
                        Option.of(resp)
                                .map(ExternalApiResponse::getData)
                                .filter(list -> list != null && !list.isEmpty())
                                .peek(list -> {
                                    System.out.println("✅ Retrieved " + list.size() + " countries from Haravan");
                                    list.forEach(dto ->
                                            Option.ofOptional(countryRepository.getCountryById(dto.getId()))
                                                    .onEmpty(() -> {
                                                        countryRepository.save(Country.builder()
                                                                .id(dto.getId())
                                                                .code(dto.getCode())
                                                                .name(dto.getName())
                                                                .build());
                                                        System.out.println("✅ Saved new country: " + dto.getName());
                                                    })
                                                    .peek(x -> System.out.println("⚠️ Country already exists, skipping: " + dto.getName()))
                                    );
                                    System.out.println("✅ Completed synchronization of " + list.size() + " countries from Haravan");
                                })
                                .onEmpty(() ->
                                        Option.ofOptional(countryRepository.getCountryById(241))
                                                .onEmpty(() -> {
                                                    countryRepository.save(Country.builder().id(241).code("VN").name("Vietnam").build());
                                                    System.out.println("✅ Created default Vietnam country (ID: 241)");
                                                })
                                                .peek(x -> System.out.println("⚠️ Vietnam country already exists, skipping default creation (ID: 241)"))
                                )
                )
                .onFailure(e -> {
                    System.err.println("❌ Unable to connect to Haravan API after 3 attempts - creating default Vietnam country");
                    Option.ofOptional(countryRepository.getCountryById(241))
                            .onEmpty(() -> {
                                countryRepository.save(Country.builder().id(241).code("VN").name("Vietnam").build());
                                System.out.println("✅ Created default Vietnam country (ID: 241)");
                            })
                            .peek(x -> System.out.println("⚠️ Vietnam country already exists, skipping default creation (ID: 241)"));
                });

        // Synchronize provinces/districts/wards
        final Integer VIETNAM_COUNTRY_ID = 241;
        retry(() -> haravanApiClient.getCountryWithProvinces(VIETNAM_COUNTRY_ID))
                .onSuccess(countryResp ->
                        Option.of(countryResp)
                                .map(ExternalApiResponse::getData)
                                .map(CountryDTO::getProvinces)
                                .filter(ps -> ps != null && !ps.isEmpty())
                                .peek(provinces -> {
                                    System.out.println("✅ Retrieved " + provinces.size() + " provinces from Haravan");
                                    provinces.forEach(provinceDTO ->
                                            Option.ofOptional(provinceRepository.getProvinceById(provinceDTO.getId()))
                                                    .onEmpty(() -> {
                                                        provinceRepository.save(Province.builder()
                                                                .id(provinceDTO.getId())
                                                                .name(provinceDTO.getName())
                                                                .code(provinceDTO.getCode() != null ? provinceDTO.getCode() : null)
                                                                .countryId(VIETNAM_COUNTRY_ID)
                                                                .build());
                                                        System.out.println("✅ Saved new province: " + provinceDTO.getName());
                                                    })
                                                    .peek(x -> System.out.println("⚠️ Province already exists, skipping: " + provinceDTO.getName()))
                                    );

                                    // Loop through each province to get districts/wards
                                    provinces.forEach(provinceDTO -> {
                                        retry(() -> haravanApiClient.getDistricts(provinceDTO.getId()))
                                                .onSuccess(districtResp ->
                                                        Option.of(districtResp.getData())
                                                                .filter(districts -> districts != null && !districts.isEmpty())
                                                                .peek(districts -> districts.forEach(districtDTO ->
                                                                        Option.of(districtDTO.getId())
                                                                                .peek(id -> Option.ofOptional(districtRepository.getDistrictById(id))
                                                                                        .onEmpty(() -> {
                                                                                            districtRepository.save(District.builder()
                                                                                                    .id(districtDTO.getId())
                                                                                                    .provinceId(districtDTO.getProvinceId())
                                                                                                    .name(districtDTO.getName())
                                                                                                    .code(districtDTO.getCode())
                                                                                                    .build());
                                                                                            System.out.println("  ✅ Saved new district: " + districtDTO.getName());
                                                                                        })
                                                                                        .peek(x -> System.out.println("  ⚠️ District already exists, skipping: " + districtDTO.getName()))
                                                                                )
                                                                ))
                                                                .onEmpty(() -> System.err.println("⚠️ No districts for province: " + provinceDTO.getName()))
                                                );

                                        // Get wards for each district of the province
                                        retry(() -> haravanApiClient.getDistricts(provinceDTO.getId()))
                                                .onSuccess(districtResp ->
                                                        Option.of(districtResp.getData()).peek(districts ->
                                                                districts.forEach(districtDTO -> {
                                                                    retry(() -> haravanApiClient.getWards(districtDTO.getId()))
                                                                            .onSuccess(wardResp ->
                                                                                    Option.of(wardResp.getData())
                                                                                            .filter(wards -> wards != null && !wards.isEmpty())
                                                                                            .peek(wards -> wards.forEach(wardDTO ->
                                                                                                    Option.of(wardDTO.getCode())
                                                                                                            .filter(code -> code != null && !code.isEmpty())
                                                                                                            .peek(code -> Option.ofOptional(wardRepository.getWardById(code))
                                                                                                                    .onEmpty(() -> {
                                                                                                                        wardRepository.save(Ward.builder()
                                                                                                                                .code(wardDTO.getCode())
                                                                                                                                .districtId(wardDTO.getDistrictId())
                                                                                                                                .name(wardDTO.getName())
                                                                                                                                .build());
                                                                                                                        System.out.println("    ✅ Saved new ward: " + wardDTO.getName());
                                                                                                                    })
                                                                                                                    .peek(x -> System.out.println("    ⚠️ Ward already exists, skipping: " + wardDTO.getName()))
                                                                                                            )
                                                                                            ))
                                                                                            .onEmpty(() -> System.err.println("⚠️ No wards for district: " + districtDTO.getName()))
                                                                            );
                                                                })
                                                        )
                                                );
                                    });
                                })
                                .onEmpty(() -> System.err.println("❌ No provinces from Haravan API"))
                )
                .onFailure(e -> System.err.println("❌ Unable to retrieve country/provinces from Haravan API after 3 attempts"));
    }

    private <T> Try<T> retry(CheckedFunction0<T> supplier) {
        return Try.of(supplier)
                .recoverWith(e1 -> {
                    System.err.println("⚠️ Attempt 1 failed, retrying... " + e1.getMessage());
                    sleep(500);
                    return Try.of(supplier)
                            .recoverWith(e2 -> {
                                System.err.println("⚠️ Attempt 2 failed, will retry continuously until successful... " + e2.getMessage());
                                sleep(1000);
                                while (true) {
                                    Try<T> result = Try.of(supplier);
                                    if (result.isSuccess()) {
                                        return result;
                                    }
                                    System.err.println("⚠️ Retry failed, trying again in 2s... " + result.getCause().getMessage());
                                    sleep(2000);
                                }
                            });
                });
    }

    private void sleep(long millis) {
        Try.run(() -> Thread.sleep(millis));
    }
}