package com.zoomout.myservice;

import com.zoomout.myservice.common.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.given;

class HealthControllerTest extends BaseTest {

    @Test
    void testThat_WhenGetAlive_returns200() {
        given()
                .get(url(ALIVE_PATH))
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void testThat_WhenGetReady_returns200() {
        given()
                .get(url(READY_PATH))
                .then()
                .statusCode(HttpStatus.OK.value());
    }

}
