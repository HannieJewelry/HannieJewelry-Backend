package hanniejewelry.vn.customer.usecase.tags;

import hanniejewelry.vn.customer.dto.CustomerTagsRequest;
import hanniejewelry.vn.customer.entity.Customer;
import hanniejewelry.vn.customer.repository.CustomerRepository;
import hanniejewelry.vn.customer.view.CustomerView;
import hanniejewelry.vn.shared.exception.BaseMessageType;
import hanniejewelry.vn.shared.exception.BizException;
import com.blazebit.persistence.view.EntityViewManager;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DeleteCustomerTagsUseCase {
    private final CustomerRepository customerRepository;
    private final EntityViewManager evm;
    private final EntityManager em;

    public CustomerView execute(UUID id, CustomerTagsRequest request) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new BizException(BaseMessageType.NOT_FOUND, "Customer not found with id: {0}", id));
        
        String updatedTags = null;
        if (request != null && request.getTags() != null && !request.getTags().isBlank() && 
            customer.getTags() != null && !customer.getTags().isBlank()) {
            
            Set<String> tagsToRemove = Arrays.stream(request.getTags().split(","))
                    .map(String::trim)
                    .collect(Collectors.toSet());
            
            String remaining = Arrays.stream(customer.getTags().split(","))
                    .map(String::trim)
                    .filter(tag -> !tagsToRemove.contains(tag))
                    .collect(Collectors.joining(","));
            
            updatedTags = remaining.isEmpty() ? null : remaining;
        }
        
        return evm.find(
            em, 
            CustomerView.class, 
            customerRepository.save(customer.toBuilder().tags(updatedTags).build()).getId()
        );
    }
}
