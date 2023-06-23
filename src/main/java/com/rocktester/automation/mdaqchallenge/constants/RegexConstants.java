package com.rocktester.automation.mdaqchallenge.constants;

import lombok.Getter;

public enum RegexConstants {
    TRANSACTION_ID_REGEX("(\\S{1,42})"),
    CCY_PAIR_REGEX("([A-Z]{6})"),
    TRANSACTION_CCY_REGEX("([A-Z]{3})"),
    SIDE_REGEX("(BUY|SELL)"),
    TRANSACTION_TIME_REGEX("(\\d{4})-(\\d{2})-(\\d{2})T(\\d{2}):(\\d{2}):(\\d{2})Z");

    @Getter
    private final String regEx;

    RegexConstants(String regEx){
        this.regEx = regEx;
    }
}
