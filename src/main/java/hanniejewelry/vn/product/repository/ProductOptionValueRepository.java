package hanniejewelry.vn.product.repository;

import hanniejewelry.vn.product.entity.ProductOptionValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.UUID;

public interface ProductOptionValueRepository extends JpaRepository<ProductOptionValue, UUID>, JpaSpecificationExecutor<ProductOptionValue> {
    List<ProductOptionValue> findAllByOptionTypeAndActiveIsTrueOrderByPositionAsc(String optionType);
}