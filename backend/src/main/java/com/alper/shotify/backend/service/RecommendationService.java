package com.alper.shotify.backend.service;

import com.alper.shotify.backend.entity.PhotoEntity;
import com.alper.shotify.backend.entity.RecommendationEntity;
import com.alper.shotify.backend.entity.SongEntity;
import com.alper.shotify.backend.model.request.CreateRecommendationRequestDTO;
import com.alper.shotify.backend.model.request.UpdateRecommendationRequestDTO;
import com.alper.shotify.backend.model.response.RecommendationResponseDTO;
import com.alper.shotify.backend.repository.IPhotoRepository;
import com.alper.shotify.backend.repository.IRecommendationRepository;
import com.alper.shotify.backend.repository.ISongRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
@Service
@RequiredArgsConstructor
public class RecommendationService {
    private final IRecommendationRepository recommendationRepository;
    private final IPhotoRepository photoRepository;
    private final ISongRepository songRepository;

    public RecommendationResponseDTO createRecommendation (CreateRecommendationRequestDTO requestDTO){
        PhotoEntity photo = photoRepository.findById(requestDTO.getPhotoId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Fotoğraf bulunamadı"));

        List<SongEntity> songs = songRepository.findAllById(requestDTO.getRecommendedSongIds());

        RecommendationEntity recommendationEntity = RecommendationEntity.builder()
                .photo(photo)
                .songs(songs)
                .build();
        recommendationRepository.save(recommendationEntity);

        return new RecommendationResponseDTO(
                recommendationEntity.getRecommendationId(),
                requestDTO.getPhotoId(),
                recommendationEntity.getSongs()
        );    }

    public List<RecommendationResponseDTO> getAllRecommendations(){
        List<RecommendationEntity> recommendations = recommendationRepository.findAll();
        if (recommendations.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT);
        }
        return recommendations.stream().map(
                recommendation -> new RecommendationResponseDTO(
                        recommendation.getRecommendationId(),
                        recommendation.getPhoto().getPhotoId(),
                        recommendation.getSongs()
                )).toList();
    }

    public RecommendationResponseDTO getRecommendationById(int recommendationId){
        RecommendationEntity recommendation = recommendationRepository.findById(recommendationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Öneri bulunamadı"));

        return new RecommendationResponseDTO(
                recommendation.getRecommendationId(),
                recommendation.getPhoto().getPhotoId(),
                recommendation.getSongs()
        );
    }

    public void deleteRecommendationById (int recommendationId){
        if (!recommendationRepository.existsById(recommendationId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Öneri bulunamadı");
        }
        recommendationRepository.deleteById(recommendationId);
    }

    public RecommendationResponseDTO updateRecommendation (UpdateRecommendationRequestDTO requestDTO){
        RecommendationEntity recommendation = recommendationRepository.findById(requestDTO.getRecommendationId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Öneri bulunamadı"));
        PhotoEntity photo = photoRepository.findById(requestDTO.getPhotoId())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Fotoğraf bulunamadı"));
        List<SongEntity> songs = songRepository.findAllById(requestDTO.getRecommendedSongIds());
        recommendation.setPhoto(photo);
        recommendation.setSongs(songs);
        recommendationRepository.save(recommendation);

        return new RecommendationResponseDTO(
                recommendation.getRecommendationId(),
                requestDTO.getPhotoId(),
                recommendation.getSongs()
        );
    }
}
