package com.alper.shotify.backend.controller;

import com.alper.shotify.backend.model.request.AnalyzePhotoRequest;
import com.alper.shotify.backend.model.response.RecommendationResponseDTO;
import com.alper.shotify.backend.service.IntegrationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/integration")
@RequiredArgsConstructor
@Tag(name = "Integration API", description = "Fotoğraf analiz işlemleri için API'ler")
public class IntegrationController {
    private final IntegrationService integrationService;

    @PostMapping("/analyze-photo")
    @Operation(summary = "Fotoğrafı analiz et ve şarkı önerilerini getir")
    public ResponseEntity<RecommendationResponseDTO> analyzePhoto(@RequestBody AnalyzePhotoRequest analyzePhotoRequest) {
        return ResponseEntity.ok(integrationService.analyzePhoto(analyzePhotoRequest));
    }
}