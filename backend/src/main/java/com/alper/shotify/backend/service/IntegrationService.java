package com.alper.shotify.backend.service;

import com.alper.shotify.backend.entity.PhotoEntity;
import com.alper.shotify.backend.model.request.AnalyzePhotoRequest;
import com.alper.shotify.backend.model.request.CreateRecommendationRequestDTO;
import com.alper.shotify.backend.model.response.*;
import com.alper.shotify.backend.repository.IPhotoRepository;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.service.RequestBodyService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class IntegrationService {
    private final WebClient.Builder webClientBuilder;
    private final IPhotoRepository photoRepository;
    private final RecommendationService recommendationService;
    private final RequestBodyService requestBodyBuilder;

    @Value("${object.detection.url}")
    private String objectDetectionUrl;

    @Value("${song.recommendation.url}")
    private String songRecommendationUrl;

    public RecommendationResponseDTO analyzePhoto (AnalyzePhotoRequest requestDTO){
        PhotoEntity photo = photoRepository.findById(requestDTO.getPhotoId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Photo not found"));

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("url", photo.getUrl());

        WebClient webClient = WebClient.builder().build();
        ObjectDetectionResponse detectionResponse = webClient.post()
                .uri(objectDetectionUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
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

        CreateRecommendationRequestDTO createRecommendationRequestDTO = new CreateRecommendationRequestDTO();
        createRecommendationRequestDTO.setPhotoId(photo.getPhotoId());
        createRecommendationRequestDTO.setRecommendedSongIds(recommendedSongIds.getRecommendedSongIds());
        return recommendationService.createRecommendation(createRecommendationRequestDTO);
    }
}
