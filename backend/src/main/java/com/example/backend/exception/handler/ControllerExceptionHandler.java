package com.example.backend.exception.handler;

import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.exception.message.ErrorMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import java.time.LocalDateTime;

/**
 * Class for handling exceptions
 */
@ControllerAdvice
@ResponseBody
@Slf4j
public class ControllerExceptionHandler {

    /**
     * @param exception is ResourceNotFound exception
     * @param request is WebRequest
     * @return is object with message, Http status code value and date
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ErrorMessage handleResourceNotFoundException(
            ResourceNotFoundException exception
            , WebRequest request){
        log.warn(exception.getLocalizedMessage());
        return ErrorMessage.builder()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .dateTime(LocalDateTime.now())
                .message(exception.getMessage())
                .description(request.getDescription(false))
                .build();
    }

    /**
     * @param exception is ArgumentNotValidException
     * @param request is WebRequest
     * @return is object with validation messages, Http Status and date
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorMessage handleArgumentNotValidException(
            MethodArgumentNotValidException exception,
            WebRequest request){
        StringBuilder builder = new StringBuilder();
        exception.getBindingResult().getAllErrors().forEach((error)-> builder.append(error.getDefaultMessage()));
        return ErrorMessage.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .dateTime(LocalDateTime.now())
                .message(builder.toString())
                .description(request.getDescription(false))
                .build();
    }


    /**
     * @param exception is unknown type exception
     * @param request is WebRequest
     * @return is object with Http Status, message and date
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorMessage handleGlobalException(Exception exception, WebRequest request){
        log.error(exception.getClass().getName());
        log.error(exception.getMessage());
        return new ErrorMessage(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                LocalDateTime.now(),
                exception.getMessage(),
                request.getDescription(false)
        );
    }


}
