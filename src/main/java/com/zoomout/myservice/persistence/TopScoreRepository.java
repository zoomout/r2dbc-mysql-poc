package com.zoomout.myservice.persistence;

import com.zoomout.myservice.core.exceptions.DatabaseMappingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class TopScoreRepository {

    private final R2dbcEntityTemplate r2dbcEntityTemplate;

    public Flux<TopScoreEntity> findTopScored(int quantity) {
        var sql = "SELECT * FROM (SELECT name as c1, avg(score) as c2, max(box) as c3 " +
                "FROM scores GROUP BY name ORDER BY c2 DESC LIMIT " + quantity +
                ")RES ORDER BY c3 DESC";
        return r2dbcEntityTemplate.getDatabaseClient()
                .sql(sql)
                .fetch().all()
                .map(TopScoreRepository::mapToTopScoresEntity);
    }

    private static TopScoreEntity mapToTopScoresEntity(Map<String, Object> result) {
        var name = (String) result.get("c1");
        var score = castNumber(result.get("c2"));
        var box = (Long) result.get("c3");
        return TopScoreEntity.builder()
                .name(name)
                .score(score)
                .box(box)
                .build();
    }

    private static BigDecimal castNumber(Object object) {
        try {
            return (BigDecimal) object;
        } catch (ClassCastException e) {
            log.debug("Couldn't cast '" + object + "' as BigDecimal");
        }
        try {
            return BigDecimal.valueOf((Integer) object);
        } catch (ClassCastException e) {
            log.debug("Couldn't cast '" + object + "' as Integer");
        }
        throw new DatabaseMappingException("Couldn't map object '" + object + "' to number");
    }

}
