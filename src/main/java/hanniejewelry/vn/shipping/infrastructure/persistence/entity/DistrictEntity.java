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
        name = "districts",
        uniqueConstraints = @UniqueConstraint(columnNames = {"code"})
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class DistrictEntity {

    @Id
    @NotNull
    private Integer districtId;

    @NotNull
    @Column(nullable = false)
    private Integer provinceId;

    @Column(columnDefinition = "TEXT", nullable = false)
    @NotBlank
    private String name;

    @Column(unique = true, nullable = false, length = 50)
    @NotBlank
    private String code;
}
