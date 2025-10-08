package hanniejewelry.vn.shared.exception;

import org.springframework.http.HttpStatus;

public class UploadFailedException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "File upload failed. Please try again later!";

    public UploadFailedException() {
        super(DEFAULT_MESSAGE);
    }

    public UploadFailedException(Throwable cause) {
        super(DEFAULT_MESSAGE, cause);
    }

    public HttpStatus getStatus() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}