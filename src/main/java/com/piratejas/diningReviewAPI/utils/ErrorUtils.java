package com.piratejas.diningReviewAPI.utils;

import com.piratejas.diningReviewAPI.errors.ErrorResponse;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.List;

public class ErrorUtils {

    public static ErrorResponse getErrorResponse(WebRequest request, Integer statusCode, List<String> errors) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(LocalDateTime.now());
        errorResponse.setStatus(statusCode);
        errorResponse.setMessage(getMessage(statusCode));
        errorResponse.setPath(((ServletWebRequest)request).getRequest().getRequestURI());
        errorResponse.setErrors(errors);

        return errorResponse;
    }

    public static ErrorResponse getErrorResponse(WebRequest request, Integer statusCode, String error) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(LocalDateTime.now());
        errorResponse.setStatus(statusCode);
        errorResponse.setMessage(getMessage(statusCode));
        errorResponse.setPath(((ServletWebRequest)request).getRequest().getRequestURI());
        errorResponse.setError(error);

        return errorResponse;
    }

    public static String getMessage(Integer status) {

        return switch (status) {
            case 400 -> "Bad Request";
            case 401 -> "Unauthorized";
            case 403 -> "Forbidden";
            case 404 -> "Not Found";
            case 405 -> "Method not allowed";
            case 409 -> "Conflict";
            case 410 -> "Gone";
            default -> "Internal server error";
        };
    }
}
