package hanniejewelry.vn.shared.dto;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class GenericFilterRequest {
    @Min(0)
    private int page = 0;

    @Min(1)
    @Max(100)
    private int size = 10;

    private String sortProperty = "createdAt";
    private String direction = "DESC";

    public void setPage(int page) {
        this.page = (page <= 1) ? 0 : page - 1;
    }

    private Map<String, String> filters = new HashMap<>();
}
