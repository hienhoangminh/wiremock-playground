package com.rocktester.automation.mdaqchallenge.model;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
public class TransactionRequestDTO {

    @SerializedName("transaction_id")
    private String transactionId;

    @SerializedName("amount")
    private double amount;

    @SerializedName("ccy_pair")
    private String ccyPair;

    @SerializedName("transaction_ccy")
    private String transactionCcy;

    @SerializedName("side")
    private String side;

}
