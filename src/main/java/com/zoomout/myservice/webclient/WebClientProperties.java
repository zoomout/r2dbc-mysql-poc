package com.zoomout.myservice.webclient;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Duration;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "webclient")
public class WebClientProperties {

    @NotBlank
    private String url;
    @NotNull
    private Duration responseTime;
    @NotNull
    private Integer connectionTimeoutMillis;

}
