package hanniejewelry.vn.user.enums;



import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.modulith.NamedInterface;

import java.util.Arrays;

@NamedInterface
public enum UserType {
    USER("user"),
    EMPLOYEE("employee");

    private final String value;

    UserType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static UserType fromValue(String value) {
        return Arrays.stream(UserType.values())
                .filter(type -> type.value.equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Loại tài khoản không hợp lệ (user/employee)"));
    }
}
