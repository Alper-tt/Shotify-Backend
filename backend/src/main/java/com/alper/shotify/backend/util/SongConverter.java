package com.alper.shotify.backend.util;

import com.alper.shotify.backend.entity.SongEntity;
import com.alper.shotify.backend.model.response.RecommendedSongDTO;
import com.alper.shotify.backend.repository.ISongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

@Component
public class SongConverter {

    private final ISongRepository songRepository;

    @Autowired
    public SongConverter(ISongRepository songRepository) {
        this.songRepository = songRepository;
    }

    public List<SongEntity> convertToSongEntities(List<RecommendedSongDTO> recommendedSongs) {
        List<SongEntity> songEntities = new ArrayList<>();
        for (RecommendedSongDTO recommendedSongDTO : recommendedSongs) {
            SongEntity songEntity = songRepository.findById(recommendedSongDTO.getSongId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Song not found"));
            songEntities.add(songEntity);
        }
        return songEntities;
    }
}
