package com.rocktester.automation.mdaqchallenge.constants;

import lombok.Getter;

public enum JsonPathConstants {
    TRANSACTION_ID_JSON_PATH("$.transaction_id"),
    CCY_PAIR_JSON_PATH("$.ccy_pair"),
    AMOUNT_JSON_PATH("$.amount"),
    POSITIVE_AMOUNT_JSON_PATH("$[?(@.amount > 0)]"),
    TRANSACTION_CCY_JSON_PATH("$.transaction_ccy"),
    SIDE_JSON_PATH("$.side"),
    NEGATIVE_OR_ZERO_AMOUNT_JSON_PATH("$[?(@.amount <= 0)]"),
    TRANSACTION_TIME_JSON_PATH("$.transaction_time"),
    SOURCE_CURRENCY_JSON_PATH("$.source_currency"),
    TARGET_CURRENCY_JSON_PATH("$.target_currency");

    @Getter
    private final String jsonPath;

    JsonPathConstants(String jsonPath){
        this.jsonPath = jsonPath;
    }
}
