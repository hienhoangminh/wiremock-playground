package com.rocktester.automation.mdaqchallenge.service;

import com.github.javafaker.Faker;
import com.rocktester.automation.mdaqchallenge.constants.PaymentOperationConstants;
import org.apache.hc.client5.http.utils.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
public class DataGeneratorService {

    @Autowired
    private Faker faker;

    public Map<String, Object> generatePaymentRequestDTO(){
        Map<String, Object> paymentRequestDTO = new HashMap<>();
        paymentRequestDTO.put("transaction_id", faker.internet().uuid());
        paymentRequestDTO.put("source_currency", faker.currency().code());
        paymentRequestDTO.put("target_currency", faker.currency().code());
        paymentRequestDTO.put("amount", faker.number().randomNumber(5, true));
        return paymentRequestDTO;
    }

    public String generateBase64Token(String userName, String password) {
        String auth = userName + ":" + password;
        byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(StandardCharsets.US_ASCII));
        return new String(encodedAuth);
    }

    public Map<String, Object> generateTransactionRequestDTO(PaymentOperationConstants operation){
        Map<String, Object> transactionRequestDTO = new HashMap<>();
        String sourceCurrency =  faker.currency().code();
        String targetCurrency =  faker.currency().code();
        transactionRequestDTO.put("transaction_id", faker.internet().uuid());
        transactionRequestDTO.put("ccy_pair", sourceCurrency.concat(targetCurrency));
        transactionRequestDTO.put("amount", faker.number().randomNumber(5, true));
        transactionRequestDTO.put("transaction_ccy", targetCurrency);
        transactionRequestDTO.put("side", operation.name());

        // {
//     "transaction_id": "47ee8d3e-73b0-43d9-8b84-a9cdac46abc1",
//     "ccy_pair": "LAKMVR",
//     "amount": 55965,
//     "transaction_ccy": "MVR",
//     "side": "BUY"
// }

        return transactionRequestDTO;
    }
}
