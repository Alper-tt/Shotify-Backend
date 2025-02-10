package com.alper.shotify.backend.controller;

import com.alper.shotify.backend.entity.PhotoEntity;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/photos")
@Tag(name = "Photo API", description = "Fotoğraf işlemleri için API'ler")
public class PhotoController {
    private final PhotoService photoService;

    @PostMapping
    @Operation(summary = "Fotoğraf oluştur")
    public ResponseEntity<PhotoResponseDTO> uploadPhoto(@RequestParam("file") MultipartFile file, @RequestBody CreatePhotoRequestDTO requestDTO) throws IOException {
        String uploadDir = "/Users/alper/Desktop/Shotify/sample_images/";
        File uploadPath = new File(uploadDir);

        if (!uploadPath.exists()) {
            uploadPath.mkdirs();
        }

        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String filename = UUID.randomUUID() + extension;

        File destinationFile = new File(uploadDir + filename);
        file.transferTo(destinationFile);

        PhotoEntity photo = new PhotoEntity();
        photo.setPhotoPath(destinationFile.getAbsolutePath());
        requestDTO.setPhotoPath(destinationFile.getAbsolutePath());

        return ResponseEntity.status(HttpStatus.CREATED).body(photoService.createPhoto(requestDTO));
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
