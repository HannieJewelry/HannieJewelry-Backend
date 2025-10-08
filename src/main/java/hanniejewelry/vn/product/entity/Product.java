package hanniejewelry.vn.product.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import hanniejewelry.vn.shared.domain.model.entity.AbstractAuditEntity;

import java.time.Instant;
import java.util.*;

@Entity
@Table(name = "product")
@Getter @Setter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor @AllArgsConstructor
public class Product extends AbstractAuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(name = "body_html", columnDefinition = "TEXT")
    private String bodyHtml;

    @Column(name = "body_plain", columnDefinition = "TEXT")
    private String bodyPlain;

    private String vendor;

    @Column(name = "product_type")
    private String productType;

    private String tags;

    @Column(name = "template_suffix")
    private String templateSuffix;

    @Column(name = "published_at")
    private Instant publishedAt;

    @Column(name = "published_scope")
    private String publishedScope;

    @Column(name = "only_hide_from_list", nullable = false)
    @Builder.Default
    private boolean onlyHideFromList = false;

    @Column(name = "not_allow_promotion", nullable = false)
    @Builder.Default
    private boolean notAllowPromotion = false;


    @Column(name = "handle", unique = true)
    private String handle;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<ProductVariant> variants = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<ProductOption> options = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<ProductImage> images = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Collect> collects = new ArrayList<>();
}
