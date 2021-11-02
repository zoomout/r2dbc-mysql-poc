package com.zoomout.myservice.persistence;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface ScoresRepository extends ReactiveCrudRepository<ScoreEntity, Long> {
}
