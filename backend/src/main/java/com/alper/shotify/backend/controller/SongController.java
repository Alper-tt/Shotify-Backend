package com.alper.shotify.backend.controller;

import com.alper.shotify.backend.model.request.CreateSongRequestDTO;
import com.alper.shotify.backend.model.request.UpdateSongRequestDTO;
import com.alper.shotify.backend.model.response.SongResponseDTO;
import com.alper.shotify.backend.service.SongService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/songs")
@RequiredArgsConstructor
@Tag(name = "Song API", description = "Şarkı işlemleri için API'ler")
public class SongController {
    private final SongService songService;

    @GetMapping("/{id}")
    public ResponseEntity<SongResponseDTO> getSongById(@PathVariable int id) {
        return ResponseEntity.ok(songService.getSongById(id));
    }

    @GetMapping
    public ResponseEntity<List<SongResponseDTO>> getAllSongs() {
        return ResponseEntity.ok(songService.getAllSongs());
    }

    @PostMapping
    public ResponseEntity<SongResponseDTO> createSong(@RequestBody CreateSongRequestDTO requestDTO){
        return ResponseEntity.ok(songService.createSong(requestDTO));
    }

    @PutMapping()
    public ResponseEntity<SongResponseDTO> updateSong(@RequestBody UpdateSongRequestDTO requestDTO){
        return ResponseEntity.ok(songService.updateSong(requestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSong(@PathVariable int id){
        songService.deleteSongById(id);
        return ResponseEntity.ok("Şarkı silindi: " + id);
    }

}
