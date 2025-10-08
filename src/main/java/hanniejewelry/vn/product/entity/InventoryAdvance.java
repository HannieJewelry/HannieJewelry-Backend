package hanniejewelry.vn.product.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Embeddable
@Getter @Setter @SuperBuilder(toBuilder = true)
@NoArgsConstructor @AllArgsConstructor
public class InventoryAdvance {
    @Builder.Default
    @Column(name = "qty_available")
    private Integer qtyAvailable = 0;

    @Builder.Default
    @Column(name = "qty_onhand")
    private Integer qtyOnhand = 0;

    @Builder.Default
    @Column(name = "qty_commited")
    private Integer qtyCommited = 0;

    @Builder.Default
    @Column(name = "qty_incoming")
    private Integer qtyIncoming = 0;
}
