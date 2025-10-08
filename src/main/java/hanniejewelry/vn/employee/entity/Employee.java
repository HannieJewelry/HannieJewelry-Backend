package hanniejewelry.vn.employee.entity;

import hanniejewelry.vn.product.entity.SoftDeletable;
import hanniejewelry.vn.shared.domain.model.entity.AbstractAuditEntity;
import hanniejewelry.vn.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;

import java.util.UUID;

@Entity
@Table(name = "employees")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Where(clause = "is_deleted = false")
public class Employee extends AbstractAuditEntity implements SoftDeletable {
    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid")
    private UUID id;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    private User user;

    @Column(nullable = false)
    private String fullName;

    @Override
    public void setDeleted(boolean deleted) {
        this.isDeleted = deleted;
    }
    
    @Override
    public boolean isDeleted() {
        return Boolean.TRUE.equals(this.isDeleted);
    }
}
