package hanniejewelry.vn.shared.utils;

import java.util.Map;
import java.util.Set;

public interface ExistenceChecker {
    void invalidateCache(Class<?> entityClass, Map<String, Object> conditions);

    <ID, E> void validateAllExistOrThrow(
            Class<E> entityClass,
            Set<ID> ids,
            String errorMessageTemplate,
            Object... keyValues
    );

    <ID, E> boolean isAllExist(Class<E> entityClass, Set<ID> ids, Map<String, Object> conditions);

    <ID, E> Set<ID> findExistingIds(Class<E> entityClass, Set<ID> ids, Map<String, Object> conditions);

    <E> boolean isExist(Class<E> entityClass, Map<String, Object> conditions);

    <E> void throwIfExist(Class<E> entityClass, Map<String, Object> conditions, String message);

    <E> void throwIfNotExist(Class<E> entityClass, Map<String, Object> conditions, String message);
}
