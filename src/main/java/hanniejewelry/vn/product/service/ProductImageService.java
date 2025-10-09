package hanniejewelry.vn.product.service;

import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.view.EntityViewSetting;
import hanniejewelry.vn.shared.service.CloudinaryService;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import hanniejewelry.vn.product.dto.VariantImageDTO;
import hanniejewelry.vn.product.entity.Product;
import hanniejewelry.vn.product.entity.ProductImage;
import hanniejewelry.vn.product.entity.ProductVariant;
import hanniejewelry.vn.product.repository.ProductImageRepository;
import hanniejewelry.vn.product.repository.ProductRepository;
import hanniejewelry.vn.product.repository.ProductVariantRepository;
import hanniejewelry.vn.product.view.ProductImageView;
import hanniejewelry.vn.shared.exception.UploadFailedException;
import hanniejewelry.vn.shared.utils.ExistenceChecker;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.blazebit.persistence.view.EntityViewManager;

import java.io.IOException;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ProductImageService {
    private final ProductRepository productRepository;
    private final ProductVariantRepository variantRepository;
    private final ProductImageRepository productImageRepository;
    private final CloudinaryService cloudinaryService;
    private final EntityManager em;
    private final EntityViewManager evm;
    private final CriteriaBuilderFactory cbf;
    private final ExistenceChecker existenceChecker;

    public ProductImageView uploadImageInternal(
            UUID productId,
            Set<Long> variantIds,
            Supplier<String> uploadImageSupplier,
            String filename
    ) {
        Set<Long> variantIdsNonNull = Optional.ofNullable(variantIds).orElseGet(Set::of);

        existenceChecker.validateAllExistOrThrow(ProductVariant.class, variantIdsNonNull,
                "Biến thể với ID %s không tồn tại cho sản phẩm", "product.id", productId);

        Product product = productRepository.findById(productId).orElseThrow(UploadFailedException::new);

        List<ProductImage> images = Optional.ofNullable(product.getImages()).orElseGet(ArrayList::new);
        product.setImages(images);

        String imageUrl = Optional.ofNullable(uploadImageSupplier.get())
                .orElseThrow(UploadFailedException::new);

        ProductImage img = ProductImage.builder()
                .filename(Optional.ofNullable(filename)
                        .filter(f -> !f.isBlank())
                        .orElseGet(() -> {
                            int lastSlash = imageUrl.lastIndexOf('/');
                            return (lastSlash >= 0 && lastSlash < imageUrl.length() - 1) ? imageUrl.substring(lastSlash + 1) : "unknown.jpg";
                        }))
                .src(imageUrl)
                .position(images.size() + 1)
                .variantIds(variantIdsNonNull)
                .product(product)
                .build();

        images.add(img);

        productImageRepository.save(img);
        productRepository.save(product);

        variantRepository.findAllById(variantIdsNonNull).forEach(variant -> {
            variant.setImageId(img.getId());
            variantRepository.save(variant);
        });

        return evm.find(em, ProductImageView.class, img.getId());
    }

    public ProductImageView uploadAndSave(UUID productId, MultipartFile file, Set<Long> variantIds) {
        return uploadImageInternal(
                productId,
                variantIds,
                () -> {
                    try {
                        return cloudinaryService.uploadImage(file, "product-" + productId + "-" + System.currentTimeMillis());
                    } catch (IOException e) {
                        throw new UploadFailedException(e);
                    }
                },
                null
        );
    }

    public ProductImageView uploadByUrl(UUID productId, String url, Set<Long> variantIds) {
        return uploadImageInternal(
                productId,
                variantIds,
                () -> {
                    try {
                        return cloudinaryService.uploadImageFromUrl(url, "product-" + productId + "-" + System.currentTimeMillis());
                    } catch (IOException e) {
                        throw new UploadFailedException(e);
                    }
                },
                null
        );
    }

    public List<ProductImageView> listImages(UUID productId) {
        return productImageRepository.findAllByProductId(productId)
                .stream()
                .map(image -> evm.find(em, ProductImageView.class, image.getId()))
                .collect(Collectors.toList());
    }

    public List<VariantImageDTO> findVariantsWithImage(UUID productId) {
        return
                cbf.create(em, VariantImageDTO.class)
                        .from(ProductVariant.class, "v")
                        .selectNew(VariantImageDTO.class)
                        .with("v.id")
                        .with("v.imageId")
                        .withSubquery()
                        .from(ProductImage.class, "img")
                        .select("img.src")
                        .where("img.id").eqExpression("v.imageId")
                        .end()
                        .end()
                        .where("v.product.id").eq(productId)
                        .getResultList()
        ;
    }

    public List<ProductImageView> findImageByVariantId(UUID productId, Long variantId) {
        var cb = cbf.create(em, ProductImage.class)
                .where("product.id").eq(productId)
                .where(":variantId").isMemberOf("variantIds")
                .setParameter("variantId", variantId);
        return evm.applySetting(EntityViewSetting.create(ProductImageView.class), cb).getResultList();
    }



}
