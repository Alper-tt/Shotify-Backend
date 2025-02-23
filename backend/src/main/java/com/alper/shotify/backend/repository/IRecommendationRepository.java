package com.alper.shotify.backend.repository;

import com.alper.shotify.backend.entity.PhotoEntity;
import com.alper.shotify.backend.entity.RecommendationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IRecommendationRepository extends JpaRepository<RecommendationEntity, Integer> {
    @Query("SELECT r FROM RecommendationEntity r LEFT JOIN FETCH r.songs WHERE r.recommendationId = :id")
    Optional<RecommendationEntity> findByIdWithSongs(@Param("id") int id);

    @Query("SELECT r FROM RecommendationEntity r " +
            "JOIN FETCH r.songs")
    List<RecommendationEntity> findAllWithSongs();

    Optional<RecommendationEntity> findByPhoto(PhotoEntity photo);
}