package hanniejewelry.vn.product.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class ItemRequest {
  @JsonProperty(required = true)
  private String name;

  private String description;


}
