package com.rocktester.automation.mdaqchallenge.constants;

import lombok.Getter;

public enum EndpointConstants {

    TRANSACTION("/transaction"),
    PAYMENT_GATEWAY("/gateway/payment");

    @Getter
    private final String endpoint;

    EndpointConstants(String endpoint){
        this.endpoint = endpoint;
    }
}
