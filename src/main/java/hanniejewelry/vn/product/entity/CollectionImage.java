package hanniejewelry.vn.product.entity;

import hanniejewelry.vn.shared.domain.model.entity.AbstractAuditEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "collection_images")
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class CollectionImage extends AbstractAuditEntity {
    
    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid")
    private UUID id;
    
    @Column(name = "src", nullable = false)
    private String src;
    
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;
    
    @OneToOne(mappedBy = "image")
    private CustomCollection collection;
} 