package hanniejewelry.vn.shipping.infrastructure.persistence.mapper;

import hanniejewelry.vn.shipping.domain.model.Province;
import hanniejewelry.vn.shipping.infrastructure.persistence.entity.ProvinceEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProvinceEntityMapper {

    @Mapping(source = "provinceId", target = "id")
    Province toDomain(ProvinceEntity entity);

    @Mapping(source = "id", target = "provinceId")
    ProvinceEntity toEntity(Province domain);

    List<Province> toDomainList(List<ProvinceEntity> entities);

    List<ProvinceEntity> toEntityList(List<Province> domains);
}