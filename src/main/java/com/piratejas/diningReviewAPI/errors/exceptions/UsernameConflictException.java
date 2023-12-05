package com.piratejas.diningReviewAPI.errors.exceptions;

public class UsernameConflictException extends RuntimeException {
    public UsernameConflictException(String message) {
        super(message);
    }
}
