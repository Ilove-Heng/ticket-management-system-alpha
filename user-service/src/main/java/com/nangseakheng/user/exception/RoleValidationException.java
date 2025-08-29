package com.nangseakheng.user.exception;

import lombok.Getter;

public class RoleValidationException extends  RuntimeException {
    @Getter
    private final String field;
    @Getter
    private final String value;

    public RoleValidationException(String field, String value) {
        // %s = params
        // field is params
        super(String.format("%s is valid", field));
        this.field = field;
        this.value = value;
    }
}
