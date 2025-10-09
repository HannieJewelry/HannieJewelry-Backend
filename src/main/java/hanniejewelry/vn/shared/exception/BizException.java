package hanniejewelry.vn.shared.exception;

import lombok.Getter;
import java.text.MessageFormat;

@Getter
public class BizException extends RuntimeException {

    private final BaseMessageType messageType;
    private final transient Object[] parameters;

    public BizException(BaseMessageType messageType) {
        super(messageType.getUserMessage());
        this.messageType = messageType;
        this.parameters = new Object[0];
    }

    public BizException(BaseMessageType messageType, String userMessageFormat, Object... parameters) {
        super(formatMessage(userMessageFormat, parameters));
        this.messageType = messageType;
        this.parameters = parameters;
    }

    public BizException(BaseMessageType messageType, Object... parameters) {
        super(formatMessage(messageType.getDevMessage(), parameters));
        this.messageType = messageType;
        this.parameters = parameters;
    }

    public BizException(BaseMessageType messageType, Throwable cause, String userMessageFormat, Object... parameters) {
        super(formatMessage(userMessageFormat, parameters), cause);
        this.messageType = messageType;
        this.parameters = parameters;
    }

    public BizException(BaseMessageType messageType, Throwable cause, Object... parameters) {
        super(formatMessage(messageType.getDevMessage(), parameters), cause);
        this.messageType = messageType;
        this.parameters = parameters;
    }

    private static String formatMessage(String pattern, Object... params) {
        if (pattern == null) return null;
        if (params == null || params.length == 0) return pattern;
        return MessageFormat.format(pattern, params);
    }

}
