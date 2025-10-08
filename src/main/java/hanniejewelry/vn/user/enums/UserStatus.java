package hanniejewelry.vn.user.enums;

import lombok.Getter;

@Getter
public enum UserStatus {
    DISABLED("DISABLED"),
    ENABLED("ENABLED"),
    INVITED("INVITED"),
    DECLINED("DECLINED");

    private final String value;

    UserStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static UserStatus fromValue(String value) {
        for (UserStatus s : UserStatus.values()) {
            if (s.value.equalsIgnoreCase(value)) return s;
        }
        throw new IllegalArgumentException("Invalid status: " + value);
    }
}
