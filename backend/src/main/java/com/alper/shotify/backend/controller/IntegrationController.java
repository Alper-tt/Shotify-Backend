package com.alper.shotify.backend.controller;

import com.alper.shotify.backend.model.request.AnalyzePhotoRequest;
import com.alper.shotify.backend.model.response.RecommendedSongsListResponseDTO;
import com.alper.shotify.backend.service.IntegrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/integration")
@RequiredArgsConstructor
public class IntegrationController {
    private final IntegrationService integrationService;

    @PostMapping("/analyze-photo")
    public ResponseEntity<RecommendedSongsListResponseDTO> analyzePhoto(@RequestBody AnalyzePhotoRequest analyzePhotoRequest) {
        RecommendedSongsListResponseDTO response = integrationService.analyzePhoto(analyzePhotoRequest.getPhotoPath());
        return ResponseEntity.ok(response);
    }
}
