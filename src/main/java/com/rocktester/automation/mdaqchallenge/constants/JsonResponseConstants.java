package com.rocktester.automation.mdaqchallenge.constants;

import lombok.Getter;

public enum JsonResponseConstants {
    SUCCESS_RESPONSE("{\"status\": \"SUCCESS\"}"),
    UNAUTHENTICATED_ERROR_RESPONSE("{\"error_message\": \"authentication error\"}"),
    BAD_REQUEST_MISSING_FIELD_RESPONSE("{\"error_code\": 1, \"error_message\": \"missing required parameter\"}"),
    BAD_REQUEST_INVALID_FORMAT_RESPONSE("{\"error_code\": 2, \"error_message\": \"parameter format error\"}"),
    UNSUPPORTED_METHOD_RESPONSE("{\"error_code\": -1, \"error_message\": \"method is not supported\"}");

    @Getter
    private final String response;

    JsonResponseConstants(String response){
        this.response = response;
    }
}
