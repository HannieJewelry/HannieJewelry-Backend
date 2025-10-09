package hanniejewelry.vn.shared.utils;

import hanniejewelry.vn.shared.dto.RestResponse;
import hanniejewelry.vn.shared.exception.BaseMessageType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;

import java.util.List;

public final class RestResponseUtils {
    private RestResponseUtils() {}

    private static <T> ResponseEntity<RestResponse<T>> build(RestResponse<T> body, HttpStatus status) {
        return ResponseEntity.status(status).body(body);
    }

    public static <T> ResponseEntity<RestResponse<T>> successResponse(T data) {
        return build(success(data, BaseMessageType.SUCCESS.getUserMessage()), BaseMessageType.SUCCESS.getHttpStatus());
    }

    public static <T> ResponseEntity<RestResponse<T>> successResponse(T data, String message) {
        return build(success(data, message), HttpStatus.OK);
    }

    public static <T> RestResponse<T> success(T data, String message) {
        return new RestResponse<>(data, message, 200);
    }

    public static <T> ResponseEntity<RestResponse<T>> createdResponse(T data) {
        return build(created(data, BaseMessageType.CREATED.getUserMessage()), BaseMessageType.CREATED.getHttpStatus());
    }
    public static <T> RestResponse<T> created(T data, String message) {
        return new RestResponse<>(data, message, 201);
    }

    public static <T> RestResponse<T> notFound(String message) {
        return new RestResponse<>(null, message, 404, null);
    }

    public static <T> ResponseEntity<RestResponse<T>> notFoundResponse() {
        return build(notFound(BaseMessageType.NOT_FOUND.getUserMessage()), BaseMessageType.NOT_FOUND.getHttpStatus());
    }

    public static <T> ResponseEntity<RestResponse<T>> notFoundResponse(String message) {
        return build(notFound(message), HttpStatus.NOT_FOUND);
    }

    public static <T> RestResponse<T> badRequest(BindingResult bindingResult) {
        List<RestResponse.ErrorField> errors = bindingResult.getAllErrors().stream()
                .map(error -> new RestResponse.ErrorField("ValidationError", error.getDefaultMessage()))
                .toList();
        return new RestResponse<>(null, "Validation errors", 400, errors);
    }

    public static <T> ResponseEntity<RestResponse<T>> badRequestResponse(BindingResult bindingResult) {
        return build(badRequest(bindingResult), HttpStatus.BAD_REQUEST);
    }

    public static <T> RestResponse<T> errorServer(String message) {
        return new RestResponse<>(null, message, 500, null);
    }

    public static <T> ResponseEntity<RestResponse<T>> errorServerResponse(String message) {
        return build(errorServer(message), HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    public static <T> ResponseEntity<RestResponse<T>> errorResponse(String message) {
        return build(new RestResponse<>(null, message, 400, null), HttpStatus.BAD_REQUEST);
    }

    public static ResponseEntity<RestResponse<String>> handleSync(Runnable runnable, String message) {
        try {
            runnable.run();
            return successResponse(message, "Success");
        } catch (Exception e) {
            return errorServerResponse("Sync failed: " + e.getMessage());
        }
    }
}
