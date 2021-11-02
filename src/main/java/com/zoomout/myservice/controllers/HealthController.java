package com.zoomout.myservice.controllers;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class HealthController {

    @GetMapping(path = "/v1/alive")
    @Operation(
            summary = "Alive endpoint",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Service is alive"),
                    @ApiResponse(responseCode = "500", description = "Unknown Server Error")
            }
    )
    @Hidden
    public Mono<ResponseEntity<Void>> getAlive() {
        return Mono.just(ResponseEntity.status(HttpStatus.OK).build());
    }

    @GetMapping(path = "/v1/ready")
    @Operation(
            summary = "Ready endpoint",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Service is ready"),
                    @ApiResponse(responseCode = "500", description = "Unknown Server Error")
            }
    )
    @Hidden
    public Mono<ResponseEntity<Void>> getReady() {
        return Mono.just(ResponseEntity.status(HttpStatus.OK).build());
    }

}
