package hanniejewelry.vn.shared.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@JsonPropertyOrder({"message", "code", "data", "errors"})
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public final class RestResponse<T> {

  private final String message;
  private final int code;
  private final T data;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private final List<ErrorField> errors;

  public RestResponse(final T data, final String message, final int code) {
    this(data, message, code, null);
  }

  public RestResponse(
      final T data, final String message, final int code, final List<ErrorField> errors) {
    this.message = message;
    this.code = code;
    this.data = data;
    this.errors = (errors == null) ? null : List.copyOf(errors);
  }

  public List<ErrorField> getErrors() {
    return errors == null ? null : List.copyOf(errors);
  }

  @Getter
  @AllArgsConstructor
  @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
  public static class ErrorField {
    private String field;
    private String message;
  }
}
