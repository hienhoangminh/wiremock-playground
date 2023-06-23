package com.rocktester.automation.mdaqchallenge.controller;

import com.rocktester.automation.mdaqchallenge.service.PaymentGatewayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Map;

@RestController
public class PaymentGatewayController {

    @Autowired
    private PaymentGatewayService paymentGatewayService;

    @PostMapping("/gateway/payment")
    public ResponseEntity<String> makePayment(@RequestHeader("content-type") MediaType contentType, @RequestHeader("authorization") String authorization, @RequestBody Map<String, Object> requestDTO){
        return this.paymentGatewayService.makePayment(contentType, authorization, requestDTO);
    }

    @ExceptionHandler({ HttpClientErrorException.class })
    public void handleException() {
        //
    }
}
