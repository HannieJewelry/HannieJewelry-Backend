package hanniejewelry.vn.payment.service;

import hanniejewelry.vn.payment.dto.PaymentMethodView;
import hanniejewelry.vn.payment.entity.PaymentMethod;
import hanniejewelry.vn.order.repository.PaymentMethodRepository;
import hanniejewelry.vn.shared.exception.BaseMessageType;
import hanniejewelry.vn.shared.exception.BizException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentMethodService {
    
    private final PaymentMethodRepository paymentMethodRepository;
    
    @Transactional(readOnly = true)
    public List<PaymentMethodView> getAllActivePaymentMethods() {
        return paymentMethodRepository.findByActiveTrueOrderByDisplayOrderAsc().stream()
                .map(this::convertToView)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<PaymentMethodView> getOnlinePaymentMethods() {
        return paymentMethodRepository.findByActiveTrueAndOnlineTrue().stream()
                .map(this::convertToView)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<PaymentMethodView> getOfflinePaymentMethods() {
        return paymentMethodRepository.findByActiveTrueAndOnlineFalse().stream()
                .map(this::convertToView)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public PaymentMethodView getPaymentMethodById(Long id) {
        PaymentMethod paymentMethod = paymentMethodRepository.findById(id)
                .orElseThrow(() -> new BizException(BaseMessageType.NOT_FOUND, "Payment method not found"));
        
        return convertToView(paymentMethod);
    }
    
    @Transactional
    public PaymentMethodView createPaymentMethod(PaymentMethodView view) {
        PaymentMethod paymentMethod = PaymentMethod.builder()
                .name(view.getName())
                .description(view.getDescription())
                .code(view.getCode())
                .online(view.getOnline() != null ? view.getOnline() : false)
                .systemTypeId(view.getSystemTypeId() != null ? view.getSystemTypeId() : 1)
                .active(true)
                .displayOrder(getNextDisplayOrder())
                .build();
        
        PaymentMethod saved = paymentMethodRepository.save(paymentMethod);
        return convertToView(saved);
    }
    
    @Transactional
    public PaymentMethodView updatePaymentMethod(Long id, PaymentMethodView view) {
        PaymentMethod paymentMethod = paymentMethodRepository.findById(id)
                .orElseThrow(() -> new BizException(BaseMessageType.NOT_FOUND, "Payment method not found"));
        
        paymentMethod.setName(view.getName());
        paymentMethod.setDescription(view.getDescription());
        paymentMethod.setCode(view.getCode());
        paymentMethod.setOnline(view.getOnline());
        paymentMethod.setSystemTypeId(view.getSystemTypeId());
        
        PaymentMethod saved = paymentMethodRepository.save(paymentMethod);
        return convertToView(saved);
    }
    
    @Transactional
    public void deletePaymentMethod(Long id) {
        PaymentMethod paymentMethod = paymentMethodRepository.findById(id)
                .orElseThrow(() -> new BizException(BaseMessageType.NOT_FOUND, "Payment method not found"));
        
        // Soft delete by setting active to false
        paymentMethod.setActive(false);
        paymentMethodRepository.save(paymentMethod);
    }
    
    private PaymentMethodView convertToView(PaymentMethod paymentMethod) {
        return PaymentMethodView.builder()
                .id(paymentMethod.getId())
                .name(paymentMethod.getName())
                .description(paymentMethod.getDescription())
                .code(paymentMethod.getCode())
                .online(paymentMethod.getOnline())
                .systemTypeId(paymentMethod.getSystemTypeId())
                .build();
    }
    
    private Integer getNextDisplayOrder() {
        return paymentMethodRepository.findAll().stream()
                .map(PaymentMethod::getDisplayOrder)
                .filter(order -> order != null)
                .max(Integer::compareTo)
                .orElse(0) + 1;
    }
} 