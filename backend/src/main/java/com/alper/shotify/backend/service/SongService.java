package com.alper.shotify.backend.service;

import com.alper.shotify.backend.entity.SongEntity;
import com.alper.shotify.backend.model.request.CreateSongRequestDTO;
import com.alper.shotify.backend.model.request.UpdateSongRequestDTO;
import com.alper.shotify.backend.model.response.SongResponseDTO;
import com.alper.shotify.backend.repository.ISongRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SongService {
    private final ISongRepository songRepository;

    public SongResponseDTO createSong(CreateSongRequestDTO requestDTO){
        if(songRepository.existsBySongTitle(requestDTO.getSongTitle()) && songRepository.existsBySongArtist(requestDTO.getSongArtist())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Şarkı zaten mevcut");
        }
        SongEntity song = SongEntity.builder()
                .songTitle(requestDTO.getSongTitle())
                .songArtist(requestDTO.getSongArtist())
                .songUrl(requestDTO.getSongUrl())
                .recommendations(requestDTO.getRecommendations())
        .build();
        songRepository.save(song);

        return new SongResponseDTO(song.getSongId(),
                song.getSongTitle(),
                song.getSongArtist(),
                song.getSongUrl(),
                song.getRecommendations());
    }

    public SongResponseDTO getSongById (int songId){
        SongEntity song = songRepository.findById(songId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Şarkı bulunamadı"));
        return new SongResponseDTO(song.getSongId(),
                song.getSongTitle(),
                song.getSongArtist(),
                song.getSongUrl(),
                song.getRecommendations());
    }

    public List<SongResponseDTO> getAllSongs(){
        List<SongEntity> songs = songRepository.findAll();
        if(songs.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NO_CONTENT);
        }

        return songs.stream()
                .map(song -> new SongResponseDTO(
                        song.getSongId(),
                        song.getSongTitle(),
                        song.getSongArtist(),
                        song.getSongUrl(),
                        song.getRecommendations()
                )).toList();
    }

    public void deleteSongById(int songId){
        if(!songRepository.existsById(songId)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Şarkı bulunamadı");
        }
        songRepository.deleteById(songId);
    }

    public SongResponseDTO updateSong(UpdateSongRequestDTO requestDTO){
        SongEntity song = songRepository.findById(requestDTO.getSongId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Şarkı bulunamadı"));

        song.setSongTitle(requestDTO.getSongTitle());
        song.setSongArtist(requestDTO.getSongArtist());
        song.setSongUrl(requestDTO.getSongUrl());
        song.setRecommendations(requestDTO.getRecommendations());
        songRepository.save(song);

        return new SongResponseDTO(
                song.getSongId(),
                song.getSongTitle(),
                song.getSongArtist(),
                song.getSongUrl(),
                song.getRecommendations()
        );

    }
}

