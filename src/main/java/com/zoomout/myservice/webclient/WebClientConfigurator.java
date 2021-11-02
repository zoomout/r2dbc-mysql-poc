package com.zoomout.myservice.webclient;

import com.zoomout.myservice.core.serialization.ReactiveObjectMapper;
import io.netty.channel.ChannelOption;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

@Configuration
@EnableConfigurationProperties
@RequiredArgsConstructor
public class WebClientConfigurator {

    private final WebClient.Builder webClientBuilder;
    private final ReactiveObjectMapper reactiveObjectMapper;

    @Bean
    public ImdbWebClient imdbWebClient(WebClientProperties properties) {
        return new ImdbWebClient(
                properties.getUrl(),
                configureWebClient(properties, webClientBuilder),
                reactiveObjectMapper
        );
    }

    private static WebClient configureWebClient(WebClientProperties properties, WebClient.Builder webClientBuilder) {
        return webClientBuilder.clone()
                .clientConnector(new ReactorClientHttpConnector(
                        HttpClient.create()
                                .responseTimeout(properties.getResponseTime())
                                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, properties.getConnectionTimeoutMillis())
                                .option(ChannelOption.SO_KEEPALIVE, true)
                                .compress(true)
                ))
                .build();
    }

}
