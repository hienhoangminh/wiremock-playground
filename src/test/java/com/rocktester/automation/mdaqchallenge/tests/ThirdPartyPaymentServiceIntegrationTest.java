package com.rocktester.automation.mdaqchallenge.tests;

import com.rocktester.automation.mdaqchallenge.MdaqChallengeApplicationTests;
import com.rocktester.automation.mdaqchallenge.service.DataGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.testng.annotations.Test;

import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;


public class ThirdPartyPaymentServiceIntegrationTest extends MdaqChallengeApplicationTests {

    @Autowired
    private DataGeneratorService dataGeneratorService;

    @Value("${username}")
    private String userName;

    @Value("${password}")
    private String password;

    @Autowired
    private RestTemplate restTemplate;

    private ResponseEntity<String> response;

    @Test
    void testPaymentSuccess() throws Exception{
        // Given
        String token = dataGeneratorService.generateBase64Token(userName, password);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBasicAuth(token);

        Map<String, Object> paymentRequestDTO = dataGeneratorService.generatePaymentRequestDTO();

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(paymentRequestDTO, headers);

        // When
        ResponseEntity<String> response = restTemplate.postForEntity("Http://localhost:8080/gateway/payment", entity, String.class);

        // Then
        assertThat(response.getStatusCode().value()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.getBody()).isEqualTo("Payment status is SUCCESS");
    }

    @Test
    void testPaymentFailed() throws Exception{
        // Given
        HttpHeaders headers = new HttpHeaders();
        Map<String, Object> paymentRequestDTO = dataGeneratorService.generatePaymentRequestDTO();
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(paymentRequestDTO, headers);

        // When
        try {
            response = restTemplate.postForEntity("Http://localhost:8080/gateway/payment", entity, String.class);
        }catch (HttpClientErrorException e){
            response = new ResponseEntity<>(e.getMostSpecificCause().getLocalizedMessage(), e.getStatusCode());
            assertThat(response.getStatusCode().value()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        }
    }
}
