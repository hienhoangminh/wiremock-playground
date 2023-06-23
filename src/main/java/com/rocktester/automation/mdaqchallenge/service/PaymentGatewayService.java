package com.rocktester.automation.mdaqchallenge.service;

import com.rocktester.automation.mdaqchallenge.constants.PaymentStatusConstants;
import com.rocktester.automation.mdaqchallenge.model.PaymentResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class PaymentGatewayService {

    @Autowired
    private RestTemplate restTemplate;

    public ResponseEntity<String> makePayment(MediaType contentType, String authorization, Map<String, Object> paymentRequestDTO){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(contentType);
        headers.setBasicAuth(authorization.split(" ")[1]);
        log.info("Headers: {}", headers);

        Map<String, Object> map = new HashMap<>();
        map.put("transaction_id", paymentRequestDTO.get("transaction_id"));
        map.put("amount", paymentRequestDTO.get("amount"));
        map.put("ccy_pair", paymentRequestDTO.get("source_currency") + (String) paymentRequestDTO.get("target_currency"));
        map.put("transaction_ccy", paymentRequestDTO.get("target_currency"));
        map.put("side", "BUY");

        map.forEach((k, v) -> log.info("<Key: {}, value: {}", k, v));
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(map, headers);

        ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:8080/transaction", request, String.class);

        log.info("Response: {}", response);
        if(response.getStatusCode().value() == HttpStatus.OK.value()){
            return new ResponseEntity<>("Payment status is " + PaymentStatusConstants.SUCCESS.name(), HttpStatus.CREATED);
        }else {
            return new ResponseEntity<>("Payment status is " + PaymentStatusConstants.FAILED.name(), HttpStatus.BAD_REQUEST);
        }
    }
}
