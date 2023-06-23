package com.rocktester.automation.mdaqchallenge.tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.rocktester.automation.mdaqchallenge.constants.HeaderAttributeConstants;
import com.rocktester.automation.mdaqchallenge.constants.JsonPathConstants;
import com.rocktester.automation.mdaqchallenge.constants.JsonResponseConstants;
import com.rocktester.automation.mdaqchallenge.constants.RegexConstants;
import com.rocktester.automation.mdaqchallenge.model.CurrencyEnum;
import com.rocktester.automation.mdaqchallenge.model.PaymentRequestDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Objects;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
public class IntegrationTest {

    public WireMockServer wireMockServer;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Autowired
    private Faker faker;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    void setup(){
        wireMockServer = new WireMockServer(9001);
        wireMockServer.start();
        WireMock.configureFor("localhost", 9001);
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testPaymentSuccess() throws Exception{
        // Given
        stubFor(post(urlMatching("/payments"))
                .withHeader(HeaderAttributeConstants.CONTENT_TYPE.getAttribute(), equalToIgnoreCase(MediaType.APPLICATION_JSON_VALUE))
                .withHeader(HeaderAttributeConstants.AUTHORIZATION.getAttribute(), equalToIgnoreCase("Basic " + faker.internet().uuid().replace("-", "")))
                .withRequestBody(matchingJsonPath(JsonPathConstants.TRANSACTION_ID_JSON_PATH.getJsonPath(), matching(RegexConstants.TRANSACTION_ID_REGEX.getRegEx())))
                .withRequestBody(matchingJsonPath(JsonPathConstants.POSITIVE_AMOUNT_JSON_PATH.getJsonPath()))
                .withRequestBody(matchingJsonPath(JsonPathConstants.SOURCE_CURRENCY_JSON_PATH.getJsonPath(), matching(RegexConstants.TRANSACTION_CCY_REGEX.getRegEx())))
                .withRequestBody(matchingJsonPath(JsonPathConstants.TARGET_CURRENCY_JSON_PATH.getJsonPath(), matching(RegexConstants.TRANSACTION_CCY_REGEX.getRegEx())))
                .willReturn(aResponse().proxiedFrom("http://localhost:8080/transaction")
                        .withStatus(200)
                        .withFixedDelay(1000)
                        .withHeader(HeaderAttributeConstants.CONTENT_TYPE.getAttribute(), MediaType.APPLICATION_JSON_VALUE)
                        .withBody(JsonResponseConstants.SUCCESS_RESPONSE.getResponse())));

        //When
        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.post("/payments")
                .header(HeaderAttributeConstants.CONTENT_TYPE.getAttribute(), MediaType.APPLICATION_JSON_VALUE)
                .header(HeaderAttributeConstants.CONTENT_TYPE.getAttribute(), equalToIgnoreCase(MediaType.APPLICATION_JSON_VALUE))
                .content(objectMapper.writeValueAsString(
                        PaymentRequestDTO.builder()
                                .transactionId(faker.internet().uuid())
                                .amount(125.73)
                                .sourceCurrency(CurrencyEnum.SGD.name())
                                .targetCurrency(CurrencyEnum.USD.name()).build()))
                )
                .andDo(print())
                .andReturn();

        //Then
        String response = mvcResult.getResponse().getContentAsString();
        System.out.println(response);


    }

}
