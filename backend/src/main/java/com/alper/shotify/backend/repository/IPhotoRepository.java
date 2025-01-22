package com.alper.shotify.backend.repository;

import com.alper.shotify.backend.entity.PhotoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IPhotoRepository extends JpaRepository<PhotoEntity, Long> {
}
