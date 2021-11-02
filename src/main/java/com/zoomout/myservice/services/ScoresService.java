package com.zoomout.myservice.services;

import com.zoomout.myservice.core.exceptions.ItemNotFoundException;
import com.zoomout.myservice.persistence.ScoreEntity;
import com.zoomout.myservice.persistence.ScoresRepository;
import com.zoomout.myservice.persistence.TopScoreEntity;
import com.zoomout.myservice.persistence.TopScoreRepository;
import com.zoomout.myservice.webclient.ImdbWebClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.zoomout.myservice.core.converter.CurrencyConverter.convertToNumber;

@Service
@RequiredArgsConstructor
public class ScoresService {

    private final ImdbWebClient webClient;
    private final ScoresRepository scoresRepository;
    private final TopScoreRepository topScoreRepository;

    public Mono<ScoreEntity> addScore(String apiKey, String title, Integer score) {
        return webClient.get(apiKey, title)
                .flatMap(imdbResponse -> convertToNumber(imdbResponse.getBoxOffice())
                        .flatMap(boxOffice -> scoresRepository.save(buildScoreEntity(title, score, boxOffice)))
                ).onErrorResume(ItemNotFoundException.class, e -> Mono.empty());
    }

    public Mono<List<TopScoreEntity>> getTopScored(int quantity) {
        return topScoreRepository.findTopScored(quantity).collectList();
    }

    private static ScoreEntity buildScoreEntity(String title, Integer score, Long boxOffice) {
        return ScoreEntity.builder()
                .name(title)
                .score(score)
                .box(boxOffice)
                .build();
    }

}
