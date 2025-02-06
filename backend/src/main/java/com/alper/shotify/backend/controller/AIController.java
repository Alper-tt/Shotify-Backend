package com.alper.shotify.backend.controller;

import com.alper.shotify.backend.model.response.AIResponse;
import com.alper.shotify.backend.service.AIService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
public class AIController {

    private final AIService aiService;

    @PostMapping("/recommendations")
    public ResponseEntity<AIResponse> getRecommendations(@RequestBody String[] keywords) {
        AIResponse response = aiService.getSongRecommendations(Arrays.asList(keywords));
        return ResponseEntity.ok(response);
    }
}