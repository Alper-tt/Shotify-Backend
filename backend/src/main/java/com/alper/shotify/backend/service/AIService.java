package com.alper.shotify.backend.service;

import com.alper.shotify.backend.model.request.AIRequest;
import com.alper.shotify.backend.model.response.AIResponse;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AIService {

    @Value("${ai.api.url}")
    private String apiUrl;

    @Value("${ai.api.key}")
    private String apiKey;

    private final WebClient.Builder webClientBuilder;

    private WebClient webClient;

    @PostConstruct
    public void init() {
        this.webClient = webClientBuilder.baseUrl(apiUrl).build();
    }

    public AIResponse getSongRecommendations(List<String> keywords) {
        if (keywords == null || keywords.size() < 3) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "En az 3 kelime sağlanmalıdır");
        }
        String prompt = String.format(
                "içinde {%s},{%s},{%s} geçen şarkılar önerebilir misin? halisülasyon görme ve sadece var olan şarkıları bul",
                keywords.get(0), keywords.get(1), keywords.get(2));
        System.out.println(prompt);

        AIRequest.Message message = new AIRequest.Message("user", prompt);
        AIRequest aiRequest = new AIRequest();
        aiRequest.setMessages(Collections.singletonList(message));

        AIResponse aiResponse = webClient.post()
                .uri("/chat/completions")
                .header("Authorization", "Bearer " + apiKey)
                .bodyValue(aiRequest)
                .retrieve()
                .onStatus(HttpStatusCode::isError, response ->
                        response.bodyToMono(String.class)
                                .flatMap(errorBody -> {
                                    throw new ResponseStatusException(response.statusCode(), errorBody); }))
                .bodyToMono(AIResponse.class)
                .block();

        return aiResponse;
    }
}
