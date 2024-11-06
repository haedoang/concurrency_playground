package io.haedoang.step1.base.advice;

import io.haedoang.step1.base.error.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(ServiceException.class)
    public void handleError(ServiceException error) {
        log.error("ERROR!! {} {}", error.getCode(), error.getMessage());
    }
}
