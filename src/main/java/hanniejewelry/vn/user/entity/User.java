package hanniejewelry.vn.user.entity;

import hanniejewelry.vn.customer.entity.Customer;
import hanniejewelry.vn.employee.entity.Employee;
import hanniejewelry.vn.product.entity.SoftDeletable;
import hanniejewelry.vn.shared.domain.model.entity.AbstractAuditEntity;
import hanniejewelry.vn.user.enums.UserType;
import hanniejewelry.vn.user.enums.UserStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.Instant;
import java.util.*;

@Entity
@Table(name = "users")
@Setter
@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Where(clause = "is_deleted = false")
public class User extends AbstractAuditEntity implements UserDetails, SoftDeletable {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid")
    private UUID id;

    @Column(nullable = false, unique = true)
    private String email;

    private String password;

    @Column(name = "phone", unique = true)
    private String phone;

    private boolean isOwner = false;

    @Column(name = "verified", nullable = false)
    @Builder.Default
    private boolean verified = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_type", nullable = false, length = 20)
    @Builder.Default
    private UserType userType = UserType.USER;

    private Instant lastLoginDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    @Builder.Default
    private UserStatus status = UserStatus.ENABLED;

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    private Employee employee;

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    private Customer customer;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @Builder.Default
    private Set<Role> roles = new HashSet<>();

    // ----------- Spring Security -----------
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();
        if (roles != null) {
            authorities.addAll(roles);
            for (Role role : roles) {
                if (role.getPermissions() != null) {
                    for (String perm : role.getPermissions()) {
                        authorities.add(new SimpleGrantedAuthority(perm));
                    }
                }
            }
        }
        return authorities;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return !Boolean.TRUE.equals(isDeleted);
    }

    @Override
    public boolean isAccountNonLocked() {
        return status == UserStatus.ENABLED;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !Boolean.TRUE.equals(isDeleted);
    }

    @Override
    public boolean isEnabled() {
        return !Boolean.TRUE.equals(isDeleted) && status == UserStatus.ENABLED;
    }

    @Override
    public void setDeleted(boolean deleted) {
        this.isDeleted = deleted;
    }
    
    @Override
    public boolean isDeleted() {
        return Boolean.TRUE.equals(this.isDeleted);
    }
}
