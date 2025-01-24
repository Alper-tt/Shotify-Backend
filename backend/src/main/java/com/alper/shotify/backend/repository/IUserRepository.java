package com.alper.shotify.backend.repository;

import com.alper.shotify.backend.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IUserRepository extends JpaRepository<UserEntity, Integer> {
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
}
