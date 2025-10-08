package hanniejewelry.vn.shared.exception;

import org.springframework.http.HttpStatus;
import lombok.Getter;

@Getter
public enum BaseMessageType {

    // 2xx Success
    SUCCESS("200", "Success", "Thành công", HttpStatus.OK),
    CREATED("201", "Resource created", "Tạo mới thành công", HttpStatus.CREATED),
    UPDATED("200", "Resource updated", "Cập nhật thành công", HttpStatus.OK),
    DELETED("200", "Resource deleted", "Xóa thành công", HttpStatus.OK),

    // 4xx Client error
    BAD_REQUEST("400", "Bad request", "Yêu cầu không hợp lệ", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED("401", "Unauthorized", "Chưa xác thực", HttpStatus.UNAUTHORIZED),
    FORBIDDEN("403", "Forbidden", "Không đủ quyền truy cập", HttpStatus.FORBIDDEN),
    NOT_FOUND("404", "Resource not found", "Không tìm thấy dữ liệu", HttpStatus.NOT_FOUND),
    ENTITY_ALREADY_EXISTS("409", "Entity already exists", "Dữ liệu đã tồn tại", HttpStatus.CONFLICT),
    CONFLICT("409", "Conflict", "Xung đột dữ liệu", HttpStatus.CONFLICT),
    LOCKED("423", "Resource locked", "Tài nguyên đang bị khóa", HttpStatus.LOCKED),
    VALIDATION_ERROR("400", "Validation failed", "Dữ liệu không hợp lệ", HttpStatus.BAD_REQUEST),
    INVALID_STATE("400", "Invalid state", "Trạng thái không hợp lệ", HttpStatus.BAD_REQUEST),

    // 5xx Server error
    INTERNAL_ERROR("500", "Internal server error", "Lỗi hệ thống, vui lòng thử lại sau", HttpStatus.INTERNAL_SERVER_ERROR),
    SERVICE_UNAVAILABLE("503", "Service unavailable", "Dịch vụ tạm thời không khả dụng", HttpStatus.SERVICE_UNAVAILABLE),
    UPLOAD_FAILED("500", "Upload failed", "Tải lên không thành công, vui lòng thử lại", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String code;
    private final String devMessage;
    private final String userMessage;
    private final HttpStatus httpStatus;

    BaseMessageType(String code, String devMessage, String userMessage, HttpStatus httpStatus) {
        this.code = code;
        this.devMessage = devMessage;
        this.userMessage = userMessage;
        this.httpStatus = httpStatus;
    }
}
