package hanniejewelry.vn.inventory.view;

import java.math.BigDecimal;

public interface InventorySummaryView {
    
    BigDecimal getTotalAmount();
    
    Integer getTotalOnHand();
    
    BigDecimal getTotalPrice();
    
    BigDecimal getTotalProfit();
} 