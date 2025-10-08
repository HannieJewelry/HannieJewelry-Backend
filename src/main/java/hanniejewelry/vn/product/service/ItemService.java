package hanniejewelry.vn.product.service;

import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.PagedList;
import com.blazebit.persistence.view.EntityViewManager;
import com.blazebit.persistence.view.EntityViewSetting;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import hanniejewelry.vn.product.dto.ItemRequest;
import hanniejewelry.vn.product.entity.Item;
import hanniejewelry.vn.product.repository.ItemRepository;
import hanniejewelry.vn.product.view.ItemView;
import hanniejewelry.vn.shared.filter.GenericBlazeFilterApplier;
import hanniejewelry.vn.shared.dto.GenericFilterRequest;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Transactional
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ItemService {

    EntityViewManager evm;
    ItemRepository itemRepository;
    CriteriaBuilderFactory cbf;
    EntityManager em;
    private final GenericBlazeFilterApplier<ItemView> filterApplier = new GenericBlazeFilterApplier<>();

    public PagedList<ItemView> getAllItemsWithBlazeFilter(GenericFilterRequest filter) {
        var setting = EntityViewSetting.create(ItemView.class, filter.getPage(), filter.getSize());
        filterApplier.applyFilters(setting, filter);

        var cb = cbf.create(em, Item.class);
        filterApplier.applySort(cb, filter);

        return evm.applySetting(setting, cb).getResultList();
    }

    public ItemView createItemAndReturnView(ItemRequest request) {
        Item saved = itemRepository.save(Item.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build());
        itemRepository.flush();
        return evm.find(em, ItemView.class, saved.getId());
    }

    public ItemView updateItemAndReturnView(UUID id, ItemRequest request) {
        Item updated = itemRepository.findById(id)
                .orElseThrow()
                .toBuilder()
                .name(request.getName())
                .description(request.getDescription())
                .build();
        Item saved = itemRepository.save(updated);
        itemRepository.flush();
        return evm.find(em, ItemView.class, saved.getId());
    }

    public void deleteItem(final UUID id) {
        itemRepository.findById(id)
                .orElseThrow();
        itemRepository.softDeleteById(id);
        itemRepository.flush();
    }

    public ItemView getItemViewById(UUID id) {
        itemRepository.findById(id)
                .orElseThrow();
        return evm.find(em, ItemView.class, id);
    }
}
