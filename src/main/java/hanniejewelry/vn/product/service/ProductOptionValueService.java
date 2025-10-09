package hanniejewelry.vn.product.service;

import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.view.EntityViewManager;
import hanniejewelry.vn.product.entity.ProductOptionValue;
import hanniejewelry.vn.product.repository.ProductOptionValueRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ProductOptionValueService {

    ProductOptionValueRepository productOptionValueRepository;
    private static final String SIZE_TYPE = "size";
    private static final String COLOR_TYPE = "color";
    private static final String MATERIAL_TYPE = "material";

    /**
     * Get all active size values as a list of strings
     *
     * @return List of size values
     */
    public List<String> getAllActiveSizeValues() {
        return productOptionValueRepository.findAllByOptionTypeAndActiveIsTrueOrderByPositionAsc(SIZE_TYPE)
                .stream()
                .map(ProductOptionValue::getValue)
                .collect(Collectors.toList());
    }

    /**
     * Get all active color values as a list of strings
     *
     * @return List of color values
     */
    public List<String> getAllActiveColorValues() {
        return productOptionValueRepository.findAllByOptionTypeAndActiveIsTrueOrderByPositionAsc(COLOR_TYPE)
                .stream()
                .map(ProductOptionValue::getValue)
                .collect(Collectors.toList());
    }
    
    /**
     * Get all active material values as a list of strings
     *
     * @return List of material values
     */
    public List<String> getAllActiveMaterialValues() {
        return productOptionValueRepository.findAllByOptionTypeAndActiveIsTrueOrderByPositionAsc(MATERIAL_TYPE)
                .stream()
                .map(ProductOptionValue::getValue)
                .collect(Collectors.toList());
    }
} 