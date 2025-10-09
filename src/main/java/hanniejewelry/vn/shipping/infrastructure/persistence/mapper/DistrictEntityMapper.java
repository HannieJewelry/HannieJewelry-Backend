package hanniejewelry.vn.shipping.infrastructure.persistence.mapper;

import hanniejewelry.vn.shipping.domain.model.District;
import hanniejewelry.vn.shipping.infrastructure.persistence.entity.DistrictEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DistrictEntityMapper {

    @Mapping(source = "districtId", target = "id")
    District toDomain(DistrictEntity entity);

    @Mapping(source = "id", target = "districtId")
    DistrictEntity toEntity(District domain);
    
    List<District> toDomainList(List<DistrictEntity> entities);
}