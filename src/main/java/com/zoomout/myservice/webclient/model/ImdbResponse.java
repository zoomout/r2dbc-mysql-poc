package com.zoomout.myservice.webclient.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotNull;

@Value
@Builder
@JsonDeserialize(builder = ImdbResponse.ImdbResponseBuilder.class)
public class ImdbResponse {

    @NotNull
    @JsonProperty("Title")
    String title;

    @NotNull
    @JsonProperty("Year")
    String year;

    @NotNull
    @JsonProperty("BoxOffice")
    String boxOffice;

    @JsonProperty("Response")
    String response;

    @JsonProperty("Error")
    String error;

}
