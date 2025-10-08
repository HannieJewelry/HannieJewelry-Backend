package hanniejewelry.vn.shared.exception;

import hanniejewelry.vn.shared.dto.RestResponse;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolationException;
import hanniejewelry.vn.shared.utils.RestResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Slf4j
@RestControllerAdvice
public final class GlobalExceptionHandler {

  private final Environment env;

  public GlobalExceptionHandler(Environment env) {
    this.env = env;
  }

  private boolean isDev() {
    return env != null && (
            List.of(env.getActiveProfiles()).contains("dev") ||
                    List.of(env.getActiveProfiles()).contains("local") ||
                    List.of(env.getActiveProfiles()).contains("test")
    );
  }

  private void logError(Throwable ex) {
    log.error("EXCEPTION CAUGHT", ex);
  }

  @ExceptionHandler(UploadFailedException.class)
  public ResponseEntity<?> handleUploadFailedException(UploadFailedException ex) {
    return ResponseEntity
            .status(ex.getStatus())
            .body(Map.of(
                    "code", "UPLOAD_FAILED",
                    "message", ex.getMessage()
            ));
  }

  @ExceptionHandler(AuthorizationDeniedException.class)
  public Map<String, Object> handleAuthorizationDeniedException(AuthorizationDeniedException ex, HttpServletResponse response) {
    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
    Map<String, Object> result = new HashMap<>();
    result.put("code", 403);
    result.put("message", "Bạn không có quyền truy cập chức năng này!");
    return result;
  }

  @ExceptionHandler(UnprocessableEntityException.class)
  public ResponseEntity<?> handleUnprocessableEntity(UnprocessableEntityException ex) {
    return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
            .body(Map.of("errors", ex.getMessage()));
  }


  @ExceptionHandler({
          MethodArgumentTypeMismatchException.class,
          IllegalArgumentException.class,
          NoSuchElementException.class
  })
  public ResponseEntity<?> handleNotFoundException() {
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(RestResponseUtils.notFound("Không tìm thấy tài nguyên"));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<RestResponse<Object>> handleValidation(MethodArgumentNotValidException ex) {
    logError(ex);
    final BindingResult result = ex.getBindingResult();
    final List<RestResponse.ErrorField> errors =
            result.getFieldErrors().stream()
                    .map(e -> new RestResponse.ErrorField(e.getField(), e.getDefaultMessage()))
                    .toList();

    return ResponseEntity.badRequest()
            .body(new RestResponse<>(null, "Dữ liệu không hợp lệ", 400, errors));
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<RestResponse<Object>> handleConstraintViolation(ConstraintViolationException ex) {
    logError(ex);
    final List<RestResponse.ErrorField> errors =
            ex.getConstraintViolations().stream()
                    .map(v -> new RestResponse.ErrorField("ConstraintViolation", v.getMessage()))
                    .toList();

    return ResponseEntity.badRequest()
            .body(new RestResponse<>(null, "Constraint violations", 400, errors));
  }

  @ExceptionHandler(NoHandlerFoundException.class)
  public ResponseEntity<RestResponse<Object>> handleNotFound(NoHandlerFoundException ex) {
    logError(ex);
    final String path = ex.getRequestURL();
    final String message = "Đường dẫn không hợp lệ: " + path;

    return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new RestResponse<>(
                    null,
                    message,
                    404,
                    List.of(new RestResponse.ErrorField("url", "Không tìm thấy đường dẫn: " + path))
            ));
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<RestResponse<Object>> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
    logError(ex);
    return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(RestResponseUtils.errorServer("Dữ liệu không hợp lệ hoặc đã tồn tại"));
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<RestResponse<Object>> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
    logError(ex);
    Throwable cause = ex.getMostSpecificCause();
    String userFriendlyMessage = "Dữ liệu JSON không hợp lệ";
    String field = "JSONParseError";

    if (cause instanceof com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException propertyEx) {
      field = propertyEx.getPropertyName();
      userFriendlyMessage = "Request chứa trường không hợp lệ: " + field;
      return ResponseEntity.badRequest()
              .body(new RestResponse<>(
                      null,
                      userFriendlyMessage,
                      400,
                      List.of(new RestResponse.ErrorField(field, "Trường này không được phép gửi lên"))
              ));
    }

    return ResponseEntity.badRequest()
            .body(new RestResponse<>(
                    null,
                    userFriendlyMessage,
                    400,
                    List.of(new RestResponse.ErrorField("json", isDev() ? cause.getMessage() : "Cấu trúc hoặc kiểu dữ liệu không hợp lệ"))
            ));
  }

  @ExceptionHandler(BizException.class)
  public ResponseEntity<?> handleCommonException(BizException ex) {
    if (isDev()) {
      log.warn("Business Exception: {} - params: {}", ex.getMessageType().getDevMessage(), ex.getParameters(), ex);
    } else {
      log.warn("Business Exception: {} - params: {}", ex.getMessageType().getDevMessage(), ex.getParameters());
    }
    return ResponseEntity
            .status(ex.getMessageType().getHttpStatus())
            .body(Map.of(
                    "code", ex.getMessageType().getCode(),
                    "message", ex.getMessage()
            ));
  }


  @ExceptionHandler(Exception.class)
  public ResponseEntity<?> handleUnknownException(Exception ex) {
    logError(ex);
    return ResponseEntity
            .status(BaseMessageType.INTERNAL_ERROR.getHttpStatus())
            .body(Map.of(
                    "code", BaseMessageType.INTERNAL_ERROR.getCode(),
                    "message", BaseMessageType.INTERNAL_ERROR.getUserMessage()
            ));
  }
}
