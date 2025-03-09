package com.alper.shotify.backend.service.rabbitmqServices;

import com.alper.shotify.backend.config.RabbitMQConfig;
import com.alper.shotify.backend.model.messages.PhotoMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import java.util.UUID;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class RabbitMQSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendToObjectDetection(PhotoMessage message) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.OBJECT_DETECTION_QUEUE, message);
        System.out.println("Mesaj gönderildi: " + message.getUrl());
    }

    public String sendVideoCreationRequest(String photoPath, String audioUrl) {
        String correlationId = UUID.randomUUID().toString();

        Map<String, String> messageBody = new HashMap<>();
        messageBody.put("photoPath", photoPath);
        messageBody.put("audioUrl", audioUrl);
        messageBody.put("correlationId", correlationId);

        MessageProperties properties = new MessageProperties();
        properties.setCorrelationId(correlationId);
        properties.setReplyTo("video_creation_results_queue");

        try {
            rabbitTemplate.send("video_creation_queue", new Message(new ObjectMapper().writeValueAsBytes(messageBody), properties));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return correlationId;
    }

}
