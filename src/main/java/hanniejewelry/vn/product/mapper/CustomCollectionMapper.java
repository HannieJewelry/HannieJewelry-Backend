package hanniejewelry.vn.product.mapper;

import hanniejewelry.vn.product.dto.CustomCollectionResponse;
import hanniejewelry.vn.product.entity.CustomCollection;
import hanniejewelry.vn.product.repository.CollectRepository;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

@Mapper(componentModel = "spring", 
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        imports = {Optional.class})
public abstract class CustomCollectionMapper {

    @Autowired
    protected CollectRepository collectRepository;
    
    @Mapping(target = "itemsCount", expression = "java(countItems(collection))")
    @Mapping(target = "image", expression = "java(mapImage(collection))")
    public abstract CustomCollectionResponse toCustomCollectionResponse(CustomCollection collection);
    
    public abstract List<CustomCollectionResponse> toCustomCollectionResponses(List<CustomCollection> collections);
    
    protected int countItems(CustomCollection collection) {
        return collectRepository.countByCollection(collection);
    }
    
    protected CustomCollectionResponse.ImageResponse mapImage(CustomCollection collection) {
        return Optional.ofNullable(collection.getImage())
                .map(img -> CustomCollectionResponse.ImageResponse.builder()
                        .createdAt(img.getCreatedAt())
                        .src(img.getSrc())
                        .build())
                .orElse(CustomCollectionResponse.ImageResponse.builder().createdAt(null).src(null).build());
    }
} 