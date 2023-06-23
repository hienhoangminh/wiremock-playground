package com.rocktester.automation.mdaqchallenge;

import com.github.javafaker.Faker;
import com.github.tomakehurst.wiremock.servlet.WireMockHandlerDispatchingServlet;
import com.github.tomakehurst.wiremock.servlet.WireMockWebContextListener;
import com.rocktester.automation.mdaqchallenge.constants.*;
import org.apache.hc.client5.http.utils.Base64;
import org.apache.hc.core5.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
import org.springframework.web.servlet.mvc.ServletWrappingController;

import java.nio.charset.StandardCharsets;
import java.util.Properties;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@SpringBootApplication
@EnableAutoConfiguration(exclude = { SecurityAutoConfiguration.class })
public class MdaqChallengeApplication {

    @Value("${username}")
    private String userName;

    @Value("${password}")
    private String password;

	@Autowired
	private Faker faker;

    public static void main(String[] args) {
        SpringApplication.run(MdaqChallengeApplication.class, args);
    }

    private String getBasicAuth(String username, String password) {
        String auth = username + ":" + password;
        byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(StandardCharsets.US_ASCII));
        return "Basic " + new String(encodedAuth);
    }

	@Bean
	WireMockWebContextListener WireMockWebContextListener() {
		return new WireMockWebContextListener();
	}

	@Bean
	ServletWrappingController wireMockController() {
		ServletWrappingController controller = new ServletWrappingController();
		controller.setServletClass(WireMockHandlerDispatchingServlet.class);
		controller.setBeanName("wireMockController");
		Properties properties = new Properties();
		properties.setProperty("RequestHandlerClass", "com.github.tomakehurst.wiremock.http.StubRequestHandler");
		controller.setInitParameters(properties);
		return controller;
	}

	@Bean
	SimpleUrlHandlerMapping wireMockControllerMapping() {
		SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping();
		Properties urlProperties = new Properties();
		urlProperties.put("/*", "wireMockController");
		mapping.setMappings(urlProperties);
		mapping.setOrder(Integer.MAX_VALUE - 1);
		return mapping;
	}

	@Bean
	ServletWrappingController wireMockAdminController() {
		ServletWrappingController controller = new ServletWrappingController();
		controller.setServletClass(WireMockHandlerDispatchingServlet.class);
		controller.setBeanName("wireMockAdminController");
		Properties properties = new Properties();
		properties.setProperty("RequestHandlerClass", "com.github.tomakehurst.wiremock.http.AdminRequestHandler");
		controller.setInitParameters(properties);
		return controller;
	}

	@Bean
	SimpleUrlHandlerMapping wireMockAdminControllerMapping() {
		SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping();
		Properties urlProperties = new Properties();
		urlProperties.put("/__admin/*", "wireMockAdminController");
		mapping.setMappings(urlProperties);
		mapping.setOrder(Integer.MAX_VALUE - 2);
		return mapping;
	}

    @EventListener(ApplicationReadyEvent.class)
    void configureStubs() {
        stubForSubmitTransactionWithoutAuthentication();
        stubForSubmitTransactionWithoutTransactionTime();
        stubForSubmitTransactionWithTransactionTime();
        stubForSubmitTransactionWithoutBody();
        stubForSubmitTransactionWithEmptyBody();
        stubForSubmitTransactionWithMissingTransactionId();
        stubForSubmitTransactionWithMissingCcyPair();
        stubForSubmitTransactionWithMissingAmount();
        stubForSubmitTransactionWithMissingTransactionCcy();
        stubForSubmitTransactionWithMissingSide();
        stubForSubmitTransactionWithInvalidTransactionId();
        stubForSubmitTransactionWithInvalidCcyPair();
        stubForSubmitTransactionWithInvalidAmount();
        stubForSubmitTransactionWithInvalidTransactionCcy();
        stubForSubmitTransactionWithInvalidSide();
        stubForSubmitTransactionWithInvalidTransactionTime();
        stubForSubmitTransactionWithGetMethod();
        stubForSubmitTransactionWithPutMethod();
        stubForSubmitTransactionWithPatchMethod();
        stubForSubmitTransactionWithDeleteMethod();
    }


	private void stubForSubmitTransactionWithoutTransactionTime() {
        stubFor(post(EndpointConstants.TRANSACTION.getEndpoint())
                .atPriority(1)
                .withHeader(HeaderAttributeConstants.CONTENT_TYPE.getAttribute(), equalToIgnoreCase(MediaType.APPLICATION_JSON_VALUE))
                .withHeader(HeaderAttributeConstants.AUTHORIZATION.getAttribute(), equalToIgnoreCase(getBasicAuth(userName, password)))
                .withRequestBody(matchingJsonPath(JsonPathConstants.TRANSACTION_ID_JSON_PATH.getJsonPath(), matching(RegexConstants.TRANSACTION_ID_REGEX.getRegEx())))
                .withRequestBody(matchingJsonPath(JsonPathConstants.CCY_PAIR_JSON_PATH.getJsonPath(), matching(RegexConstants.CCY_PAIR_REGEX.getRegEx())))
                .withRequestBody(matchingJsonPath(JsonPathConstants.POSITIVE_AMOUNT_JSON_PATH.getJsonPath()))
                .withRequestBody(matchingJsonPath(JsonPathConstants.TRANSACTION_CCY_JSON_PATH.getJsonPath(), matching(RegexConstants.TRANSACTION_CCY_REGEX.getRegEx())))
                .withRequestBody(matchingJsonPath(JsonPathConstants.SIDE_JSON_PATH.getJsonPath(), matching(RegexConstants.SIDE_REGEX.getRegEx())))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.SC_OK)
                        .withBody(JsonResponseConstants.SUCCESS_RESPONSE.getResponse())));
    }

    private void stubForSubmitTransactionWithTransactionTime() {
        stubFor(post(EndpointConstants.TRANSACTION.getEndpoint())
                .atPriority(1)
                .withHeader(HeaderAttributeConstants.CONTENT_TYPE.getAttribute(), equalToIgnoreCase(MediaType.APPLICATION_JSON_VALUE))
                .withHeader(HeaderAttributeConstants.AUTHORIZATION.getAttribute(), equalToIgnoreCase(getBasicAuth(userName, password)))
				.withRequestBody(matchingJsonPath(JsonPathConstants.TRANSACTION_ID_JSON_PATH.getJsonPath(), matching(RegexConstants.TRANSACTION_ID_REGEX.getRegEx())))
				.withRequestBody(matchingJsonPath(JsonPathConstants.CCY_PAIR_JSON_PATH.getJsonPath(), matching(RegexConstants.CCY_PAIR_REGEX.getRegEx())))
				.withRequestBody(matchingJsonPath(JsonPathConstants.POSITIVE_AMOUNT_JSON_PATH.getJsonPath()))
				.withRequestBody(matchingJsonPath(JsonPathConstants.TRANSACTION_CCY_JSON_PATH.getJsonPath(), matching(RegexConstants.TRANSACTION_CCY_REGEX.getRegEx())))
				.withRequestBody(matchingJsonPath(JsonPathConstants.SIDE_JSON_PATH.getJsonPath(), matching(RegexConstants.SIDE_REGEX.getRegEx())))
				.withRequestBody(matchingJsonPath(JsonPathConstants.TRANSACTION_TIME_JSON_PATH.getJsonPath(), matching(RegexConstants.TRANSACTION_TIME_REGEX.getRegEx())))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.SC_OK)
                        .withBody(JsonResponseConstants.SUCCESS_RESPONSE.getResponse())));
    }

    private void stubForSubmitTransactionWithoutAuthentication() {
        stubFor(post(EndpointConstants.TRANSACTION.getEndpoint())
                .atPriority(1)
                .withHeader(HeaderAttributeConstants.CONTENT_TYPE.getAttribute(), equalToIgnoreCase(MediaType.APPLICATION_JSON_VALUE))
				.withRequestBody(matchingJsonPath(JsonPathConstants.TRANSACTION_ID_JSON_PATH.getJsonPath(), matching(RegexConstants.TRANSACTION_ID_REGEX.getRegEx())))
				.withRequestBody(matchingJsonPath(JsonPathConstants.CCY_PAIR_JSON_PATH.getJsonPath(), matching(RegexConstants.CCY_PAIR_REGEX.getRegEx())))
				.withRequestBody(matchingJsonPath(JsonPathConstants.POSITIVE_AMOUNT_JSON_PATH.getJsonPath()))
				.withRequestBody(matchingJsonPath(JsonPathConstants.TRANSACTION_CCY_JSON_PATH.getJsonPath(), matching(RegexConstants.TRANSACTION_CCY_REGEX.getRegEx())))
				.withRequestBody(matchingJsonPath(JsonPathConstants.SIDE_JSON_PATH.getJsonPath(), matching(RegexConstants.SIDE_REGEX.getRegEx())))
				.willReturn(aResponse()
                        .withStatus(HttpStatus.SC_UNAUTHORIZED)
                        .withBody(JsonResponseConstants.UNAUTHENTICATED_ERROR_RESPONSE.getResponse())));
    }

    private void stubForSubmitTransactionWithoutBody() {
        stubFor(post(EndpointConstants.TRANSACTION.getEndpoint())
                .atPriority(2)
				.withHeader(HeaderAttributeConstants.CONTENT_TYPE.getAttribute(), equalToIgnoreCase(MediaType.APPLICATION_JSON_VALUE))
				.withHeader(HeaderAttributeConstants.AUTHORIZATION.getAttribute(), equalToIgnoreCase(getBasicAuth(userName, password)))
				.willReturn(aResponse()
                        .withStatus(HttpStatus.SC_BAD_REQUEST)
                        .withBody(JsonResponseConstants.BAD_REQUEST_MISSING_FIELD_RESPONSE.getResponse())));
    }

    private void stubForSubmitTransactionWithEmptyBody() {
        stubFor(post(EndpointConstants.TRANSACTION.getEndpoint())
                .atPriority(3)
				.withHeader(HeaderAttributeConstants.CONTENT_TYPE.getAttribute(), equalToIgnoreCase(MediaType.APPLICATION_JSON_VALUE))
				.withHeader(HeaderAttributeConstants.AUTHORIZATION.getAttribute(), equalToIgnoreCase(getBasicAuth(userName, password)))
				.withRequestBody(equalToJson("{}"))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.SC_BAD_REQUEST)
                        .withBody(JsonResponseConstants.BAD_REQUEST_MISSING_FIELD_RESPONSE.getResponse())));
    }


    private void stubForSubmitTransactionWithMissingTransactionId() {
        stubFor(post(EndpointConstants.TRANSACTION.getEndpoint())
                .atPriority(4)
				.withHeader(HeaderAttributeConstants.CONTENT_TYPE.getAttribute(), equalToIgnoreCase(MediaType.APPLICATION_JSON_VALUE))
				.withHeader(HeaderAttributeConstants.AUTHORIZATION.getAttribute(), equalToIgnoreCase(getBasicAuth(userName, password)))
				.withRequestBody(matchingJsonPath(JsonPathConstants.TRANSACTION_ID_JSON_PATH.getJsonPath(), absent()))
				.withRequestBody(matchingJsonPath(JsonPathConstants.CCY_PAIR_JSON_PATH.getJsonPath(), matching(RegexConstants.CCY_PAIR_REGEX.getRegEx())))
				.withRequestBody(matchingJsonPath(JsonPathConstants.POSITIVE_AMOUNT_JSON_PATH.getJsonPath()))
				.withRequestBody(matchingJsonPath(JsonPathConstants.TRANSACTION_CCY_JSON_PATH.getJsonPath(), matching(RegexConstants.TRANSACTION_CCY_REGEX.getRegEx())))
				.withRequestBody(matchingJsonPath(JsonPathConstants.SIDE_JSON_PATH.getJsonPath(), matching(RegexConstants.SIDE_REGEX.getRegEx())))
				.willReturn(aResponse()
                        .withStatus(HttpStatus.SC_BAD_REQUEST)
                        .withBody(JsonResponseConstants.BAD_REQUEST_MISSING_FIELD_RESPONSE.getResponse())));
    }

    private void stubForSubmitTransactionWithMissingCcyPair() {
        stubFor(post(EndpointConstants.TRANSACTION.getEndpoint())
                .atPriority(5)
				.withHeader(HeaderAttributeConstants.CONTENT_TYPE.getAttribute(), equalToIgnoreCase(MediaType.APPLICATION_JSON_VALUE))
				.withHeader(HeaderAttributeConstants.AUTHORIZATION.getAttribute(), equalToIgnoreCase(getBasicAuth(userName, password)))
				.withRequestBody(matchingJsonPath(JsonPathConstants.TRANSACTION_ID_JSON_PATH.getJsonPath(), matching(RegexConstants.TRANSACTION_ID_REGEX.getRegEx())))
				.withRequestBody(matchingJsonPath(JsonPathConstants.CCY_PAIR_JSON_PATH.getJsonPath(), absent()))
				.withRequestBody(matchingJsonPath(JsonPathConstants.POSITIVE_AMOUNT_JSON_PATH.getJsonPath()))
				.withRequestBody(matchingJsonPath(JsonPathConstants.TRANSACTION_CCY_JSON_PATH.getJsonPath(), matching(RegexConstants.TRANSACTION_CCY_REGEX.getRegEx())))
				.withRequestBody(matchingJsonPath(JsonPathConstants.SIDE_JSON_PATH.getJsonPath(), matching(RegexConstants.SIDE_REGEX.getRegEx())))
				.willReturn(aResponse()
                        .withStatus(HttpStatus.SC_BAD_REQUEST)
                        .withBody(JsonResponseConstants.BAD_REQUEST_MISSING_FIELD_RESPONSE.getResponse())));
    }

    private void stubForSubmitTransactionWithMissingAmount() {
        stubFor(post(EndpointConstants.TRANSACTION.getEndpoint())
                .atPriority(5)
				.withHeader(HeaderAttributeConstants.CONTENT_TYPE.getAttribute(), equalToIgnoreCase(MediaType.APPLICATION_JSON_VALUE))
				.withHeader(HeaderAttributeConstants.AUTHORIZATION.getAttribute(), equalToIgnoreCase(getBasicAuth(userName, password)))
				.withRequestBody(matchingJsonPath(JsonPathConstants.TRANSACTION_ID_JSON_PATH.getJsonPath(), matching(RegexConstants.TRANSACTION_ID_REGEX.getRegEx())))
				.withRequestBody(matchingJsonPath(JsonPathConstants.CCY_PAIR_JSON_PATH.getJsonPath(), matching(RegexConstants.CCY_PAIR_REGEX.getRegEx())))
                .withRequestBody(matchingJsonPath(JsonPathConstants.AMOUNT_JSON_PATH.getJsonPath(), absent()))
				.withRequestBody(matchingJsonPath(JsonPathConstants.TRANSACTION_CCY_JSON_PATH.getJsonPath(), matching(RegexConstants.TRANSACTION_CCY_REGEX.getRegEx())))
				.withRequestBody(matchingJsonPath(JsonPathConstants.SIDE_JSON_PATH.getJsonPath(), matching(RegexConstants.SIDE_REGEX.getRegEx())))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.SC_BAD_REQUEST)
                        .withBody(JsonResponseConstants.BAD_REQUEST_MISSING_FIELD_RESPONSE.getResponse())));
    }


    private void stubForSubmitTransactionWithMissingTransactionCcy() {
        stubFor(post(EndpointConstants.TRANSACTION.getEndpoint())
                .atPriority(6)
				.withHeader(HeaderAttributeConstants.CONTENT_TYPE.getAttribute(), equalToIgnoreCase(MediaType.APPLICATION_JSON_VALUE))
				.withHeader(HeaderAttributeConstants.AUTHORIZATION.getAttribute(), equalToIgnoreCase(getBasicAuth(userName, password)))
				.withRequestBody(matchingJsonPath(JsonPathConstants.TRANSACTION_ID_JSON_PATH.getJsonPath(), matching(RegexConstants.TRANSACTION_ID_REGEX.getRegEx())))
				.withRequestBody(matchingJsonPath(JsonPathConstants.CCY_PAIR_JSON_PATH.getJsonPath(), matching(RegexConstants.CCY_PAIR_REGEX.getRegEx())))
				.withRequestBody(matchingJsonPath(JsonPathConstants.POSITIVE_AMOUNT_JSON_PATH.getJsonPath()))
                .withRequestBody(matchingJsonPath(JsonPathConstants.TRANSACTION_CCY_JSON_PATH.getJsonPath(), absent()))
				.withRequestBody(matchingJsonPath(JsonPathConstants.SIDE_JSON_PATH.getJsonPath(), matching(RegexConstants.SIDE_REGEX.getRegEx())))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.SC_BAD_REQUEST)
                        .withBody(JsonResponseConstants.BAD_REQUEST_MISSING_FIELD_RESPONSE.getResponse())));
    }

    private void stubForSubmitTransactionWithMissingSide() {
        stubFor(post(EndpointConstants.TRANSACTION.getEndpoint())
                .atPriority(7)
				.withHeader(HeaderAttributeConstants.CONTENT_TYPE.getAttribute(), equalToIgnoreCase(MediaType.APPLICATION_JSON_VALUE))
				.withHeader(HeaderAttributeConstants.AUTHORIZATION.getAttribute(), equalToIgnoreCase(getBasicAuth(userName, password)))
				.withRequestBody(matchingJsonPath(JsonPathConstants.TRANSACTION_ID_JSON_PATH.getJsonPath(), matching(RegexConstants.TRANSACTION_ID_REGEX.getRegEx())))
				.withRequestBody(matchingJsonPath(JsonPathConstants.CCY_PAIR_JSON_PATH.getJsonPath(), matching(RegexConstants.CCY_PAIR_REGEX.getRegEx())))
				.withRequestBody(matchingJsonPath(JsonPathConstants.POSITIVE_AMOUNT_JSON_PATH.getJsonPath()))
                .withRequestBody(matchingJsonPath(JsonPathConstants.TRANSACTION_CCY_JSON_PATH.getJsonPath(), matching(RegexConstants.TRANSACTION_CCY_REGEX.getRegEx())))
                .withRequestBody(matchingJsonPath(JsonPathConstants.SIDE_JSON_PATH.getJsonPath(), absent()))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.SC_BAD_REQUEST)
                        .withBody(JsonResponseConstants.BAD_REQUEST_MISSING_FIELD_RESPONSE.getResponse())));
    }

    private void stubForSubmitTransactionWithInvalidTransactionId() {
        stubFor(post(EndpointConstants.TRANSACTION.getEndpoint())
                .atPriority(2)
				.withHeader(HeaderAttributeConstants.CONTENT_TYPE.getAttribute(), equalToIgnoreCase(MediaType.APPLICATION_JSON_VALUE))
				.withHeader(HeaderAttributeConstants.AUTHORIZATION.getAttribute(), equalToIgnoreCase(getBasicAuth(userName, password)))
				.withRequestBody(matchingJsonPath(JsonPathConstants.TRANSACTION_ID_JSON_PATH.getJsonPath(), notMatching(RegexConstants.TRANSACTION_ID_REGEX.getRegEx())))
				.withRequestBody(matchingJsonPath(JsonPathConstants.CCY_PAIR_JSON_PATH.getJsonPath(), matching(RegexConstants.CCY_PAIR_REGEX.getRegEx())))
				.withRequestBody(matchingJsonPath(JsonPathConstants.POSITIVE_AMOUNT_JSON_PATH.getJsonPath()))
				.withRequestBody(matchingJsonPath(JsonPathConstants.TRANSACTION_CCY_JSON_PATH.getJsonPath(), matching(RegexConstants.TRANSACTION_CCY_REGEX.getRegEx())))
				.withRequestBody(matchingJsonPath(JsonPathConstants.SIDE_JSON_PATH.getJsonPath(), matching(RegexConstants.SIDE_REGEX.getRegEx())))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.SC_BAD_REQUEST)
                        .withBody(JsonResponseConstants.BAD_REQUEST_INVALID_FORMAT_RESPONSE.getResponse())));
    }

    private void stubForSubmitTransactionWithInvalidCcyPair() {
        stubFor(post(EndpointConstants.TRANSACTION.getEndpoint())
                .atPriority(2)
				.withHeader(HeaderAttributeConstants.CONTENT_TYPE.getAttribute(), equalToIgnoreCase(MediaType.APPLICATION_JSON_VALUE))
				.withHeader(HeaderAttributeConstants.AUTHORIZATION.getAttribute(), equalToIgnoreCase(getBasicAuth(userName, password)))
				.withRequestBody(matchingJsonPath(JsonPathConstants.TRANSACTION_ID_JSON_PATH.getJsonPath(), matching(RegexConstants.TRANSACTION_ID_REGEX.getRegEx())))
				.withRequestBody(matchingJsonPath(JsonPathConstants.CCY_PAIR_JSON_PATH.getJsonPath(), notMatching(RegexConstants.CCY_PAIR_REGEX.getRegEx())))
				.withRequestBody(matchingJsonPath(JsonPathConstants.POSITIVE_AMOUNT_JSON_PATH.getJsonPath()))
				.withRequestBody(matchingJsonPath(JsonPathConstants.TRANSACTION_CCY_JSON_PATH.getJsonPath(), matching(RegexConstants.TRANSACTION_CCY_REGEX.getRegEx())))
				.withRequestBody(matchingJsonPath(JsonPathConstants.SIDE_JSON_PATH.getJsonPath(), matching(RegexConstants.SIDE_REGEX.getRegEx())))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.SC_BAD_REQUEST)
                        .withBody(JsonResponseConstants.BAD_REQUEST_INVALID_FORMAT_RESPONSE.getResponse())));
    }

    private void stubForSubmitTransactionWithInvalidAmount() {
        stubFor(post(EndpointConstants.TRANSACTION.getEndpoint())
                .atPriority(2)
				.withHeader(HeaderAttributeConstants.CONTENT_TYPE.getAttribute(), equalToIgnoreCase(MediaType.APPLICATION_JSON_VALUE))
				.withHeader(HeaderAttributeConstants.AUTHORIZATION.getAttribute(), equalToIgnoreCase(getBasicAuth(userName, password)))
				.withRequestBody(matchingJsonPath(JsonPathConstants.TRANSACTION_ID_JSON_PATH.getJsonPath(), matching(RegexConstants.TRANSACTION_ID_REGEX.getRegEx())))
				.withRequestBody(matchingJsonPath(JsonPathConstants.CCY_PAIR_JSON_PATH.getJsonPath(), matching(RegexConstants.CCY_PAIR_REGEX.getRegEx())))
                .withRequestBody(matchingJsonPath(JsonPathConstants.NEGATIVE_OR_ZERO_AMOUNT_JSON_PATH.getJsonPath()))
				.withRequestBody(matchingJsonPath(JsonPathConstants.TRANSACTION_CCY_JSON_PATH.getJsonPath(), matching(RegexConstants.TRANSACTION_CCY_REGEX.getRegEx())))
				.withRequestBody(matchingJsonPath(JsonPathConstants.SIDE_JSON_PATH.getJsonPath(), matching(RegexConstants.SIDE_REGEX.getRegEx())))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.SC_BAD_REQUEST)
                        .withBody(JsonResponseConstants.BAD_REQUEST_INVALID_FORMAT_RESPONSE.getResponse())));
    }


    private void stubForSubmitTransactionWithInvalidTransactionCcy() {
        stubFor(post(EndpointConstants.TRANSACTION.getEndpoint())
                .atPriority(2)
				.withHeader(HeaderAttributeConstants.CONTENT_TYPE.getAttribute(), equalToIgnoreCase(MediaType.APPLICATION_JSON_VALUE))
				.withHeader(HeaderAttributeConstants.AUTHORIZATION.getAttribute(), equalToIgnoreCase(getBasicAuth(userName, password)))
				.withRequestBody(matchingJsonPath(JsonPathConstants.TRANSACTION_ID_JSON_PATH.getJsonPath(), matching(RegexConstants.TRANSACTION_ID_REGEX.getRegEx())))
				.withRequestBody(matchingJsonPath(JsonPathConstants.CCY_PAIR_JSON_PATH.getJsonPath(), matching(RegexConstants.CCY_PAIR_REGEX.getRegEx())))
				.withRequestBody(matchingJsonPath(JsonPathConstants.POSITIVE_AMOUNT_JSON_PATH.getJsonPath()))
                .withRequestBody(matchingJsonPath(JsonPathConstants.TRANSACTION_CCY_JSON_PATH.getJsonPath(), notMatching(RegexConstants.TRANSACTION_CCY_REGEX.getRegEx())))
				.withRequestBody(matchingJsonPath(JsonPathConstants.SIDE_JSON_PATH.getJsonPath(), matching(RegexConstants.SIDE_REGEX.getRegEx())))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.SC_BAD_REQUEST)
                        .withBody(JsonResponseConstants.BAD_REQUEST_INVALID_FORMAT_RESPONSE.getResponse())));
    }


    private void stubForSubmitTransactionWithInvalidSide() {
        stubFor(post(EndpointConstants.TRANSACTION.getEndpoint())
                .atPriority(2)
				.withHeader(HeaderAttributeConstants.CONTENT_TYPE.getAttribute(), equalToIgnoreCase(MediaType.APPLICATION_JSON_VALUE))
				.withHeader(HeaderAttributeConstants.AUTHORIZATION.getAttribute(), equalToIgnoreCase(getBasicAuth(userName, password)))
				.withRequestBody(matchingJsonPath(JsonPathConstants.TRANSACTION_ID_JSON_PATH.getJsonPath(), matching(RegexConstants.TRANSACTION_ID_REGEX.getRegEx())))
				.withRequestBody(matchingJsonPath(JsonPathConstants.CCY_PAIR_JSON_PATH.getJsonPath(), matching(RegexConstants.CCY_PAIR_REGEX.getRegEx())))
				.withRequestBody(matchingJsonPath(JsonPathConstants.POSITIVE_AMOUNT_JSON_PATH.getJsonPath()))
				.withRequestBody(matchingJsonPath(JsonPathConstants.TRANSACTION_CCY_JSON_PATH.getJsonPath(), matching(RegexConstants.TRANSACTION_CCY_REGEX.getRegEx())))
                .withRequestBody(matchingJsonPath(JsonPathConstants.SIDE_JSON_PATH.getJsonPath(), notMatching(RegexConstants.SIDE_REGEX.getRegEx())))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.SC_BAD_REQUEST)
                        .withBody(JsonResponseConstants.BAD_REQUEST_INVALID_FORMAT_RESPONSE.getResponse())));
    }

    private void stubForSubmitTransactionWithInvalidTransactionTime() {
        stubFor(post(EndpointConstants.TRANSACTION.getEndpoint())
                .atPriority(1)
				.withHeader(HeaderAttributeConstants.CONTENT_TYPE.getAttribute(), equalToIgnoreCase(MediaType.APPLICATION_JSON_VALUE))
				.withHeader(HeaderAttributeConstants.AUTHORIZATION.getAttribute(), equalToIgnoreCase(getBasicAuth(userName, password)))
				.withRequestBody(matchingJsonPath(JsonPathConstants.TRANSACTION_ID_JSON_PATH.getJsonPath(), matching(RegexConstants.TRANSACTION_ID_REGEX.getRegEx())))
				.withRequestBody(matchingJsonPath(JsonPathConstants.CCY_PAIR_JSON_PATH.getJsonPath(), matching(RegexConstants.CCY_PAIR_REGEX.getRegEx())))
				.withRequestBody(matchingJsonPath(JsonPathConstants.POSITIVE_AMOUNT_JSON_PATH.getJsonPath()))
				.withRequestBody(matchingJsonPath(JsonPathConstants.TRANSACTION_CCY_JSON_PATH.getJsonPath(), matching(RegexConstants.TRANSACTION_CCY_REGEX.getRegEx())))
				.withRequestBody(matchingJsonPath(JsonPathConstants.SIDE_JSON_PATH.getJsonPath(), matching(RegexConstants.SIDE_REGEX.getRegEx())))
                .withRequestBody(matchingJsonPath(JsonPathConstants.TRANSACTION_TIME_JSON_PATH.getJsonPath(), notMatching(RegexConstants.TRANSACTION_TIME_REGEX.getRegEx())))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.SC_BAD_REQUEST)
                        .withBody(JsonResponseConstants.BAD_REQUEST_INVALID_FORMAT_RESPONSE.getResponse())));
    }

    private void stubForSubmitTransactionWithGetMethod() {
        stubFor(get(EndpointConstants.TRANSACTION.getEndpoint())
                .atPriority(3)
				.withHeader(HeaderAttributeConstants.CONTENT_TYPE.getAttribute(), equalToIgnoreCase(MediaType.APPLICATION_JSON_VALUE))
				.withHeader(HeaderAttributeConstants.AUTHORIZATION.getAttribute(), equalToIgnoreCase(getBasicAuth(userName, password)))
				.withRequestBody(matchingJsonPath(JsonPathConstants.TRANSACTION_ID_JSON_PATH.getJsonPath(), matching(RegexConstants.TRANSACTION_ID_REGEX.getRegEx())))
				.withRequestBody(matchingJsonPath(JsonPathConstants.CCY_PAIR_JSON_PATH.getJsonPath(), matching(RegexConstants.CCY_PAIR_REGEX.getRegEx())))
				.withRequestBody(matchingJsonPath(JsonPathConstants.POSITIVE_AMOUNT_JSON_PATH.getJsonPath()))
				.withRequestBody(matchingJsonPath(JsonPathConstants.TRANSACTION_CCY_JSON_PATH.getJsonPath(), matching(RegexConstants.TRANSACTION_CCY_REGEX.getRegEx())))
				.withRequestBody(matchingJsonPath(JsonPathConstants.SIDE_JSON_PATH.getJsonPath(), matching(RegexConstants.SIDE_REGEX.getRegEx())))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.SC_METHOD_NOT_ALLOWED)
                        .withBody(JsonResponseConstants.UNSUPPORTED_METHOD_RESPONSE.getResponse())));
    }

    private void stubForSubmitTransactionWithPutMethod() {
        stubFor(put(EndpointConstants.TRANSACTION.getEndpoint())
                .atPriority(3)
				.withHeader(HeaderAttributeConstants.CONTENT_TYPE.getAttribute(), equalToIgnoreCase(MediaType.APPLICATION_JSON_VALUE))
				.withHeader(HeaderAttributeConstants.AUTHORIZATION.getAttribute(), equalToIgnoreCase(getBasicAuth(userName, password)))
				.withRequestBody(matchingJsonPath(JsonPathConstants.TRANSACTION_ID_JSON_PATH.getJsonPath(), matching(RegexConstants.TRANSACTION_ID_REGEX.getRegEx())))
				.withRequestBody(matchingJsonPath(JsonPathConstants.CCY_PAIR_JSON_PATH.getJsonPath(), matching(RegexConstants.CCY_PAIR_REGEX.getRegEx())))
				.withRequestBody(matchingJsonPath(JsonPathConstants.POSITIVE_AMOUNT_JSON_PATH.getJsonPath()))
				.withRequestBody(matchingJsonPath(JsonPathConstants.TRANSACTION_CCY_JSON_PATH.getJsonPath(), matching(RegexConstants.TRANSACTION_CCY_REGEX.getRegEx())))
				.withRequestBody(matchingJsonPath(JsonPathConstants.SIDE_JSON_PATH.getJsonPath(), matching(RegexConstants.SIDE_REGEX.getRegEx())))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.SC_METHOD_NOT_ALLOWED)
                        .withBody(JsonResponseConstants.UNSUPPORTED_METHOD_RESPONSE.getResponse())));
    }

    private void stubForSubmitTransactionWithPatchMethod() {
        stubFor(patch(urlEqualTo(EndpointConstants.TRANSACTION.getEndpoint()))
                .atPriority(3)
				.withHeader(HeaderAttributeConstants.CONTENT_TYPE.getAttribute(), equalToIgnoreCase(MediaType.APPLICATION_JSON_VALUE))
				.withHeader(HeaderAttributeConstants.AUTHORIZATION.getAttribute(), equalToIgnoreCase(getBasicAuth(userName, password)))
				.withRequestBody(matchingJsonPath(JsonPathConstants.TRANSACTION_ID_JSON_PATH.getJsonPath(), matching(RegexConstants.TRANSACTION_ID_REGEX.getRegEx())))
				.withRequestBody(matchingJsonPath(JsonPathConstants.CCY_PAIR_JSON_PATH.getJsonPath(), matching(RegexConstants.CCY_PAIR_REGEX.getRegEx())))
				.withRequestBody(matchingJsonPath(JsonPathConstants.POSITIVE_AMOUNT_JSON_PATH.getJsonPath()))
				.withRequestBody(matchingJsonPath(JsonPathConstants.TRANSACTION_CCY_JSON_PATH.getJsonPath(), matching(RegexConstants.TRANSACTION_CCY_REGEX.getRegEx())))
				.withRequestBody(matchingJsonPath(JsonPathConstants.SIDE_JSON_PATH.getJsonPath(), matching(RegexConstants.SIDE_REGEX.getRegEx())))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.SC_METHOD_NOT_ALLOWED)
                        .withBody(JsonResponseConstants.UNSUPPORTED_METHOD_RESPONSE.getResponse())));
    }

    private void stubForSubmitTransactionWithDeleteMethod() {
        stubFor(delete(EndpointConstants.TRANSACTION.getEndpoint())
                .atPriority(3)
				.withHeader(HeaderAttributeConstants.CONTENT_TYPE.getAttribute(), equalToIgnoreCase(MediaType.APPLICATION_JSON_VALUE))
				.withHeader(HeaderAttributeConstants.AUTHORIZATION.getAttribute(), equalToIgnoreCase(getBasicAuth(userName, password)))
				.withRequestBody(matchingJsonPath(JsonPathConstants.TRANSACTION_ID_JSON_PATH.getJsonPath(), matching(RegexConstants.TRANSACTION_ID_REGEX.getRegEx())))
				.withRequestBody(matchingJsonPath(JsonPathConstants.CCY_PAIR_JSON_PATH.getJsonPath(), matching(RegexConstants.CCY_PAIR_REGEX.getRegEx())))
				.withRequestBody(matchingJsonPath(JsonPathConstants.POSITIVE_AMOUNT_JSON_PATH.getJsonPath()))
				.withRequestBody(matchingJsonPath(JsonPathConstants.TRANSACTION_CCY_JSON_PATH.getJsonPath(), matching(RegexConstants.TRANSACTION_CCY_REGEX.getRegEx())))
				.withRequestBody(matchingJsonPath(JsonPathConstants.SIDE_JSON_PATH.getJsonPath(), matching(RegexConstants.SIDE_REGEX.getRegEx())))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.SC_METHOD_NOT_ALLOWED)
                        .withBody(JsonResponseConstants.UNSUPPORTED_METHOD_RESPONSE.getResponse())));
    }
}
