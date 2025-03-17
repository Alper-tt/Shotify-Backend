package com.alper.shotify.backend.service;

import com.alper.shotify.backend.entity.PhotoEntity;
import com.alper.shotify.backend.entity.RecommendationEntity;
import com.alper.shotify.backend.entity.SongEntity;
import com.alper.shotify.backend.model.request.CreateRecommendationRequestDTO;
import com.alper.shotify.backend.model.request.UpdateRecommendationRequestDTO;
import com.alper.shotify.backend.model.response.RecommendationResponseDTO;
import com.alper.shotify.backend.model.response.SongResponseDTO;
import com.alper.shotify.backend.repository.IPhotoRepository;
import com.alper.shotify.backend.repository.IRecommendationRepository;
import com.alper.shotify.backend.repository.ISongRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecommendationService {
    private final IRecommendationRepository recommendationRepository;
    private final IPhotoRepository photoRepository;
    private final ISongRepository songRepository;

    public RecommendationResponseDTO mapToRecommendationDTO(RecommendationEntity recommendation) {
        List<SongResponseDTO> songDTOs = recommendation.getSongs().stream()
                .map(song -> new SongResponseDTO(song.getSongId(), song.getSongTitle(), song.getSongArtist()))
                .collect(Collectors.toList());

        return new RecommendationResponseDTO(
                recommendation.getRecommendationId(),
                recommendation.getPhoto().getPhotoId(),
                songDTOs
        );
    }

    public RecommendationResponseDTO createRecommendation (CreateRecommendationRequestDTO requestDTO){
        PhotoEntity photo = photoRepository.findById(requestDTO.getPhotoId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Fotoğraf bulunamadı"));

        List<SongEntity> songs = songRepository.findAllById(requestDTO.getRecommendedSongIds());
        RecommendationEntity recommendationEntity = RecommendationEntity.builder()
                .photo(photo)
                .songs(songs)
                .build();
        recommendationRepository.save(recommendationEntity);
        return mapToRecommendationDTO(recommendationEntity);
    }

    public List<RecommendationResponseDTO> getAllRecommendations() {
        List<RecommendationEntity> recommendations = recommendationRepository.findAllWithSongs();
        if (recommendations.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT);
        }
        return recommendations.stream()
                .map(this::mapToRecommendationDTO)
                .collect(Collectors.toList());
    }

    public RecommendationResponseDTO getRecommendationById(int recommendationId){
        RecommendationEntity recommendation = recommendationRepository.findByIdWithSongs(recommendationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Öneri bulunamadı"));

        return mapToRecommendationDTO(recommendation);
    }

    @Transactional
    public RecommendationResponseDTO getRecommendationByPhotoId(int photoId){
        PhotoEntity photo = photoRepository.findById(photoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Fotoğraf bulunamadı"));
        RecommendationEntity recommendation = recommendationRepository.findByPhoto(photo)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Öneri bulunamadı"));
        return mapToRecommendationDTO(recommendation);
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

        return mapToRecommendationDTO(recommendation);
    }
}
