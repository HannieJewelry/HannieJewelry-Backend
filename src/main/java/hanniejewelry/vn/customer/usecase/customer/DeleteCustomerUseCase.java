package hanniejewelry.vn.customer.usecase.customer;

import hanniejewelry.vn.customer.repository.CustomerRepository;
import hanniejewelry.vn.shared.exception.BaseMessageType;
import hanniejewelry.vn.shared.exception.BizException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeleteCustomerUseCase {
    private final CustomerRepository customerRepository;

    public void execute(UUID id) {
        if (!customerRepository.existsById(id)) {
            throw new BizException(BaseMessageType.NOT_FOUND, "Customer not found with id: {0}", id);
        }
        customerRepository.softDeleteById(id);
        customerRepository.flush();
    }
}
