package com.alper.shotify.backend.repository;

import com.alper.shotify.backend.entity.PhotoEntity;
import com.alper.shotify.backend.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IPhotoRepository extends JpaRepository<PhotoEntity, Integer> {
}
