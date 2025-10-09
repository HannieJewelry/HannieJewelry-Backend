package hanniejewelry.vn.customer.entity;

import hanniejewelry.vn.shared.domain.model.entity.AbstractAuditEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Entity
@Table(name = "customer_segments")
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class CustomerSegment extends AbstractAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private SegmentType type;

    @Column(name = "definition", columnDefinition = "TEXT")
    private String definition;

    @ManyToMany(mappedBy = "segments")
    private Set<Customer> customers;

    public enum SegmentType {
        AUTOMATIC("Automatic"),
        STATIC("Static");

        private final String value;

        SegmentType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
} 