package com.alper.shotify.backend.controller;

import com.alper.shotify.backend.model.request.CreateRecommendationRequestDTO;
import com.alper.shotify.backend.model.request.UpdateRecommendationRequestDTO;
import com.alper.shotify.backend.model.response.RecommendationResponseDTO;
import com.alper.shotify.backend.service.RecommendationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/recommendations")
@Tag(name = "Recommendation API", description = "Recommendation işlemleri için API'ler")
public class RecommendationController {
    private final RecommendationService recommendationService;


    @GetMapping
    @Operation(summary = "Önerileri listele")
    public ResponseEntity<List<RecommendationResponseDTO>> getAllRecommendations() {
        return ResponseEntity.ok(recommendationService.getAllRecommendations());
    }

    @GetMapping("/{id}")
    @Operation(summary = "ID'ye göre öneri getir")
    public ResponseEntity<RecommendationResponseDTO> getRecommendationById(@PathVariable int id){
        return ResponseEntity.ok(recommendationService.getRecommendationById(id));
    }

    @PostMapping
    @Operation(summary = "Öneri oluştur")
    public ResponseEntity<RecommendationResponseDTO> createRecommendation(@RequestBody CreateRecommendationRequestDTO requestDTO){
        return ResponseEntity.ok(recommendationService.createRecommendation(requestDTO));
    }

    @PutMapping
    @Operation(summary = "Öneri bilgilerini güncelle")
    public ResponseEntity<RecommendationResponseDTO> updateRecommendation(@RequestBody UpdateRecommendationRequestDTO requestDTO){
        return ResponseEntity.ok(recommendationService.updateRecommendation(requestDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "ID'ye göre öneri sil")
    public ResponseEntity<String> deleteRecommendation(@PathVariable int id){
        recommendationService.deleteRecommendationById(id);
        return ResponseEntity.ok("Öneri silindi: " + id);
    }

}

