package com.alper.shotify.backend.controller;

import com.alper.shotify.backend.model.request.CreateVideoRequestDTO;
import com.alper.shotify.backend.model.response.VideoUrlResponseDTO;
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
    public ResponseEntity<VideoUrlResponseDTO> createVideo(@RequestBody CreateVideoRequestDTO request) {
        return ResponseEntity.ok(videoCreationService.createVideo(request));
    }
}
