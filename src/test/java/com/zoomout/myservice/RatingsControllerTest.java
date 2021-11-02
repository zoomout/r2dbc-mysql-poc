package com.zoomout.myservice;

import com.zoomout.myservice.common.BaseTest;
import lombok.Getter;
import org.junit.jupiter.api.Test;
import org.mockserver.matchers.Times;
import org.springframework.http.HttpStatus;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ScoresControllerTest extends BaseTest {

    @Test
    void testThat_WhenGetScores_returns200() {
        var quantity = 10;
        given()
                .get(url(SCORES_PATH + "?quantity=" + quantity))
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(is("[]"));
    }

    @Test
    void testThat_WhenPostScores_returns200() {
        var apiKey = "apiKey";
        var title = "titlePost" + UUID.randomUUID();
        var boxOffice = "$1,000";
        var score = 1;
        configureImdbMock(apiKey, title, boxOffice);
        given()
                .post(url(SCORES_PATH + "?apiKey=" + apiKey + "&title=" + title + "&score=" + score))
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("id", is(notNullValue()))
                .body("title", is(title))
                .body("score", is(score));
    }

    @Test
    void testThat_WhenPostScores_withScoreOutOfRange_returns400() {
        var apiKey = "apiKey";
        var title = "titlePost" + UUID.randomUUID();
        var boxOffice = "$1,000";
        var score = 101;
        configureImdbMock(apiKey, title, boxOffice);
        given()
                .post(url(SCORES_PATH + "?apiKey=" + apiKey + "&title=" + title + "&score=" + score))
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("message", containsString("score: must be less than or equal to 10"));
    }

    @Test
    void testThat_WhenPostAndGetScores_returns200() {
        var apiKey = "apiKey";
        var title = "titlePostAndGet" + UUID.randomUUID();
        var boxOffice = "$1,000.00";
        var score = 1;
        var quantity = 1;
        configureImdbMock(apiKey, title, boxOffice);
        given()
                .post(url(SCORES_PATH + "?apiKey=" + apiKey + "&title=" + title + "&score=" + score))
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("id", is(notNullValue()))
                .body("title", is(title))
                .body("score", is(score));
        given()
                .get(url(SCORES_PATH + "?quantity=" + quantity))
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("$", hasItem(
                        allOf(
                                hasEntry("name", title),
                                hasEntry("officeBox", boxOffice)
                        )
                ))
                .body("$", hasItem(hasEntry("score", score)));
    }

    @Test
    void testThat_WhenPostAndGetScores_returnsTopTwoByScore_sortedByBoxOffice() {
        var apiKey = "apiKey";
        var quantity = 10;
        var testData = new ArrayList<TestData>();
        var numberOfExtraItems = 5;
        for (int i = 1; i <= quantity + numberOfExtraItems; i++) {
            var testDataItem = new TestData(i);
            testData.add(testDataItem);
            configureImdbMock(apiKey, testDataItem.getTitle(), testDataItem.getBoxOffice());
            given()
                    .post(url(SCORES_PATH + "?apiKey=" + apiKey + "&title=" + testDataItem.getTitle() + "&score=" + testDataItem.getScore()))
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("id", is(notNullValue()))
                    .body("title", is(testDataItem.getTitle()))
                    .body("score", is(testDataItem.getScore()));
        }
        var filteredAndSorted = testData.stream()
                .sorted(Comparator.comparing(TestData::getScore))
                .skip(numberOfExtraItems)
                .sorted(Comparator.comparing(TestData::getBoxOfficeLong).reversed())
                .collect(Collectors.toList());

        ArrayList list = given()
                .get(url(SCORES_PATH + "?quantity=" + quantity))
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("$", hasSize(quantity))
                .extract().body().as(ArrayList.class);
        for (int i = 0; i < list.size(); i++) {
            String name = (String) ((LinkedHashMap) list.get(i)).get("name");
            String officeBox = (String) ((LinkedHashMap) list.get(i)).get("officeBox");
            Integer score = (Integer) ((LinkedHashMap) list.get(i)).get("score");
            assertEquals(filteredAndSorted.get(i).getTitle(), name);
            assertEquals(filteredAndSorted.get(i).getBoxOffice(), officeBox);
            assertEquals(filteredAndSorted.get(i).getScore(), score);
        }
    }

    @Test
    void testThat_thatScoreIsCalculatedCorrectly() {
        var apiKey = "apiKey";
        var title = "titlePostAndGet" + UUID.randomUUID();
        var boxOffice = "$1,000.00";
        var score1 = 2;
        var score2 = 4;
        var expectedScore = (score1 + score2) / 2;
        var quantity = 1;
        configureImdbMock(apiKey, title, boxOffice, Times.exactly(2));
        given()
                .post(url(SCORES_PATH + "?apiKey=" + apiKey + "&title=" + title + "&score=" + score1))
                .then()
                .statusCode(HttpStatus.OK.value());
        given()
                .post(url(SCORES_PATH + "?apiKey=" + apiKey + "&title=" + title + "&score=" + score2))
                .then()
                .statusCode(HttpStatus.OK.value());
        given()
                .get(url(SCORES_PATH + "?quantity=" + quantity))
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("$", hasItem(
                        allOf(
                                hasEntry("name", title),
                                hasEntry("officeBox", boxOffice)
                        )
                ))
                .body("$", hasItem(hasEntry("score", expectedScore)));
    }

    @Test
    void testThat_WhenGetScores_WithoutQuantityQuery_Returns400() {
        given()
                .get(url(SCORES_PATH))
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Getter
    private static class TestData {
        String title;
        String boxOffice;
        Integer boxOfficeLong;
        Integer score;

        public TestData(int id) {
            this.title = "titlePostAndGetSorted" + UUID.randomUUID() + id;
            this.boxOfficeLong = id * 1000;
            this.boxOffice = NumberFormat.getCurrencyInstance(Locale.US).format(boxOfficeLong);
            this.score = id;
        }

    }

}
