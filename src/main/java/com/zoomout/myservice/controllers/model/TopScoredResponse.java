package com.zoomout.myservice.controllers.model;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
public class TopScoredResponse {
    String name;
    String officeBox;
    BigDecimal score;
}
