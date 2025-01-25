package com.alper.shotify.backend.controller;

import com.alper.shotify.backend.model.request.CreatePhotoRequestDTO;
import com.alper.shotify.backend.model.request.UpdatePhotoRequestDTO;
import com.alper.shotify.backend.model.response.PhotoResponseDTO;
import com.alper.shotify.backend.service.PhotoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/photos")
@Tag(name = "Photo API", description = "Fotoğraf işlemleri için API'ler")
public class PhotoController {
    private final PhotoService photoService;

    @PostMapping
    @Operation(summary = "Fotoğraf oluştur")
    public ResponseEntity<PhotoResponseDTO> createPhoto(@RequestBody CreatePhotoRequestDTO requestDTO){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(photoService.createPhoto(requestDTO));
    }

    @GetMapping
    @Operation(summary = "Fotoğrafları listele")
    public ResponseEntity<List<PhotoResponseDTO>> getAllPhotos(){
        return ResponseEntity.ok(photoService.getAllPhotos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "ID'ye göre fotoğraf getir")
    public ResponseEntity<PhotoResponseDTO> getPhoto(@PathVariable Integer id){
        return ResponseEntity.ok(photoService.getPhotoById(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "ID'ye göre fotoğraf sil")
    public ResponseEntity<String> deletePhoto(@PathVariable Integer id){
        photoService.deletePhoto(id);
        return ResponseEntity.ok("Fotoğraf silindi: " + id);
    }

    @PutMapping()
    @Operation(summary = "Fotoğraf bilgilerini güncelle")
    public ResponseEntity<PhotoResponseDTO> updatePhoto(@RequestBody UpdatePhotoRequestDTO requestDTO){
        return ResponseEntity.ok(photoService.updatePhoto(requestDTO));
    }
}
