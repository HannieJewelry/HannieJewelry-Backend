package hanniejewelry.vn.shipping.infrastructure.persistence.entity;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;

import java.util.List;
import java.util.Map;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdministrativeMeta {

    private Integer status;
    private Integer isEnable;
    private String canUpdateCOD;
    private Integer pickType;
    private Integer deliverType;

    @Type(JsonType.class)
    @Column(name = "white_list_client", columnDefinition = "jsonb")
    private Map<String, List<String>> whiteListClient;

    // Tùy entity, trường này có thể là whiteListDistrict, whiteListWard...
    @Type(JsonType.class)
    @Column(name = "white_list_area", columnDefinition = "jsonb")
    private Map<String, Object> whiteListArea;

    private String reasonCode;
    private String reasonMessage;

    @Type(JsonType.class)
    @Column(name = "on_dates", columnDefinition = "jsonb")
    private Object onDates;

}
