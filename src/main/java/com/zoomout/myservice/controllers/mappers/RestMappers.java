package com.zoomout.myservice.controllers.mappers;

import com.zoomout.myservice.controllers.model.ScoreUpdateResponse;
import com.zoomout.myservice.controllers.model.TopScoredResponse;
import com.zoomout.myservice.persistence.ScoreEntity;
import com.zoomout.myservice.persistence.TopScoreEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

import static com.zoomout.myservice.core.converter.CurrencyConverter.convertFromNumber;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public abstract class RestMappers {

    public static List<TopScoredResponse> mapToTopScoredResponseList(List<TopScoreEntity> scoreEntities) {
        return scoreEntities.stream()
                .map(topScored -> TopScoredResponse.builder()
                        .name(topScored.getName())
                        .officeBox(convertFromNumber(topScored.getBox()))
                        .score(topScored.getScore())
                        .build()
                )
                .collect(Collectors.toList());
    }

    public static ScoreUpdateResponse mapToScoreUpdateResponse(ScoreEntity scoreEntity) {
        return ScoreUpdateResponse.builder()
                .id(scoreEntity.getId())
                .title(scoreEntity.getName())
                .score(scoreEntity.getScore())
                .build();
    }

}
