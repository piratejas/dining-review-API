package com.piratejas.diningReviewAPI.errors;

import com.piratejas.diningReviewAPI.errors.exceptions.LoginException;
import com.piratejas.diningReviewAPI.errors.exceptions.UsernameConflictException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static com.piratejas.diningReviewAPI.utils.ErrorUtils.getErrorResponse;


@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(LoginException.class)
    public ResponseEntity<ErrorResponse> handleLoginException(LoginException ex, WebRequest request) {
        String error = ex.getMessage();
        ErrorResponse errorResponse = getErrorResponse(request, HttpStatus.UNAUTHORIZED.value(), error);
        return ResponseEntity.status(errorResponse.getStatus()).body(errorResponse);
    }

    @ExceptionHandler(UsernameConflictException.class)
    public ResponseEntity<ErrorResponse> handleUsernameConflictException(UsernameConflictException ex, WebRequest request) {
        String error = ex.getMessage();
        ErrorResponse errorResponse = getErrorResponse(request, HttpStatus.CONFLICT.value(), error);
        return ResponseEntity.status(errorResponse.getStatus()).body(errorResponse);
    }
}
