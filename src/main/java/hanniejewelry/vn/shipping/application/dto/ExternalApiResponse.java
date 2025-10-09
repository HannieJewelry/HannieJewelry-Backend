package hanniejewelry.vn.shipping.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


@Data
public class ExternalApiResponse<T> {
    
    private Integer code;
    
    private String message;
    
    private T data;
    
    private T countries;
    
    private T country;
    
    private T districts;

    private T wards;

    public T getData() {
        if (data != null) return data;
        if (countries != null) return countries;
        if (country != null) return country;
        if (districts != null) return districts;
        if (wards != null) return wards;
        return null;
    }

} 