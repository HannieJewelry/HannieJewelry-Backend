package hanniejewelry.vn.order.application.query.handler;

import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.view.EntityViewManager;
import com.blazebit.persistence.view.EntityViewSetting;
import hanniejewelry.vn.order.application.query.FindOrderByIdQuery;
import hanniejewelry.vn.order.application.query.view.OrderView;
import hanniejewelry.vn.order.entity.Order;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderQueryHandler {
    private final EntityViewManager evm;
    private final CriteriaBuilderFactory cbf;
    private final EntityManager em;

    @QueryHandler
    public OrderView handle(FindOrderByIdQuery query) {
        var cb = cbf.create(em, Order.class).where("id").eq(query.orderId());
        var resultList = evm.applySetting(EntityViewSetting.create(OrderView.class), cb).getResultList();
        return resultList.isEmpty() ? null : resultList.get(0);
    }
}
