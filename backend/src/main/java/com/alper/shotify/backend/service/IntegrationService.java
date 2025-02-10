package com.alper.shotify.backend.service;

import com.alper.shotify.backend.model.request.AnalyzePhotoRequest;
import com.alper.shotify.backend.model.response.*;
import com.alper.shotify.backend.repository.ISongRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class IntegrationService {
    private final WebClient.Builder webClientBuilder;
    private final ISongRepository songRepository;

    @Value("${object.detection.url}")
    private String objectDetectionUrl;

    @Value("${song.recommendation.url}")
    private String songRecommendationUrl;

    public RecommendedSongIdsDTO analyzePhoto (String photoPath){
        WebClient webClient = WebClient.builder().build();
        ObjectDetectionResponse detectionResponse = webClient.post()
                .uri(objectDetectionUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new AnalyzePhotoRequest(photoPath))
                .retrieve()
                .bodyToMono(ObjectDetectionResponse.class)
                .block();
        if(detectionResponse == null || detectionResponse.getKeywords() == null || detectionResponse.getKeywords().isEmpty()){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Object detection failed");
        }

        RecommendedSongIdsDTO recommendedSongIds = webClient.post()
                .uri(songRecommendationUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(Collections.singletonMap("keywords", detectionResponse.getKeywords()))
                .retrieve()
                .bodyToMono(RecommendedSongIdsDTO.class)
                .block();

        if (recommendedSongIds == null || recommendedSongIds.getRecommendedSongIds().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Song recommendation failed");
        }

        return recommendedSongIds;
    }
}
