package hanniejewelry.vn.product.mapper;

import hanniejewelry.vn.product.dto.ProductClientResponse;
import hanniejewelry.vn.product.entity.Product;
import hanniejewelry.vn.product.entity.ProductVariant;
import org.mapstruct.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", 
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        imports = {Collections.class, Optional.class, UUID.class})
public interface ProductClientMapper {

    @Mapping(target = "available", constant = "true")
    @Mapping(target = "soleQuantity", expression = "java(calculateSoleQuantity(product))")
    @Mapping(target = "image", expression = "java(mapFirstImage(product))")
    @Mapping(target = "images", expression = "java(mapImages(product))")
    @Mapping(target = "variants", expression = "java(mapVariants(product))")
    @Mapping(target = "options", expression = "java(mapOptions(product))")
    ProductClientResponse productToProductClientResponse(Product product);

    List<ProductClientResponse> productsToProductClientResponses(List<Product> products);

    default Integer calculateSoleQuantity(Product product) {
        return Optional.ofNullable(product.getVariants())
                .orElse(Collections.emptyList())
                .stream()
                .mapToInt(ProductVariant::getInventoryQuantity)
                .sum();
    }
    
    default ProductClientResponse.ProductImageResponse mapFirstImage(Product product) {
        return Optional.ofNullable(product.getImages())
                .filter(imgs -> !imgs.isEmpty())
                .map(imgs -> imgs.get(0))
                .map(img -> ProductClientResponse.ProductImageResponse.builder()
                        .id(img.getId())
                        .productId(img.getProduct().getId())
                        .position(img.getPosition())
                        .src(img.getSrc())
                        .alt(null)
                        .createdAt(img.getCreatedAt())
                        .updatedAt(img.getUpdatedAt())
                        .build())
                .orElse(null);
    }
    
    default List<ProductClientResponse.ProductImageResponse> mapImages(Product product) {
        return Optional.ofNullable(product.getImages())
                .orElse(Collections.emptyList())
                .stream()
                .map(img -> ProductClientResponse.ProductImageResponse.builder()
                        .id(img.getId())
                        .productId(img.getProduct().getId())
                        .position(img.getPosition())
                        .src(img.getSrc())
                        .alt(null)
                        .createdAt(img.getCreatedAt())
                        .updatedAt(img.getUpdatedAt())
                        .build())
                .collect(Collectors.toList());
    }
    
    default List<ProductClientResponse.ProductVariantResponse> mapVariants(Product product) {
        return Optional.ofNullable(product.getVariants())
                .orElse(Collections.emptyList())
                .stream()
                .map(variant -> ProductClientResponse.ProductVariantResponse.builder()
                        .id(variant.getId())
                        .title(variant.getTitle())
                        .price(variant.getPrice() != null ? variant.getPrice().toString() : null)
                        .compareAtPrice(variant.getCompareAtPrice() != null ? variant.getCompareAtPrice().toString() : null)
                        .sku(variant.getSku())
                        .barcode(variant.getBarcode())
                        .grams(variant.getGrams() != null ? variant.getGrams().intValue() : null)
                        .position(variant.getPosition())
                        .inventoryQuantity(variant.getInventoryQuantity())
                        .oldInventoryQuantity(variant.getInventoryQuantity())
                        .inventoryPolicy(variant.getInventoryPolicy())
                        .inventoryManagement(variant.getInventoryManagement())
                        .requiresShipping(variant.isRequiresShipping())
                        .taxable(variant.isTaxable())
                        .option1(variant.getOption1())
                        .option2(variant.getOption2())
                        .option3(variant.getOption3())
                        .imageId(variant.getImageId())
                        .weight(variant.getGrams() != null ? variant.getGrams().intValue() : null)
                        .available(variant.getInventoryQuantity() != null && variant.getInventoryQuantity() > 0)
                        .weightUnit("gram")
                        .build())
                .collect(Collectors.toList());
    }
    
    default List<ProductClientResponse.ProductOptionResponse> mapOptions(Product product) {
        return Optional.ofNullable(product.getOptions())
                .orElse(Collections.emptyList())
                .stream()
                .map(option -> {
                    List<String> values = Optional.ofNullable(product.getVariants())
                            .orElse(Collections.emptyList())
                            .stream()
                            .map(variant -> {
                                if (option.getPosition() == 1) return variant.getOption1();
                                if (option.getPosition() == 2) return variant.getOption2();
                                if (option.getPosition() == 3) return variant.getOption3();
                                return null;
                            })
                            .filter(v -> v != null && !v.isEmpty())
                            .distinct()
                            .collect(Collectors.toList());
                            
                    return ProductClientResponse.ProductOptionResponse.builder()
                            .name(option.getName())
                            .position(option.getPosition())
                            .productId(product.getId())
                            .values(values)
                            .build();
                })
                .collect(Collectors.toList());
    }
} 