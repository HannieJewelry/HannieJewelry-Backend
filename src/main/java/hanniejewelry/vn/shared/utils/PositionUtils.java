package hanniejewelry.vn.shared.utils;

import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.BiConsumer;

/**
 * Utility class for handling position-related operations.
 */
@UtilityClass
public class PositionUtils {

    /**
     * Reorders a list of items based on requested positions.
     * Items will be sorted according to their requested positions,
     * and then positions will be reassigned sequentially starting from 1.
     *
     * @param items List of items to reorder
     * @param requestedPositions Map of item IDs to their requested positions
     * @param idGetter Function to get the ID from an item
     * @param positionGetter Function to get the current position from an item
     * @param positionSetter BiConsumer to set a new position on an item
     * @param <T> Type of items in the list
     * @param <ID> Type of the item ID
     * @return A new list with items in the correct order and positions reassigned
     */
    public <T, ID> List<T> reorderByPosition(
            List<T> items,
            Map<ID, Integer> requestedPositions,
            Function<T, ID> idGetter,
            Function<T, Integer> positionGetter,
            BiConsumer<T, Integer> positionSetter
    ) {
        if (items == null || items.isEmpty() || requestedPositions == null || requestedPositions.isEmpty()) {
            return items;
        }

        // Create a copy of the list to avoid modifying the original
        List<T> sortedItems = new ArrayList<>(items);
        
        // Sort items based on requested positions or current positions if not specified
        sortedItems.sort((item1, item2) -> {
            ID id1 = idGetter.apply(item1);
            ID id2 = idGetter.apply(item2);
            Integer pos1 = requestedPositions.getOrDefault(id1, positionGetter.apply(item1));
            Integer pos2 = requestedPositions.getOrDefault(id2, positionGetter.apply(item2));
            return pos1.compareTo(pos2);
        });
        
        // Reassign positions to ensure they are sequential starting from 1
        for (int i = 0; i < sortedItems.size(); i++) {
            positionSetter.accept(sortedItems.get(i), i + 1);
        }
        
        return sortedItems;
    }
    
    /**
     * Reorders a list of items based on their position property.
     * Items will be sorted by position, and then positions will be reassigned sequentially starting from 1.
     *
     * @param items List of items to reorder
     * @param positionGetter Function to get the position from an item
     * @param positionSetter BiConsumer to set a new position on an item
     * @param <T> Type of items in the list
     * @return A new list with items in the correct order and positions reassigned
     */
    public <T> List<T> reorderByPosition(
            List<T> items,
            Function<T, Integer> positionGetter,
            BiConsumer<T, Integer> positionSetter
    ) {
        if (items == null || items.isEmpty()) {
            return items;
        }

        // Create a copy of the list to avoid modifying the original
        List<T> sortedItems = new ArrayList<>(items);
        
        // Sort items based on their position
        sortedItems.sort(Comparator.comparing(positionGetter));
        
        // Reassign positions to ensure they are sequential starting from 1
        for (int i = 0; i < sortedItems.size(); i++) {
            positionSetter.accept(sortedItems.get(i), i + 1);
        }
        
        return sortedItems;
    }
} 