package com.nangseakheng.user.exception;

import java.io.Serial;

public class UsernameValidationException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    private final String field;
    private final String value;

    public UsernameValidationException(String field, String value) {
        // %s = params
        // field is params
        super(String.format("Username %s is valid", field));
        this.field = field;
        this.value = value;
    }

    public String getField() {
        return field;
    }

    public String getValue() {
        return value;
    }
}
