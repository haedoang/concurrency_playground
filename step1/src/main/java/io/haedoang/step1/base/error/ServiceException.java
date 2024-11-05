package io.haedoang.step1.base.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class ServiceException extends RuntimeException {
    private final ErrorCode code;

    public ServiceException(ErrorCode code) {
        this.code = code;
    }

    public ServiceException(ErrorCode code, String message) {
        super(message);
        this.code = code;
    }
}
