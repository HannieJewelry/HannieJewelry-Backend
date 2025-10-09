package hanniejewelry.vn.shipping.infrastructure.persistence.entity;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Type;

import java.util.List;

@Entity
@Table(
        name = "provinces",
        uniqueConstraints = @UniqueConstraint(columnNames = {"code"})
)
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ProvinceEntity {

    @Id
    @NotNull
    private Integer provinceId;

    @Column(columnDefinition = "TEXT", nullable = false)
    @NotBlank
    private String name;

    @Column(unique = true, nullable = false, length = 50)
    @NotBlank
    private String code;

    @NotNull
    @Column(nullable = false)
    private Integer countryId;
}
