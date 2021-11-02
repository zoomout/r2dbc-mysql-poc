package com.zoomout.myservice.common;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.restassured.RestAssured;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.junit.jupiter.MockServerExtension;
import org.mockserver.matchers.Times;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpStatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith({MockServerExtension.class, MockitoExtension.class})
@Slf4j
@ContextConfiguration(
        initializers = {MockInitializer.class}
)
@TestPropertySource(locations = BaseTest.UNIT_TEST_PROPERTIES)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public abstract class BaseTest {

    public static final String UNIT_TEST_PROPERTIES = "/application-unit-test.properties";
    private static final int CONNECTION_TIMEOUT_MILLIS = 5000;
    public static final String ALIVE_PATH = "/v1/alive";
    public static final String READY_PATH = "/v1/ready";
    public static final String SCORES_PATH = "/v1/scores";

    @LocalServerPort
    public int applicationPort;
    @Autowired
    ClientAndServer mockServerClient;

    @BeforeEach
    void configureTestSetup() {
        RestAssured.port = applicationPort;
        RestAssured.config = RestAssuredConfig.config().httpClient(HttpClientConfig.httpClientConfig().setParam("http.connection.timeout", CONNECTION_TIMEOUT_MILLIS));
        mockServerClient.reset();
        await().atMost(5, SECONDS).alias("Mock is reset log line never appeared").until(() -> !getMockResetLogs().isEmpty());
    }

    private List<String> getMockResetLogs() {
        return Arrays.stream(mockServerClient.retrieveLogMessagesArray(new HttpRequest())).filter(line -> line.contains("resetting all expectations and request logs"))
                .collect(Collectors.toList());
    }

    protected String url(String path) {
        return "http://localhost:" + applicationPort + "/myservice" + path;
    }

    protected void configureImdbMock(String apiKey, String title, String boxOffice) {
        configureImdbMock(apiKey, title, boxOffice, Times.once());
    }

    protected void configureImdbMock(String apiKey, String title, String boxOffice, Times times) {
        var httpRequest = request()
                .withMethod("GET")
                .withPath("/imdb")
                .withQueryStringParameter("apiKey", apiKey)
                .withQueryStringParameter("t", title);
        mockServerClient
                .clear(httpRequest)
                .when(httpRequest, times)
                .respond(response()
                        .withStatusCode(HttpStatusCode.OK_200.code())
                        .withBody(makeImdbResponse(title, boxOffice).toPrettyString())
                        .withHeader("Content-Type", APPLICATION_JSON_VALUE)
                );
    }

    public static JsonNode makeImdbResponse(String title, String boxOffice) {
        return makeEmptyNode()
                .put("Title", title)
                .put("BoxOffice", boxOffice)
                .put("Response", "True");
    }

    public static ObjectNode makeEmptyNode() {
        return JsonNodeFactory.instance.objectNode();
    }

}
