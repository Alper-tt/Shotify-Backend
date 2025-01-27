package com.alper.shotify.backend.controller;

import com.alper.shotify.backend.model.request.CreateRecommendationRequestDTO;
import com.alper.shotify.backend.model.request.UpdateRecommendationRequestDTO;
import com.alper.shotify.backend.model.response.RecommendationResponseDTO;
import com.alper.shotify.backend.service.RecommendationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/recommendation")
@Tag(name = "Recommendation API", description = "Recommendation işlemleri için API'ler")
public class RecommendationController {
    private final RecommendationService recommendationService;

    @GetMapping
    public ResponseEntity<List<RecommendationResponseDTO>> getAllRecommendations() {
        return ResponseEntity.ok(recommendationService.getAllRecommendations());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecommendationResponseDTO> getRecommendationById(@PathVariable int id){
        return ResponseEntity.ok(recommendationService.getRecommendationByid(id));
    }

    @PostMapping
    public ResponseEntity<RecommendationResponseDTO> createRecommendation(@RequestBody CreateRecommendationRequestDTO requestDTO){
        return ResponseEntity.ok(recommendationService.createRecommendation(requestDTO));
    }

    @PutMapping
    public ResponseEntity<RecommendationResponseDTO> updateRecommendation(@RequestBody UpdateRecommendationRequestDTO requestDTO){
        return ResponseEntity.ok(recommendationService.updateRecommendation(requestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRecommendation(@PathVariable int id){
        recommendationService.deleteRecommendationById(id);
        return ResponseEntity.ok("Öneri silindi: " + id);
    }
}
