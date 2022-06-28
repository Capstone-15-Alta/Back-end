package com.capstone15.alterra.exception;

import com.capstone15.alterra.constant.AppConstant;
import com.capstone15.alterra.util.ResponseUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.nio.file.AccessDeniedException;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(UsernameNotFoundException.class)
    public final ResponseEntity<Object> handleUserNotFoundException(UsernameNotFoundException ex, WebRequest request) {
        String details = ex.getLocalizedMessage();
      return ResponseUtil.build(AppConstant.Message.NOT_FOUND, details, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {
        String details = ex.getLocalizedMessage();
        return ResponseUtil.build(AppConstant.Message.UNKNOWN_ERROR, details, HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @ExceptionHandler(AccessDeniedException.class)
    public final ResponseEntity<Object> handleAccessDeniedException(Exception ex, WebRequest request) {
        String details = ex.getLocalizedMessage();
        return ResponseUtil.build(AppConstant.Message.UNKNOWN_ERROR, details, HttpStatus.FORBIDDEN);

    }

    @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class})
    public final ResponseEntity<Object> handleConflict(RuntimeException  ex, WebRequest request) {
        String details = ex.getLocalizedMessage();
        return ResponseUtil.build(AppConstant.Message.UNKNOWN_ERROR, details, HttpStatus.CONFLICT);

    }

}
