package hanniejewelry.vn.shipping.infrastructure.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "countries")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class CountryEntity {

    @Id
    @NotNull
    private Integer id;

    @Column(columnDefinition = "TEXT", nullable = false)
    @NotBlank
    private String name;

    @Column(unique = true, nullable = false, length = 10)
    @NotBlank
    private String code;
}
