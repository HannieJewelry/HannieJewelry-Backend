package hanniejewelry.vn.customer.usecase.customer;

import com.blazebit.persistence.view.EntityViewManager;
import hanniejewelry.vn.customer.dto.CustomerProfileUpdateRequest;
import hanniejewelry.vn.customer.repository.CustomerRepository;
import hanniejewelry.vn.customer.view.CustomerView;
import hanniejewelry.vn.shared.exception.BaseMessageType;
import hanniejewelry.vn.shared.exception.BizException;
import hanniejewelry.vn.shared.service.CloudinaryService;
import hanniejewelry.vn.shared.utils.SecurityUtils;
import io.vavr.control.Option;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UpdateCustomerProfileUseCase {
    private final CustomerRepository customerRepository;
    private final EntityViewManager evm;
    private final EntityManager em;
    private final CloudinaryService cloudinaryService;

    public CustomerView execute(CustomerProfileUpdateRequest req) {
        var existing = SecurityUtils.getCurrentCustomer();
        
        // Debug logging
        log.info("Updating customer profile - ID: {}", existing.getId());
        log.info("Received firstName: '{}', lastName: '{}'", req.getFirstName(), req.getLastName());

        Option.of(req.getEmail()).filter(email -> !email.equals(existing.getEmail()))
                .filter(email -> customerRepository.findByEmail(email).isPresent())
                .forEach(email -> { throw new BizException(BaseMessageType.ENTITY_ALREADY_EXISTS, "Customer with email {0} already exists", email); });

        String avatarUrl = existing.getAvatarUrl();
        if (req.getAvatar() != null && !req.getAvatar().isEmpty()) {
            try {
                avatarUrl = uploadCustomerAvatar(req.getAvatar(), existing.getId());
                log.info("Avatar uploaded successfully for customer ID: {}", existing.getId());
            } catch (IOException e) {
                log.error("Failed to upload avatar for customer ID: {}", existing.getId(), e);
                throw new BizException(BaseMessageType.INTERNAL_ERROR, "Failed to upload avatar: {0}", e.getMessage());
            }
        }

        String firstName = req.getFirstName();
        String lastName = req.getLastName();
        
        log.info("Updating customer - firstName: '{}' -> '{}', lastName: '{}' -> '{}'", 
                existing.getFirstName(), firstName, 
                existing.getLastName(), lastName);

        var customer = customerRepository.save(existing.toBuilder()
                .email(Option.of(req.getEmail()).getOrElse(existing.getEmail()))
                .firstName(firstName != null ? firstName : existing.getFirstName())
                .lastName(lastName != null ? lastName : existing.getLastName())
                .birthday(Option.of(req.getBirthday()).getOrElse(existing.getBirthday()))
                .gender(Option.of(req.getGender()).getOrElse(existing.getGender()))
                .avatarUrl(avatarUrl)
                .build());

        customerRepository.flush();
        
        log.info("Customer updated - new firstName: '{}', new lastName: '{}'", 
                customer.getFirstName(), customer.getLastName());

        return evm.find(em, CustomerView.class, customer.getId());
    }
    
    private String uploadCustomerAvatar(MultipartFile avatar, UUID customerId) throws IOException {
        String filename = "customer_avatar_" + customerId + "_" + System.currentTimeMillis();
        return cloudinaryService.uploadImage(avatar, filename);
    }
} 