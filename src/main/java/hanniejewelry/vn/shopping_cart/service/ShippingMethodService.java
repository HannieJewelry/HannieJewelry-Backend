package hanniejewelry.vn.shopping_cart.service;

import hanniejewelry.vn.order.dto.ShippingMethodView;
import hanniejewelry.vn.order.ShippingMethod;
import hanniejewelry.vn.order.repository.ShippingMethodRepository;
import hanniejewelry.vn.shared.exception.BaseMessageType;
import hanniejewelry.vn.shared.exception.BizException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShippingMethodService {
    
    private final ShippingMethodRepository shippingMethodRepository;
    
    @Transactional(readOnly = true)
    public List<ShippingMethodView> getAllActiveShippingMethods() {
        return shippingMethodRepository.findByActiveTrueOrderByDisplayOrderAsc().stream()
                .map(this::convertToView)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public ShippingMethodView getShippingMethodById(Long id) {
        ShippingMethod shippingMethod = shippingMethodRepository.findById(id)
                .orElseThrow(() -> new BizException(BaseMessageType.NOT_FOUND, "Shipping method not found"));
        
        return convertToView(shippingMethod);
    }
    
    @Transactional
    public ShippingMethodView createShippingMethod(ShippingMethodView view) {
        ShippingMethod shippingMethod = ShippingMethod.builder()
                .name(view.getName())
                .description(view.getDescription())
                .price(view.getPrice() != null ? view.getPrice() : BigDecimal.ZERO)
                .code(view.getCode())
                .active(true)
                .displayOrder(getNextDisplayOrder())
                .build();
        
        ShippingMethod saved = shippingMethodRepository.save(shippingMethod);
        return convertToView(saved);
    }
    
    @Transactional
    public ShippingMethodView updateShippingMethod(Long id, ShippingMethodView view) {
        ShippingMethod shippingMethod = shippingMethodRepository.findById(id)
                .orElseThrow(() -> new BizException(BaseMessageType.NOT_FOUND, "Shipping method not found"));
        
        shippingMethod.setName(view.getName());
        shippingMethod.setDescription(view.getDescription());
        shippingMethod.setPrice(view.getPrice());
        shippingMethod.setCode(view.getCode());
        
        ShippingMethod saved = shippingMethodRepository.save(shippingMethod);
        return convertToView(saved);
    }
    
    @Transactional
    public void deleteShippingMethod(Long id) {
        ShippingMethod shippingMethod = shippingMethodRepository.findById(id)
                .orElseThrow(() -> new BizException(BaseMessageType.NOT_FOUND, "Shipping method not found"));
        
        // Soft delete by setting active to false
        shippingMethod.setActive(false);
        shippingMethodRepository.save(shippingMethod);
    }
    
    private ShippingMethodView convertToView(ShippingMethod shippingMethod) {
        return ShippingMethodView.builder()
                .id(shippingMethod.getId())
                .name(shippingMethod.getName())
                .description(shippingMethod.getDescription())
                .price(shippingMethod.getPrice())
                .code(shippingMethod.getCode())
                .build();
    }
    
    private Integer getNextDisplayOrder() {
        return shippingMethodRepository.findAll().stream()
                .map(ShippingMethod::getDisplayOrder)
                .filter(order -> order != null)
                .max(Integer::compareTo)
                .orElse(0) + 1;
    }
} 