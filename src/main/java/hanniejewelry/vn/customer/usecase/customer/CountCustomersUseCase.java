package hanniejewelry.vn.customer.usecase.customer;

import hanniejewelry.vn.customer.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CountCustomersUseCase {
    private final CustomerRepository customerRepository;

    public long execute() {
        return customerRepository.count();
    }
}
