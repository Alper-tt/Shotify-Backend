package com.alper.shotify.backend.controller;

import com.alper.shotify.backend.model.request.CreateVideoRequestDTO;
import com.alper.shotify.backend.service.VideoCreationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/video")
@RequiredArgsConstructor
public class VideoController {

    final VideoCreationService videoCreationService;

    @PostMapping("/create")
    public ResponseEntity<?> createVideo(@RequestBody CreateVideoRequestDTO request) {
        try {
            String videoUrl = videoCreationService.createVideo(request.getPhotoPath(), request.getAudioUrl());
            return ResponseEntity.ok().body(videoUrl);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Video oluşturulamadı: " + e.getMessage());
        }
    }
}