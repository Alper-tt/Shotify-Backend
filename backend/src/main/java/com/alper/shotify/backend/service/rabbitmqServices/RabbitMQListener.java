package com.alper.shotify.backend.service.rabbitmqServices;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import com.alper.shotify.backend.config.RabbitMQConfig;
import com.alper.shotify.backend.entity.PhotoAnalysisProcess;
import com.alper.shotify.backend.repository.IPhotoAnalysisProcessRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RabbitMQListener {

    private final Map<Integer, CompletableFuture<List<Integer>>> pendingRequests = new ConcurrentHashMap<>();

    public CompletableFuture<List<Integer>> waitForRecommendation(Integer photoId) {
        CompletableFuture<List<Integer>> future = new CompletableFuture<>();
        pendingRequests.put(photoId, future);
        return future;
    }

    @RabbitListener(queues = RabbitMQConfig.RECOMMENDATION_QUEUE)
    public void receiveDetectionResults(Map<String, List<Integer>> result) {
        List<Integer> recommendedSongIds = result.get("recommendedSongIds");

        for (Map.Entry<Integer, CompletableFuture<List<Integer>>> entry : pendingRequests.entrySet()) {
            if (!entry.getValue().isDone()) {
                entry.getValue().complete(recommendedSongIds);
                pendingRequests.remove(entry.getKey());
                break;
            }
        }
    }
}
