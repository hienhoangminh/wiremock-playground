package com.rocktester.automation.mdaqchallenge.model;

import com.google.gson.annotations.SerializedName;
import com.rocktester.automation.mdaqchallenge.constants.PaymentStatusConstants;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentResponseDTO {

    @SerializedName("status")
    private PaymentStatusConstants status;

}
