package hanniejewelry.vn.product.entity;

import hanniejewelry.vn.shared.domain.model.entity.AbstractAuditEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Where;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "custom_collections")
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Where(clause = "is_deleted = false")
public class CustomCollection extends AbstractAuditEntity implements SoftDeletable {
    
    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid")
    private UUID id;
    
    @Column(name = "title", nullable = false)
    private String title;
    
    @Column(name = "body_html", columnDefinition = "TEXT")
    private String bodyHtml;
    
    @Column(name = "handle", unique = true)
    private String handle;
    
    @Column(name = "published", nullable = false)
    @Builder.Default
    private Boolean published = false;
    
    @Column(name = "published_at")
    private Instant publishedAt;
    
    @Column(name = "published_scope", nullable = false)
    @Builder.Default
    private String publishedScope = "web";
    
    @Column(name = "sort_order", nullable = false)
    @Builder.Default
    private String sortOrder = "alpha_asc";
    
    @Column(name = "template_suffix")
    private String templateSuffix;
    
    @OneToMany(mappedBy = "collection", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Collect> collects = new ArrayList<>();
    
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "image_id")
    private CollectionImage image;
    
    @Override
    public void setDeleted(boolean deleted) {
        this.isDeleted = deleted;
    }

    @Override
    public boolean isDeleted() {
        return Boolean.TRUE.equals(this.isDeleted);
    }
    
    public enum SortOrder {
        ALPHA_ASC("alpha_asc"),
        ALPHA_DESC("alpha_desc"),
        BEST_SELLING("best_selling"),
        CREATED("created"),
        CREATED_DESC("created_desc"),
        MANUAL("manual"),
        PRICE_ASC("price_asc"),
        PRICE_DESC("price_desc");
        
        private final String value;
        
        SortOrder(String value) {
            this.value = value;
        }
        
        public String getValue() {
            return value;
        }
        
        public static SortOrder fromValue(String value) {
            for (SortOrder sortOrder : SortOrder.values()) {
                if (sortOrder.getValue().equals(value)) {
                    return sortOrder;
                }
            }
            return ALPHA_ASC; // Default
        }
    }
} 