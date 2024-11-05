package io.haedoang.step1.base.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@Getter
@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class ServiceException extends RuntimeException {
    private final ErrorCode code;

    public ServiceException(ErrorCode code) {
        super(code.name());
        this.code = code;
    }

    public ServiceException(ErrorCode code, String message) {
        super(message);
        this.code = code;
    }
}
