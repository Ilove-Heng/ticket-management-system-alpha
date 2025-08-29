package com.nangseakheng.user.utils;

import com.nangseakheng.common.dto.EmptyObject;
import com.nangseakheng.common.exception.CustomMessageException;
import org.springframework.http.HttpStatus;

public class CustomMessageExceptionUtils {

    private CustomMessageExceptionUtils() {}

    public static CustomMessageException unauthorized() {

        CustomMessageException messageException = new CustomMessageException();
        messageException.setMessage("Unauthorized");
        messageException.setCode(String.valueOf(HttpStatus.UNAUTHORIZED.value()));
        messageException.setObject(new EmptyObject());
        messageException.setHttpStatus(HttpStatus.UNAUTHORIZED);
        return messageException;
    }

}
