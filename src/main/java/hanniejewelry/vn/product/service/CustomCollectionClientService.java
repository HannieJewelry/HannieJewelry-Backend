package hanniejewelry.vn.product.service;

import hanniejewelry.vn.product.entity.Product;
import hanniejewelry.vn.product.dto.*;
import hanniejewelry.vn.product.entity.*;
import hanniejewelry.vn.product.mapper.*;
import hanniejewelry.vn.product.repository.*;
import hanniejewelry.vn.shared.exception.BaseMessageType;
import hanniejewelry.vn.shared.exception.BizException;
import hanniejewelry.vn.shared.filter.SpecificationBuilder;
import hanniejewelry.vn.shared.dto.PagedResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CustomCollectionClientService {
    private final CustomCollectionRepository customCollectionRepository;
    private final CollectRepository collectRepository;
    private final ProductRepository productRepository;
    private final ProductClientMapper productMapper;
    private final CustomCollectionMapper collectionMapper;

    public List<CustomCollectionResponse> getAllCollections() {
        return collectionMapper.toCustomCollectionResponses(
                customCollectionRepository.findAll().stream()
                        .filter(CustomCollection::getPublished)
                        .collect(Collectors.toList())
        );
    }

    public ProductClientResponse getProductByHandle(String handle) {
        return productRepository.findAll().stream()
                .filter(p -> handle.equals(p.getHandle()))
                .findFirst()
                .map(productMapper::productToProductClientResponse)
                .orElseThrow(() -> new BizException(BaseMessageType.NOT_FOUND, "Product not found with handle: {0}", handle));
    }


    public PagedResponse<ProductClientResponse> getAllProducts(ProductFilterRequest filter) {
        Page<Product> page = productRepository.findAll(SpecificationBuilder.build(filter.getFilter(), Product.class), PageRequest.of(
                filter.getPage(),
                filter.getSize(),
                Sort.by(Sort.Direction.fromString(filter.getDirection()), filter.getSortProperty())
        ));
        return new PagedResponse<>(
                productMapper.productsToProductClientResponses(page.getContent()),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                filter.getSortProperty(),
                filter.getDirection()
        );
    }



public PagedResponse<ProductClientResponse> getProductsByCollectionHandle(String handle, ProductFilterRequest filter) {
    Page<Product> page = productRepository.findAll(SpecificationBuilder.build(customCollectionRepository.findByHandle(handle)
            .map(collection -> collectRepository.findByCollectionOrderByPosition(collection)
                    .stream().map(c -> c.getProduct().getId()).collect(Collectors.toList()))
            .orElseThrow(() -> new BizException(BaseMessageType.NOT_FOUND, "Collection not found with handle: {0}", handle)), "id", filter.getFilter(), Product.class), PageRequest.of(
            filter.getPage(),
            filter.getSize(),
            Sort.by(Sort.Direction.fromString(filter.getDirection()), filter.getSortProperty())
    ));
    return new PagedResponse<>(
            productMapper.productsToProductClientResponses(page.getContent()),
            page.getNumber(),
            page.getSize(),
            page.getTotalElements(),
            filter.getSortProperty(),
            filter.getDirection()
    );
}



}
