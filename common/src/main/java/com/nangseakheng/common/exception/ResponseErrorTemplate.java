package com.nangseakheng.common.exception;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.nangseakheng.common.dto.Metadata;

import java.util.HashMap;
import java.util.Map;

public record ResponseErrorTemplate(
        @JsonProperty("message") String message,
        @JsonProperty("code") String code,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonProperty("data") Object data,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonProperty("metadata") Metadata metadata,
        @JsonIgnore boolean isError,
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        @JsonProperty("errors")Map<String, String> errors
        ) {
    public ResponseErrorTemplate(String message, String code, Object data, boolean isError) {
        this(message, code, data, null, isError, new HashMap<>());
    }

}
