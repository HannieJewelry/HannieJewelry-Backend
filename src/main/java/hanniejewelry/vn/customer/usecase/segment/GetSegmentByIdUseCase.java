package hanniejewelry.vn.customer.usecase.segment;

import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.view.EntityViewManager;
import com.blazebit.persistence.view.EntityViewSetting;
import hanniejewelry.vn.customer.entity.CustomerSegment;
import hanniejewelry.vn.customer.view.CustomerSegmentView;
import hanniejewelry.vn.shared.exception.BizException;
import hanniejewelry.vn.shared.exception.BaseMessageType;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetSegmentByIdUseCase {
    private final EntityViewManager evm;
    private final CriteriaBuilderFactory cbf;
    private final EntityManager em;

    public CustomerSegmentView execute(Long id) {
        var setting = EntityViewSetting.create(CustomerSegmentView.class);
        var cb = cbf.create(em, CustomerSegment.class)
                .where("id").eq(id);
        
        var result = evm.applySetting(setting, cb).getResultList();
        
        if (result.isEmpty()) {
            throw new BizException(BaseMessageType.NOT_FOUND, "Segment not found");
        }
        
        return result.get(0);
    }
} 