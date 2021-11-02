package com.zoomout.myservice.webclient;

import com.zoomout.myservice.core.exceptions.ItemNotFoundException;
import com.zoomout.myservice.core.serialization.ReactiveObjectMapper;
import com.zoomout.myservice.webclient.model.ImdbResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
public class ImdbWebClient {

    private static final String NOT_FOUND_RESPONSE = "False";
    private final String url;
    private final WebClient webClient;
    private final ReactiveObjectMapper reactiveObjectMapper;

    public ImdbWebClient(
            String url,
            WebClient webClient,
            ReactiveObjectMapper reactiveObjectMapper
    ) {

        this.url = url;
        this.reactiveObjectMapper = reactiveObjectMapper;
        this.webClient = webClient;
    }

    public Mono<ImdbResponse> get(String apiKey, String title) {
        var requestUrl = url + "?apikey=" + apiKey + "&t=" + title;
        return webClient.get()
                .uri(requestUrl)
                .retrieve()
                .toEntity(String.class)
                .transform(this::mapResponseEntity)
                .flatMap(imdbResponse -> mapToErrorIfNotFound(imdbResponse, title));
    }

    private static Mono<ImdbResponse> mapToErrorIfNotFound(ImdbResponse imdbResponse, String title) {
        if (NOT_FOUND_RESPONSE.equalsIgnoreCase(imdbResponse.getResponse())) {
            return Mono.error(new ItemNotFoundException(HttpStatus.NOT_FOUND, "Item with title '" + title + "' not found"));
        }
        return Mono.just(imdbResponse);
    }

    private Mono<ImdbResponse> mapResponseEntity(Mono<ResponseEntity<String>> responseEntityMono) {
        return responseEntityMono.flatMap(responseEntity -> reactiveObjectMapper.readValue(responseEntity.getBody(), ImdbResponse.class));
    }

}
