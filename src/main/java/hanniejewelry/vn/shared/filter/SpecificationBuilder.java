package hanniejewelry.vn.shared.filter;

import org.springframework.data.jpa.domain.Specification;
import java.util.List;
import java.util.UUID;

public class SpecificationBuilder {

    public static <T> Specification<T> build(List<UUID> ids, String idField, String filterStr, Class<T> clazz) {
        Specification<T> spec = null;

        if (ids != null && !ids.isEmpty() && idField != null && !idField.isBlank()) {
            spec = (root, query, cb) -> root.get(idField).in(ids);
        }

        if (filterStr != null && !filterStr.isBlank()) {
            Specification<T> rsqlSpec = io.github.perplexhub.rsql.RSQLJPASupport.toSpecification(filterStr);
            spec = (spec == null) ? rsqlSpec : spec.and(rsqlSpec);
        }

        return spec;
    }

    public static <T> Specification<T> build(String filterStr, Class<T> clazz) {
        return build(null, null, filterStr, clazz);
    }
}
