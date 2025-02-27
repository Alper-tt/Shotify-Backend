package com.alper.shotify.backend.repository;

import com.alper.shotify.backend.entity.PhotoAnalysisProcess;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IPhotoAnalysisProcessRepository extends JpaRepository<PhotoAnalysisProcess, Long> {
    Optional<PhotoAnalysisProcess> findByPhotoId(Long photoId);
}
