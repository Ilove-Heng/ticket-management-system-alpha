package com.nangseakheng.common.dto;

import java.time.LocalDateTime;

public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;
    private LocalDateTime timestamp;

    // Default constructor
    public ApiResponse() {
        this.timestamp = LocalDateTime.now();
    }

    // Full constructor
    public ApiResponse(boolean success, String message, T data, LocalDateTime timestamp) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.timestamp = timestamp;
    }

    // Constructor without timestamp (auto-generated)
    public ApiResponse(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.timestamp = LocalDateTime.now();
    }

    // Factory method for successful response with data
    public static <T> ApiResponse <T> success (T data) {
        return new ApiResponse<T>(true, "Success", data, LocalDateTime.now());
    }

    // Factory method for successful response with custom message
    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<T>(true, message, data, LocalDateTime.now());
    }

    // Factory method for successful response without data
    public  static  <T> ApiResponse<T> success() {
        return new ApiResponse<T>(true, "Success", null, LocalDateTime.now());
    }

    // Factory method for error response with message
    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<T>(false, message, null, LocalDateTime.now());
    }

    // Factory method for error response with data and message
    public static <T> ApiResponse<T> error (T data, String message) {
        return new ApiResponse<T>(false, message, data, LocalDateTime.now());
    }

    // Getters
    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public T getData() {
        return data;
    }

    // Setters

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(T data) {
        this.data = data;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "ApiResponse{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", data=" + data +
                ", timestamp=" + timestamp +
                '}';
    }

}
