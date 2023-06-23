package com.rocktester.automation.mdaqchallenge.model;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
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
