package hanniejewelry.vn.inventory.service;

import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.PagedList;
import com.blazebit.persistence.view.EntityViewManager;
import com.blazebit.persistence.view.EntityViewSetting;
import hanniejewelry.vn.inventory.dto.SupplierRequest;
import hanniejewelry.vn.inventory.entity.Supplier;
import hanniejewelry.vn.inventory.repository.SupplierRepository;
import hanniejewelry.vn.inventory.view.SupplierView;
import hanniejewelry.vn.shared.exception.BaseMessageType;
import hanniejewelry.vn.shared.exception.BizException;
import hanniejewelry.vn.shared.filter.GenericBlazeFilterApplier;
import hanniejewelry.vn.shared.dto.GenericFilterRequest;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SupplierService {

    EntityViewManager evm;
    SupplierRepository supplierRepository;
    CriteriaBuilderFactory cbf;
    EntityManager em;
    private final GenericBlazeFilterApplier<SupplierView> filterApplier = new GenericBlazeFilterApplier<>();

    public PagedList<SupplierView> getAllSuppliersWithBlazeFilter(GenericFilterRequest filter) {
        var setting = EntityViewSetting.create(SupplierView.class, filter.getPage(), filter.getSize());
        filterApplier.applyFilters(setting, filter);

        var cb = cbf.create(em, Supplier.class);
        filterApplier.applySort(cb, filter);

        return evm.applySetting(setting, cb).getResultList();
    }

    public SupplierView getSupplierViewById(UUID id) {
        supplierRepository.findById(id)
                .orElseThrow(() -> new BizException(BaseMessageType.NOT_FOUND, "Supplier not found with id: " + id));
        return evm.find(em, SupplierView.class, id);
    }

    public SupplierView createSupplierAndReturnView(SupplierRequest request) {
        Supplier supplier = Supplier.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .supplierName(request.getSupplierName())
                .phone(request.getPhone())
                .email(request.getEmail())
                .status(request.getStatus())
                .zipCode(request.getZipCode())
                .countryId(request.getCountryId())
                .location(request.getLocation())
                .address(request.getAddress())
                .provinceId(request.getProvinceId())
                .districtId(request.getDistrictId())
                .city(request.getCity())
                .wardId(request.getWardId())
                .createdDate(Instant.now())
                .debt(BigDecimal.ZERO)
                .totalPurchase(BigDecimal.ZERO)
                .company(request.getCompany())
                .taxCode(request.getTaxCode())
                .notes(request.getNotes())
                .tags(request.getTags())
                .build();
                
        Supplier saved = supplierRepository.save(supplier);
        supplierRepository.flush();
        return evm.find(em, SupplierView.class, saved.getId());
    }

    public SupplierView updateSupplierAndReturnView(UUID id, SupplierRequest request) {
        Supplier existingSupplier = supplierRepository.findById(id)
                .orElseThrow(() -> new BizException(BaseMessageType.NOT_FOUND, "Supplier not found with id: " + id));
                
        Supplier updatedSupplier = existingSupplier.toBuilder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .supplierName(request.getSupplierName())
                .phone(request.getPhone())
                .email(request.getEmail())
                .status(request.getStatus())
                .zipCode(request.getZipCode())
                .countryId(request.getCountryId())
                .location(request.getLocation())
                .address(request.getAddress())
                .provinceId(request.getProvinceId())
                .districtId(request.getDistrictId())
                .city(request.getCity())
                .wardId(request.getWardId())
                .company(request.getCompany())
                .taxCode(request.getTaxCode())
                .notes(request.getNotes())
                .tags(request.getTags())
                .build();
                
        Supplier saved = supplierRepository.save(updatedSupplier);
        supplierRepository.flush();
        return evm.find(em, SupplierView.class, saved.getId());
    }

    public void deleteSupplier(UUID id) {
        supplierRepository.findById(id)
                .orElseThrow(() -> new BizException(BaseMessageType.NOT_FOUND, "Supplier not found with id: " + id));
        
        supplierRepository.softDeleteById(id);
        supplierRepository.flush();
    }

    public SupplierView updateSupplierDebt(UUID id, BigDecimal debt) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new BizException(BaseMessageType.NOT_FOUND, "Supplier not found with id: " + id));
        
        supplier.setDebt(debt);
        Supplier saved = supplierRepository.save(supplier);
        supplierRepository.flush();
        
        return evm.find(em, SupplierView.class, saved.getId());
    }

    public SupplierView updateSupplierTotalPurchase(UUID id, BigDecimal totalPurchase) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new BizException(BaseMessageType.NOT_FOUND, "Supplier not found with id: " + id));
        
        supplier.setTotalPurchase(totalPurchase);
        Supplier saved = supplierRepository.save(supplier);
        supplierRepository.flush();
        
        return evm.find(em, SupplierView.class, saved.getId());
    }
} 