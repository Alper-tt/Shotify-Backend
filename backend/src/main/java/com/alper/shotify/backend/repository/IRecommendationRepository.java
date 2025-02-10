package com.alper.shotify.backend.repository;

import com.alper.shotify.backend.entity.RecommendationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IRecommendationRepository extends JpaRepository<RecommendationEntity, Integer> {
}
