package com.example.backend.exception.handler;

import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.exception.message.ErrorMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import java.time.LocalDateTime;

@ControllerAdvice
@ResponseBody
@Slf4j
public class ControllerExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ErrorMessage handleResourceNotFoundException(
            ResourceNotFoundException resourceNotFoundException
            , WebRequest webRequest){
        log.warn(resourceNotFoundException.getLocalizedMessage());
        return ErrorMessage.builder()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .dateTime(LocalDateTime.now())
                .message(resourceNotFoundException.getMessage())
                .description(webRequest.getDescription(false))
                .build();
    }



    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorMessage handleGlobalException(Exception exception, WebRequest webRequest){
        log.error(exception.getClass().getName());
        log.error(exception.getMessage());
        return new ErrorMessage(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                LocalDateTime.now(),
                exception.getMessage(),
                webRequest.getDescription(false)
        );
    }

    @ExceptionHandler({AuthenticationException.class,
            BadCredentialsException.class
    })
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public ErrorMessage handleAuthenticationFailedException(Exception exception,
                                                            WebRequest webRequest){
        log.warn(exception.getMessage());
        return new ErrorMessage(
                HttpStatus.UNAUTHORIZED.value(),
                LocalDateTime.now(),
                exception.getMessage(),
                webRequest.getDescription(false)
        );
    }
}
