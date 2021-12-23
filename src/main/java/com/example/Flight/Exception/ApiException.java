package com.example.Flight.Exception;

public class ApiException {
    private final String reason;
    private final String status;

    public ApiException(String reason, String status) {
        this.reason = reason;
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public String getStatus() {
        return status;
    }
}
