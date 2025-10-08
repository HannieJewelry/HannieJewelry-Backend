package hanniejewelry.vn.shipping.infrastructure.persistence.entity;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Type;

import java.util.List;
import java.util.Map;
@Entity
@Table(
        name = "wards",
        uniqueConstraints = @UniqueConstraint(columnNames = {"code"})
)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class WardEntity {

    @Id
    @NotBlank
    @Column(length = 50)
    private String code;

    @NotNull
    @Column(nullable = false)
    private Integer districtId;

    @NotBlank
    @Column(nullable = false)
    private String name;
}
