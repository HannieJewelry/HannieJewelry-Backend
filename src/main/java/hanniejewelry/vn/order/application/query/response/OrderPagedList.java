package hanniejewelry.vn.order.application.query.response;

import com.blazebit.persistence.PagedList;
import hanniejewelry.vn.order.application.query.view.OrderView;

public class OrderPagedList {
    private final PagedList<OrderView> pagedList;
    
    public OrderPagedList(PagedList<OrderView> pagedList) {
        this.pagedList = pagedList;
    }
    
    public PagedList<OrderView> getPagedList() {
        return pagedList;
    }
} 