package com.alper.shotify.backend.repository;

import com.alper.shotify.backend.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IUserRepository extends JpaRepository<UserEntity, Integer> {
    Boolean existsByFirebaseUid(String firebaseUid);
    Boolean existsByEmail(String email);
    UserEntity findByFirebaseUid(String firebaseUid);
}
