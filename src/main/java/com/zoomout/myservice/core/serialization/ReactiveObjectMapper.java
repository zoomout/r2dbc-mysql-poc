package com.zoomout.myservice.core.serialization;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Component
@RequiredArgsConstructor
public class ReactiveObjectMapper {

    private final ObjectMapper objectMapper;

    public <T> Mono<T> readValue(String body, final Class<T> elementClass) {
        return Mono.fromCallable(() -> objectMapper.readValue(body, elementClass))
                .subscribeOn(Schedulers.boundedElastic());
    }

    public <T> Mono<String> writeValue(T body) {
        return Mono.fromCallable(() -> objectMapper.writeValueAsString(body))
                .subscribeOn(Schedulers.boundedElastic());
    }

}
