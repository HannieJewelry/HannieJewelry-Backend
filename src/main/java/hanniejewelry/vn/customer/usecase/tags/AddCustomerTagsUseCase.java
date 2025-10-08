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
public class AddCustomerTagsUseCase {
    private final CustomerRepository customerRepository;
    private final EntityViewManager evm;
    private final EntityManager em;

    public CustomerView execute(UUID id, CustomerTagsRequest request) {
        Customer existing = customerRepository.findById(id)
                .orElseThrow(() -> new BizException(BaseMessageType.NOT_FOUND, "Customer not found with id: {0}", id));

        if (request == null || request.getTags() == null || request.getTags().isBlank()) {
            return evm.find(em, CustomerView.class, existing.getId());
        }
        
        Set<String> newTagsSet = Arrays.stream(request.getTags().split(","))
                .map(String::trim)
                .filter(tag -> !tag.isEmpty())
                .collect(Collectors.toSet());
                
        Set<String> existingTagsSet = new HashSet<>();
        if (existing.getTags() != null && !existing.getTags().isBlank()) {
            existingTagsSet = Arrays.stream(existing.getTags().split(","))
                    .map(String::trim)
                    .filter(tag -> !tag.isEmpty())
                    .collect(Collectors.toSet());
        }
        
        existingTagsSet.addAll(newTagsSet);
        
        String updatedTags = String.join(",", existingTagsSet);

        Customer updated = existing.toBuilder()
                .tags(updatedTags)
                .build();

        Customer saved = customerRepository.save(updated);
        customerRepository.flush();
        return evm.find(em, CustomerView.class, saved.getId());
    }
}
