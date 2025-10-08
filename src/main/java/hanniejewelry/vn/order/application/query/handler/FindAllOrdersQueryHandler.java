package hanniejewelry.vn.order.application.query.handler;

import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.PagedList;
import com.blazebit.persistence.view.EntityViewManager;
import com.blazebit.persistence.view.EntityViewSetting;
import hanniejewelry.vn.order.application.query.FindAllOrdersQuery;
import hanniejewelry.vn.order.application.query.filter.OrderBlazeFilterApplier;
import hanniejewelry.vn.order.application.query.response.OrderPagedList;
import hanniejewelry.vn.order.application.query.view.OrderView;
import hanniejewelry.vn.order.entity.Order;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FindAllOrdersQueryHandler {
    private final EntityViewManager evm;
    private final CriteriaBuilderFactory cbf;
    private final EntityManager em;
    private final OrderBlazeFilterApplier filterApplier;

    @QueryHandler
    public OrderPagedList handle(FindAllOrdersQuery query) {
        var filter = query.filter();
        var setting = EntityViewSetting.create(OrderView.class, filter.getPage(), filter.getSize());
        var cb = cbf.create(em, Order.class);
        
        filterApplier.applyFilters(setting, filter);
        
        filterApplier.applySort(cb, filter);
        
        PagedList<OrderView> pagedList = evm.applySetting(setting, cb).getResultList();
        return new OrderPagedList(pagedList);
    }
} 