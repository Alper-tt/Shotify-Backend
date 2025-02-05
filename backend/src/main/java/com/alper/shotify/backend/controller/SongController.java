package com.alper.shotify.backend.controller;

import com.alper.shotify.backend.model.request.CreateSongRequestDTO;
import com.alper.shotify.backend.model.request.UpdateSongRequestDTO;
import com.alper.shotify.backend.model.response.SongResponseDTO;
import com.alper.shotify.backend.service.SongService;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(summary = "ID'ye göre şarkı getir")
    public ResponseEntity<SongResponseDTO> getSongById(@PathVariable int id) {
        return ResponseEntity.ok(songService.getSongById(id));
    }

    @GetMapping
    @Operation(summary = "Şarkıları listele")
    public ResponseEntity<List<SongResponseDTO>> getAllSongs() {
        return ResponseEntity.ok(songService.getAllSongs());
    }

    @PostMapping
    @Operation(summary = "Şarkı oluştur")
    public ResponseEntity<SongResponseDTO> createSong(@RequestBody CreateSongRequestDTO requestDTO){
        return ResponseEntity.ok(songService.createSong(requestDTO));
    }

    @PutMapping()
    @Operation(summary = "Şarkı bilgilerini güncelle")
    public ResponseEntity<SongResponseDTO> updateSong(@RequestBody UpdateSongRequestDTO requestDTO){
        return ResponseEntity.ok(songService.updateSong(requestDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "ID'ye göre şarkı sil")
    public ResponseEntity<String> deleteSong(@PathVariable int id){
        songService.deleteSongById(id);
        return ResponseEntity.ok("Şarkı silindi: " + id);
    }

}
