package com.zoomout.myservice.controllers;

import com.zoomout.myservice.controllers.mappers.RestMappers;
import com.zoomout.myservice.controllers.model.ScoreUpdateResponse;
import com.zoomout.myservice.controllers.model.TopScoredResponse;
import com.zoomout.myservice.services.ScoresService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@AllArgsConstructor
@Validated
public class ScoresController {

    private final ScoresService scoresService;

    @PostMapping(path = "/v1/scores")
    @Operation(
            summary = "Add score for a movie",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Score update status"),
                    @ApiResponse(responseCode = "400", description = "Invalid request or failed validation"),
                    @ApiResponse(responseCode = "500", description = "Unknown Server Error")
            }
    )
    public Mono<ResponseEntity<ScoreUpdateResponse>> addScore(
            @RequestParam String apiKey,
            @RequestParam String title,
            @RequestParam @Min(1) @Max(100) Integer score
    ) {
        return scoresService.addScore(apiKey, title, score)
                .map(RestMappers::mapToScoreUpdateResponse)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    @GetMapping(path = "/v1/scores")
    @Operation(
            summary = "Get top scored movies",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of top scored movies"),
                    @ApiResponse(responseCode = "400", description = "Invalid request or failed validation"),
                    @ApiResponse(responseCode = "500", description = "Unknown Server Error")
            }
    )
    public Mono<ResponseEntity<List<TopScoredResponse>>> getScore(@RequestParam Integer quantity) {
        return scoresService.getTopScored(quantity)
                .map(RestMappers::mapToTopScoredResponseList)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

}
