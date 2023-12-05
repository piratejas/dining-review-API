package com.piratejas.diningReviewAPI.errors;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Data
public class ErrorResponse {
    private LocalDateTime timestamp;
    private Integer status;
    private String message;
    private String path;
    private List<String> errors;

    public void setError(String error) {
        errors = Collections.singletonList(error);
    }
}
