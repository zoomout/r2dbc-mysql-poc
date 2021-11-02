package com.zoomout.myservice.controllers.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ScoreUpdateResponse {
    long id;
    String title;
    int score;
}
