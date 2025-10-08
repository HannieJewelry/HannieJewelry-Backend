package hanniejewelry.vn.order.domain.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Trạng thái xử lý đơn hàng (order_processing_status) trong hệ thống.
 */
public enum OrderProcessingStatus {
    /**
     * Đơn hàng vừa tạo, đang chờ xử lý.
     */
    PENDING,

    /**
     * Đơn hàng đang trong quá trình xử lý (ví dụ: chuẩn bị hàng, đóng gói, xác nhận...).
     */
    PROCESSING,

    /**
     * Đơn hàng đã được xác nhận bởi admin/staff.
     */
    CONFIRMED,

    /**
     * Tự giao hàng: Cửa hàng tự vận chuyển, không dùng đơn vị vận chuyển bên ngoài.
     */
    SELF_DELIVERY,

    /**
     * Đơn hàng đã xử lý hoàn tất (đã giao hàng hoặc hoàn thành toàn bộ quy trình).
     */
    COMPLETED,

    /**
     * Đơn hàng xử lý thất bại (ví dụ: không liên lạc được khách, không đủ hàng, hoặc lý do khác).
     */
    FAILED,
    
    /**
     * Đơn hàng đã bị hủy.
     */
    CANCEL;

    @JsonValue
    public String toJson() {
        return name().toLowerCase();
    }
}
