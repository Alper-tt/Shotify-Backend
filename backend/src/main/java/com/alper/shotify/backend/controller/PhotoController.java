package com.alper.shotify.backend.controller;

import com.alper.shotify.backend.model.request.CreatePhotoRequestDTO;
import com.alper.shotify.backend.model.request.UpdatePhotoRequestDTO;
import com.alper.shotify.backend.model.response.PhotoResponseDTO;
import com.alper.shotify.backend.service.PhotoService;
import com.alper.shotify.backend.service.firabaseServices.FirebaseStorageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/photos")
@Tag(name = "Photo API", description = "Fotoğraf işlemleri için API'ler")
public class PhotoController {
    private final PhotoService photoService;
    private final ObjectMapper objectMapper;
    private final FirebaseStorageService firebaseStorageService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Fotoğraf yükle")
    public ResponseEntity<PhotoResponseDTO> uploadPhoto(
            @RequestPart("file") MultipartFile file,
            @RequestPart(value = "requestDTO") String requestDTOJson) throws IOException {

        CreatePhotoRequestDTO requestDTO = objectMapper.readValue(requestDTOJson, CreatePhotoRequestDTO.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(photoService.createPhoto(requestDTO, file));

    }

    @GetMapping
    @Operation(summary = "Fotoğrafları listele")
    public ResponseEntity<List<PhotoResponseDTO>> getAllPhotos(){
        return ResponseEntity.ok(photoService.getAllPhotos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "ID'ye göre fotoğraf getir")
    public ResponseEntity<PhotoResponseDTO> getPhotoById(@PathVariable Integer id){
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