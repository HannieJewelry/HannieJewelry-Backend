package hanniejewelry.vn.shipping.infrastructure.persistence.mapper;

import hanniejewelry.vn.shipping.domain.model.Country;
import hanniejewelry.vn.shipping.infrastructure.persistence.entity.CountryEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CountryEntityMapper {

    Country toDomain(CountryEntity entity);

    CountryEntity toEntity(Country domain);

    List<Country> toDomainList(List<CountryEntity> entities);

    List<CountryEntity> toEntityList(List<Country> domains);
} 