package com.alper.shotify.backend.service.rabbitmqServices;

import com.alper.shotify.backend.config.RabbitMQConfig;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class VideoResultListener {

    private final ConcurrentHashMap<String, CompletableFuture<String>> pendingRequests = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public CompletableFuture<String> waitForResponse(String correlationId) {
        CompletableFuture<String> future = new CompletableFuture<>();
        pendingRequests.put(correlationId, future);
        return future;
    }

    public String getResponse(String correlationId, long timeoutMillis) throws Exception {
        return waitForResponse(correlationId).get(timeoutMillis, java.util.concurrent.TimeUnit.MILLISECONDS);
    }

    @RabbitListener(queues = RabbitMQConfig.VIDEO_CREATION_RESULTS_QUEUE)
    public void receiveVideoResult(Message message) {
        try {
            String correlationId = new String(message.getMessageProperties().getCorrelationId());
            Map<String, Object> result = objectMapper.readValue(message.getBody(), new TypeReference<Map<String, Object>>() {});
            String videoUrl = (String) result.get("videoUrl");

            CompletableFuture<String> future = pendingRequests.remove(correlationId);
            if (future != null && !future.isDone()) {
                future.complete(videoUrl);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
