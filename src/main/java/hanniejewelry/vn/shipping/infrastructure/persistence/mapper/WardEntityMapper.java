package hanniejewelry.vn.shipping.infrastructure.persistence.mapper;

import hanniejewelry.vn.shipping.domain.model.Ward;
import hanniejewelry.vn.shipping.infrastructure.persistence.entity.WardEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface WardEntityMapper {

    Ward toDomain(WardEntity entity);

    WardEntity toEntity(Ward domain);

    List<Ward> toDomainList(List<WardEntity> entities);

    List<WardEntity> toEntityList(List<Ward> domains);
}