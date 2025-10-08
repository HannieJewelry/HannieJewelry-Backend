package hanniejewelry.vn.shared.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import hanniejewelry.vn.shared.exception.BaseMessageType;
import hanniejewelry.vn.shared.exception.BizException;
import hanniejewelry.vn.shared.utils.ExistenceChecker;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExistenceCheckerImpl implements ExistenceChecker {

    private static final Logger log = LoggerFactory.getLogger(ExistenceCheckerImpl.class);

    private final EntityManager em;

    private final Cache<String, Set<Object>> cache = Caffeine.newBuilder()
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .maximumSize(10_000)
            .build();

    private final Cache<String, Boolean> existCache = Caffeine.newBuilder()
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .maximumSize(10_000)
            .build();

    @Override
    public <ID, E> void validateAllExistOrThrow(
            Class<E> entityClass,
            Set<ID> ids,
            String errorMessageTemplate,
            Object... keyValues
    ) {
        if (ids == null || ids.isEmpty()) return;

        if (keyValues.length % 2 != 0) {
            throw new IllegalArgumentException("KeyValues must be in pairs");
        }
        Map<String, Object> conditions = new HashMap<>();
        for (int i = 0; i < keyValues.length; i += 2) {
            conditions.put((String) keyValues[i], keyValues[i + 1]);
        }

        if (!isAllExist(entityClass, ids, conditions)) {
            Set<ID> validIds = findExistingIds(entityClass, ids, conditions);
            ids.stream()
                    .filter(id -> !validIds.contains(id))
                    .findFirst()
                    .ifPresent(invalidId -> {
                        throw new BizException(
                                BaseMessageType.NOT_FOUND,
                                String.format(errorMessageTemplate, invalidId)
                        );
                    });
        }
    }

    @Override
    public <ID, E> boolean isAllExist(Class<E> entityClass, Set<ID> ids, Map<String, Object> conditions) {
        if (ids == null || ids.isEmpty()) return true;
        Set<ID> validIds = findExistingIds(entityClass, ids, conditions);
        return validIds.containsAll(ids);
    }

    @Override
    public <ID, E> Set<ID> findExistingIds(Class<E> entityClass, Set<ID> ids, Map<String, Object> conditions) {
        if (ids == null || ids.isEmpty()) return Set.of();

        String cacheKey = generateCacheKey(entityClass, conditions, ids);

        Set<Object> cachedIds = cache.getIfPresent(cacheKey);
        if (cachedIds != null) {
            log.info("[EXISTENCE_CACHE HIT] key={}", cacheKey);
            //noinspection unchecked
            return (Set<ID>) cachedIds;
        }

        log.info("[EXISTENCE_CACHE MISS] key={}", cacheKey);

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<E> cq = cb.createQuery(entityClass);
        Root<E> root = cq.from(entityClass);

        Predicate predicate = root.get("id").in(ids);

        if (conditions != null && !conditions.isEmpty()) {
            for (Map.Entry<String, Object> entry : conditions.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();

                if (key.contains(".")) {
                    String[] parts = key.split("\\.");
                    jakarta.persistence.criteria.Path<Object> path = root.get(parts[0]);
                    for (int i = 1; i < parts.length; i++) {
                        path = path.get(parts[i]);
                    }
                    predicate = cb.and(predicate, cb.equal(path, value));
                } else {
                    predicate = cb.and(predicate, cb.equal(root.get(key), value));
                }
            }
        }

        cq.select(root).where(predicate);

        List<E> results = em.createQuery(cq).getResultList();

        Set<ID> validIds = results.stream()
                .map(e -> (ID) em.getEntityManagerFactory().getPersistenceUnitUtil().getIdentifier(e))
                .collect(Collectors.toSet());

        cache.put(cacheKey, new HashSet<>(validIds));
        log.info("[EXISTENCE_CACHE PUT] key={}, value={}", cacheKey, validIds);

        return validIds;
    }

    @Override
    public <E> boolean isExist(Class<E> entityClass, Map<String, Object> conditions) {
        String cacheKey = generateExistCacheKey(entityClass, conditions);

        Boolean cached = existCache.getIfPresent(cacheKey);
        if (cached != null) {
            log.info("[EXIST_CACHE HIT] key={}, value={}", cacheKey, cached);
            return cached;
        }

        log.info("[EXIST_CACHE MISS] key={}", cacheKey);

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<E> root = cq.from(entityClass);

        List<Predicate> predicates = new ArrayList<>();
        if (conditions != null) {
            for (Map.Entry<String, Object> entry : conditions.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();

                if (key.contains(".")) {
                    String[] parts = key.split("\\.");
                    jakarta.persistence.criteria.Path<Object> path = root.get(parts[0]);
                    for (int i = 1; i < parts.length; i++) {
                        path = path.get(parts[i]);
                    }
                    predicates.add(cb.equal(path, value));
                } else {
                    predicates.add(cb.equal(root.get(key), value));
                }
            }
        }

        cq.select(cb.count(root)).where(predicates.toArray(new Predicate[0]));
        Long count = em.createQuery(cq).getSingleResult();
        boolean result = count != null && count > 0;

        existCache.put(cacheKey, result);
        log.info("[EXIST_CACHE PUT] key={}, value={}", cacheKey, result);

        return result;
    }

    @Override
    public <E> void throwIfExist(Class<E> entityClass, Map<String, Object> conditions, String message) {
        if (isExist(entityClass, conditions)) {
            throw new BizException(BaseMessageType.ENTITY_ALREADY_EXISTS, message);
        }
    }

    @Override
    public <E> void throwIfNotExist(Class<E> entityClass, Map<String, Object> conditions, String message) {
        if (!isExist(entityClass, conditions)) {
            throw new BizException(BaseMessageType.NOT_FOUND, message);
        }
    }

    @Override
    public void invalidateCache(Class<?> entityClass, Map<String, Object> conditions) {
        log.info("[EXISTENCE_CACHE INVALIDATE] entity={}, conditions={}", entityClass.getName(), conditions);
        cache.invalidateAll();
        existCache.invalidateAll();
    }

    private <ID> String generateCacheKey(Class<?> entityClass, Map<String, Object> conditions, Set<ID> ids) {
        StringBuilder sb = new StringBuilder(entityClass.getName());
        if (conditions != null && !conditions.isEmpty()) {
            conditions.entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .forEach(e -> sb.append("|").append(e.getKey()).append("=").append(e.getValue()));
        }
        sb.append("|ids=").append(ids.stream().sorted().map(Object::toString).collect(Collectors.joining(",")));
        return sb.toString();
    }

    private String generateExistCacheKey(Class<?> entityClass, Map<String, Object> conditions) {
        StringBuilder sb = new StringBuilder(entityClass.getName());
        if (conditions != null && !conditions.isEmpty()) {
            conditions.entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .forEach(e -> sb.append("|").append(e.getKey()).append("=").append(e.getValue()));
        }
        return sb.toString();
    }
}
