package com.alper.shotify.backend.service;

import com.alper.shotify.backend.config.RabbitMQConfig;
import com.alper.shotify.backend.model.request.CreateVideoRequestDTO;
import com.alper.shotify.backend.model.response.VideoUrlResponseDTO;
import com.alper.shotify.backend.service.rabbitmqServices.VideoResultListener;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class VideoCreationService {

    private final RabbitTemplate rabbitTemplate;
    private final VideoResultListener videoResultListener;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public VideoUrlResponseDTO createVideo(CreateVideoRequestDTO request) {
        try {
            String correlationId = UUID.randomUUID().toString();

            Map<String, String> messageMap = new HashMap<>();
            messageMap.put("photoPath", request.getPhotoPath());
            messageMap.put("audioUrl", request.getAudioUrl());
            byte[] messageBody = objectMapper.writeValueAsBytes(messageMap);

            MessageProperties props = new MessageProperties();
            props.setCorrelationId(correlationId);
            Message message = new Message(messageBody, props);

            rabbitTemplate.send(RabbitMQConfig.VIDEO_CREATION_QUEUE, message);

            String videoUrl = videoResultListener.getResponse(correlationId, 60000);
            VideoUrlResponseDTO videoUrlResponseDTO = new VideoUrlResponseDTO();
            videoUrlResponseDTO.setVideoUrl(videoUrl);
            return videoUrlResponseDTO;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to create video", e);
        }
    }
}
