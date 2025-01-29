package com.alper.shotify.backend.repository;

import com.alper.shotify.backend.entity.SongEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ISongRepository extends JpaRepository<SongEntity, Integer> {
    boolean existsBySongTitle (String songTitle);
    boolean existsBySongArtist (String songArtist);
}
