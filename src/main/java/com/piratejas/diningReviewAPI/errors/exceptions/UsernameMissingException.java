package com.piratejas.diningReviewAPI.errors.exceptions;

public class UsernameMissingException extends RuntimeException {
    public UsernameMissingException(String message) {
        super(message);
    }
}
