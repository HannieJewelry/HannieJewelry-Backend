package hanniejewelry.vn.shared.dto;

import lombok.Getter;

import java.util.List;

@Getter
public final class PagedResponse<T> {
    private final Result<T> result;

    public PagedResponse(
            final List<T> content,
            final int page,
            final int size,
            final long totalElements,
            final String sortProperty,
            final String direction) {
        this.result = new Result<>(content, page, size, totalElements, sortProperty, direction);
    }

    public record Sort(String property, String direction) {
    }

    @Getter
    public static class Result<T> {
        private final List<T> content;
        private final int page;
        private final int size;
        private final long totalElements;
        private final int totalPages;
        private final List<Sort> sorts;

        public Result(
                final List<T> content,
                final int page,
                final int size,
                final long totalElements,
                final String sortProperty,
                final String direction) {
            this.content = List.copyOf(content);
            this.page = page;
            this.size = size;
            this.totalElements = totalElements;
            this.totalPages = (int) Math.ceil((double) totalElements / size);
            this.sorts = List.of(new Sort(sortProperty, direction));
        }

        public int getPage() {
            return page + 1;
        }

        public List<T> getContent() {
            return List.copyOf(content);
        }
    }
}
