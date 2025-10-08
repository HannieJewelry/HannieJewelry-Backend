package hanniejewelry.vn.product.controller;

import hanniejewelry.vn.product.service.ProductOptionValueService;
import hanniejewelry.vn.shared.dto.RestResponse;
import hanniejewelry.vn.shared.utils.RestResponseUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductOptionValueController {

    private final ProductOptionValueService productOptionValueService;

    /**
     * Get all active size values
     *
     * @return List of size values
     */
    @GetMapping("/size_values")
    public ResponseEntity<RestResponse<List<String>>> getAllActiveSizeValues() {
        return RestResponseUtils.successResponse(productOptionValueService.getAllActiveSizeValues());
    }

    /**
     * Get all active color values
     *
     * @return List of color values
     */
    @GetMapping("/color_values")
    public ResponseEntity<RestResponse<List<String>>> getAllActiveColorValues() {
        return RestResponseUtils.successResponse(productOptionValueService.getAllActiveColorValues());
    }
    
    /**
     * Get all active material values
     *
     * @return List of material values
     */
    @GetMapping("/material_values")
    public ResponseEntity<RestResponse<List<String>>> getAllActiveMaterialValues() {
        return RestResponseUtils.successResponse(productOptionValueService.getAllActiveMaterialValues());
    }
} 