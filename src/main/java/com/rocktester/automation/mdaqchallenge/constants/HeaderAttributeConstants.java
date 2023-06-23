package com.rocktester.automation.mdaqchallenge.constants;

import lombok.Getter;

public enum HeaderAttributeConstants {
    CONTENT_TYPE("Content-Type"),
    AUTHORIZATION("Authorization");

    @Getter
    private final String attribute;

    HeaderAttributeConstants(String attribute) {
        this.attribute = attribute;
    }

}
