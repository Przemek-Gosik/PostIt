package com.example.backend.exception.handler;

import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.exception.message.ErrorMessage;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
                .message("Resource not found!")
                .details(Collections.singletonList(exception.getMessage()))
                .path(request.getDescription(false))
                .build();
    }

    /**
     * @param exception is ArgumentNotValidException
     * @param request is WebRequest
     * @return is object with validation messages, Http Status and date
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessage> handleArgumentNotValidException(
            MethodArgumentNotValidException exception,
            WebRequest request){
        List<String> fieldErrors = exception.getBindingResult().getFieldErrors()
                .stream().map(FieldError::getDefaultMessage).collect(Collectors.toList());

        ErrorMessage errorMessage=ErrorMessage.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .dateTime(LocalDateTime.now())
                .message("Validation failed for request")
                .details(fieldErrors)
                .path(request.getDescription(false))
                .build();
        return new ResponseEntity<>(errorMessage,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorMessage> handleConstraintViolationException(
            ConstraintViolationException exception,
            WebRequest request){
        List<String> fieldErrors = new ArrayList<>();
        exception.getConstraintViolations().forEach(violation -> fieldErrors.add(violation.getPropertyPath()+": "+violation.getMessage()));
        ErrorMessage errorMessage = ErrorMessage.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message("Constraint violation")
                .details(fieldErrors)
                .dateTime(LocalDateTime.now())
                .path(request.getDescription(false))
                .build();
        return new ResponseEntity<>(errorMessage,HttpStatus.BAD_REQUEST);
    }

    /**
     * Method
     *
     * @param exception is of types included in annotation
     * @param request is WebRequest
     * @return is object with Http status code, error type, error details and route path
     */
    @ExceptionHandler({MethodArgumentTypeMismatchException.class,
            HttpRequestMethodNotSupportedException.class,
            HttpMessageNotReadableException.class,
            HttpMessageConversionException.class})
    public ResponseEntity<ErrorMessage> handleMethodArgumentMismatchException(
            Exception exception,WebRequest request
    ){
        ErrorMessage errorMessage = ErrorMessage.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .dateTime(LocalDateTime.now())
                .message(exception.getClass().getSimpleName())
                .details(Collections.singletonList(exception.getMessage()))
                .path(request.getDescription(false))
                .build();
        return new ResponseEntity<>(errorMessage,HttpStatus.BAD_REQUEST);
    }


    /**
     * Handler for 404 error
     *
     * @return html code
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<String> handleNoControllerException(WebRequest request) {
        String page =  "<body>\n" +
                "    <h1>Route not found</h1>\n" +
                "<p>For:"+request.getDescription(false)
                +"</p>"+
                "</body>";
        return new ResponseEntity<>(page,HttpStatus.NOT_FOUND);
    }


    /**
     * @param exception is unknown type exception
     * @param request is WebRequest
     * @return is object with Http Status, message and date
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> handleGlobalException(Exception exception, WebRequest request){
        log.error("Unknown exception occurred: "+exception.getClass().getName());
        log.error(exception.getMessage());
        ErrorMessage errorMessage = ErrorMessage.builder()
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .dateTime(LocalDateTime.now())
                .message(exception.getClass().getSimpleName())
                .details(Collections.singletonList(exception.getMessage()))
                .path(request.getDescription(false))
                .build();
        return new ResponseEntity<>(errorMessage,HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
