package com.nangseakheng.user.exception;

import lombok.Getter;

import java.io.Serial;

public class UserValidationException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    @Getter
    private final String field;
    @Getter
    private final String value;

    public UserValidationException(String field, String value) {
        // %s = params
        // field is params
        super(String.format("Username %s is valid", field));
        this.field = field;
        this.value = value;
    }
}
