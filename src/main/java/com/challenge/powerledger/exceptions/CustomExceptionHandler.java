package com.challenge.powerledger.exceptions;



import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Level;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import com.challenge.powerledger.models.ExceptionDTO;

import javax.validation.ConstraintViolationException;
import java.sql.SQLException;

@ControllerAdvice(annotations = RestController.class)
@Log4j2
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    enum ErrorCode {
        SERVER_ERROR,
        CLIENT_ERROR
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return customHandler(ex, request, status);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return customHandler(ex, request, status);
    }

    @ExceptionHandler({InvalidNameException.class, InvalidPostcodeException.class, ConstraintViolationException.class})
    public ResponseEntity<Object> handleClientException(Exception ex, WebRequest request) {
        return customHandler(ex, request, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleConstraintClientException(Exception ex, WebRequest request) {
        var sqlException = (SQLException) ex.getCause().getCause();
        return customHandler(sqlException, request, HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<Object> customHandler(Exception ex, WebRequest request, HttpStatus httpStatus) {
        var req = (ServletWebRequest) request;
        final ErrorCode errorCode;
        if (httpStatus.is4xxClientError()) {
            errorCode = ErrorCode.CLIENT_ERROR;
            logWarning(ex, req);
        } else {
            errorCode = ErrorCode.SERVER_ERROR;
            logError(ex, req);
        }
        var response = ExceptionDTO.builder().error(errorCode.toString()).message(ex.getMessage()).build();
        return new ResponseEntity<>(response, httpStatus);
    }

    private void logWarning(Exception ex, ServletWebRequest request) {
        logger.warn("Caught Exception: {}, Request: {}", ex::getMessage, request.getRequest()::getRequestURI);
        logger.catching(Level.DEBUG, ex);
    }

    private void logError(Exception ex, ServletWebRequest request) {
        logger.error("Caught Exception: {}, Request: {}", ex.getMessage(), request.getRequest().getRequestURI(), ex);
    }
}
