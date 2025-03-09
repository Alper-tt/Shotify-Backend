package com.alper.shotify.backend.service;

import com.alper.shotify.backend.entity.PhotoEntity;
import com.alper.shotify.backend.model.messages.PhotoMessage;
import com.alper.shotify.backend.model.request.AnalyzePhotoRequest;
import com.alper.shotify.backend.model.request.CreateRecommendationRequestDTO;
import com.alper.shotify.backend.model.response.RecommendationResponseDTO;
import com.alper.shotify.backend.repository.IPhotoRepository;
import com.alper.shotify.backend.service.rabbitmqServices.RabbitMQListener;
import com.alper.shotify.backend.service.rabbitmqServices.RabbitMQSender;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class IntegrationService {
    private final IPhotoRepository photoRepository;
    private final RabbitMQSender rabbitMQSender;
    private final RabbitMQListener rabbitMQListener;
    private final RecommendationService recommendationService;

    public RecommendationResponseDTO analyzePhoto(AnalyzePhotoRequest requestDTO) {
        PhotoEntity photo = photoRepository.findById(requestDTO.getPhotoId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Fotoğraf bulunamadı"));

        rabbitMQSender.sendToObjectDetection(new PhotoMessage(photo.getUrl()));

        CompletableFuture<List<Integer>> future = rabbitMQListener.waitForRecommendation(photo.getPhotoId());
        List<Integer> recommendedSongIds;
        try {
            recommendedSongIds = future.get(10, TimeUnit.SECONDS);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.REQUEST_TIMEOUT, "Müzik önerisi zaman aşımına uğradı");
        }

        CreateRecommendationRequestDTO createRecommendationRequestDTO = new CreateRecommendationRequestDTO();
        createRecommendationRequestDTO.setPhotoId(photo.getPhotoId());
        createRecommendationRequestDTO.setRecommendedSongIds(recommendedSongIds);
        return recommendationService.createRecommendation(createRecommendationRequestDTO);
    }
}
