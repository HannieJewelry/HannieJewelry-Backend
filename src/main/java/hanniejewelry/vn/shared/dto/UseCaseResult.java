package hanniejewelry.vn.shared.dto;


public class UseCaseResult<T> {
    private final T data;
    private final String error;

    private UseCaseResult(T data, String error) {
        this.data = data;
        this.error = error;
    }

    public static <T> UseCaseResult<T> ok(T data) {
        return new UseCaseResult<>(data, null);
    }

    public static <T> UseCaseResult<T> error(String error) {
        return new UseCaseResult<>(null, error);
    }

    public boolean isOk() {
        return error == null;
    }

    public T getData() {
        return data;
    }

    public String getError() {
        return error;
    }
}
