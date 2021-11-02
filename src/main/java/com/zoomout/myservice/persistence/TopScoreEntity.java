package com.zoomout.myservice.persistence;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
public class TopScoreEntity {

    String name;
    BigDecimal score;
    Long box;

}
