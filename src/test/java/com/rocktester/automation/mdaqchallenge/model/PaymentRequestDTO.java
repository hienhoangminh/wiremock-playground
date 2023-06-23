package com.rocktester.automation.mdaqchallenge.model;

import com.google.gson.annotations.SerializedName;
import com.rocktester.automation.mdaqchallenge.constants.JsonPathConstants;
import com.rocktester.automation.mdaqchallenge.constants.RegexConstants;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentRequestDTO {

    @SerializedName("transaction_id")
    private String transactionId;

    @SerializedName("amount")
    private double amount;

    @SerializedName("source_currency")
    private String sourceCurrency;

    @SerializedName("target_currency")
    private String targetCurrency;

}
