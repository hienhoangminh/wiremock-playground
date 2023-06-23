package com.rocktester.automation.mdaqchallenge.tests;

import com.rocktester.automation.mdaqchallenge.MdaqChallengeApplicationTests;
import com.rocktester.automation.mdaqchallenge.constants.JsonResponseConstants;
import com.rocktester.automation.mdaqchallenge.constants.PaymentOperationConstants;
import com.rocktester.automation.mdaqchallenge.service.DataGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.testng.annotations.Test;
import java.util.HashMap;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;

public class InhouseIntegrationTest extends MdaqChallengeApplicationTests {

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
    void testTransactionWithoutAuthorization() throws Exception{
        // Given
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Map<String, Object> transactionRequestDTO = dataGeneratorService.generateTransactionRequestDTO(PaymentOperationConstants.BUY);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(transactionRequestDTO, headers);

        // When
        try {
            response = restTemplate.postForEntity("Http://localhost:8080/transaction", entity, String.class);
        }catch (HttpClientErrorException e){
            // Then
            response = new ResponseEntity<>(e.getMostSpecificCause().getLocalizedMessage(), e.getStatusCode());
            assertThat(response.getStatusCode().value()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
            assertThat(response.getBody()).contains(JsonResponseConstants.UNAUTHENTICATED_ERROR_RESPONSE.getResponse());
        }
    }

    @Test
    void testTransactionWithMissingFields() throws Exception{
        // Given
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBasicAuth(dataGeneratorService.generateBase64Token(userName, password));

        Map<String, Object> transactionRequestDTO = new HashMap<>();
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(transactionRequestDTO, headers);

        // When
        try {
            response = restTemplate.postForEntity("Http://localhost:8080/transaction", entity, String.class);
        }catch (HttpClientErrorException e){
            // Then
            response = new ResponseEntity<>(e.getMostSpecificCause().getLocalizedMessage(), e.getStatusCode());
            assertThat(response.getStatusCode().value()).isEqualTo(HttpStatus.BAD_REQUEST.value());
            assertThat(response.getBody()).contains(JsonResponseConstants.BAD_REQUEST_MISSING_FIELD_RESPONSE.getResponse());
        }
    }

    @Test
    void testTransactionWithInvalidFormat() throws Exception{
        // Given
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBasicAuth(dataGeneratorService.generateBase64Token(userName, password));

        Map<String, Object> transactionRequestDTO = dataGeneratorService.generateTransactionRequestDTO(PaymentOperationConstants.BUY);
        transactionRequestDTO.put("transaction_id", "");
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(transactionRequestDTO, headers);

        // When
        try {
            response = restTemplate.postForEntity("Http://localhost:8080/transaction", entity, String.class);
        }catch (HttpClientErrorException e){
            // Then
            response = new ResponseEntity<>(e.getMostSpecificCause().getLocalizedMessage(), e.getStatusCode());
            assertThat(response.getStatusCode().value()).isEqualTo(HttpStatus.BAD_REQUEST.value());
            assertThat(response.getBody()).contains(JsonResponseConstants.BAD_REQUEST_INVALID_FORMAT_RESPONSE.getResponse());
        }
    }

    @Test
    void testTransactionWithValidData() throws Exception{
        // Given
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBasicAuth(dataGeneratorService.generateBase64Token(userName, password));

        Map<String, Object> transactionRequestDTO = dataGeneratorService.generateTransactionRequestDTO(PaymentOperationConstants.BUY);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(transactionRequestDTO, headers);

        // When
        ResponseEntity<String> response = restTemplate.postForEntity("Http://localhost:8080/transaction", entity, String.class);

        // Then
        assertThat(response.getStatusCode().value()).isEqualTo(200);
    }
}
