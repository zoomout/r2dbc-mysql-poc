package com.zoomout.myservice.persistence;

import lombok.Builder;
import lombok.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Value
@Builder
@Table("scores")
public class ScoreEntity {

    @Id
    Long id;
    String name;
    Integer score;
    Long box;

}
