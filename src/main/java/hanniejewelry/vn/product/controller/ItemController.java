package hanniejewelry.vn.product.controller;

import hanniejewelry.vn.shared.dto.RestResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import hanniejewelry.vn.product.dto.ItemRequest;
import hanniejewelry.vn.product.service.ItemService;
import hanniejewelry.vn.product.view.ItemView;
import hanniejewelry.vn.shared.dto.GenericFilterRequest;
import hanniejewelry.vn.shared.utils.RestResponseUtils;
import hanniejewelry.vn.shared.dto.PagedResponse;
import hanniejewelry.vn.shared.utils.ResponseUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static hanniejewelry.vn.shared.constants.ApiConstants.API_PREFIX;

@RestController
@RequestMapping(API_PREFIX + "/items")
@RequiredArgsConstructor
public final class ItemController {

    private final ItemService itemService;

    @GetMapping("/{id}")
    public ResponseEntity<RestResponse<ItemView>> getItemById(@PathVariable UUID id) {
        return RestResponseUtils.successResponse(itemService.getItemViewById(id));
    }

    @PostMapping
    public ResponseEntity<RestResponse<ItemView>> createItem(@Valid @RequestBody ItemRequest request) {
        return RestResponseUtils.createdResponse(
                itemService.createItemAndReturnView(request)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<RestResponse<ItemView>> updateItem(
            @PathVariable UUID id, @Valid @RequestBody ItemRequest request) {
        return RestResponseUtils.successResponse(
                itemService.updateItemAndReturnView(id, request)

        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RestResponse<Void>> deleteItem(@PathVariable UUID id) {
        itemService.deleteItem(id);
        return RestResponseUtils.successResponse(null);
    }

    @GetMapping("/bp/view")
    public ResponseEntity<RestResponse<PagedResponse<ItemView>>> getItemsByBlazeFilter(
            @ModelAttribute GenericFilterRequest filter) {
        return RestResponseUtils.successResponse(
                ResponseUtils.pagedSuccess(itemService.getAllItemsWithBlazeFilter(filter), filter)

        );
    }
}
