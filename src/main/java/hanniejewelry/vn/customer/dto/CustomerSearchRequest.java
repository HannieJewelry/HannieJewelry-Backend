package hanniejewelry.vn.customer.dto;

import hanniejewelry.vn.shared.dto.GenericFilterRequest;

public class CustomerSearchRequest extends GenericFilterRequest {
    /**
     * Nếu true, chỉ lấy khách hàng có đơn chưa hoàn tất trong 7 ngày qua
     */
    private boolean hasUnfinishedOrderIn7Days;

    /**
     * Nếu true, chỉ lấy khách hàng có đơn chưa hoàn tất trong 30 ngày qua
     */
    private boolean hasUnfinishedOrderIn30Days;

    public boolean isHasUnfinishedOrderIn7Days() {
        return hasUnfinishedOrderIn7Days;
    }

    public void setHasUnfinishedOrderIn7Days(boolean hasUnfinishedOrderIn7Days) {
        this.hasUnfinishedOrderIn7Days = hasUnfinishedOrderIn7Days;
    }

    public boolean isHasUnfinishedOrderIn30Days() {
        return hasUnfinishedOrderIn30Days;
    }

    public void setHasUnfinishedOrderIn30Days(boolean hasUnfinishedOrderIn30Days) {
        this.hasUnfinishedOrderIn30Days = hasUnfinishedOrderIn30Days;
    }
}
