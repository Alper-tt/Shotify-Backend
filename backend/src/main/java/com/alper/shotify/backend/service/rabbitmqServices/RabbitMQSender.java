package com.alper.shotify.backend.service.rabbitmqServices;

import com.alper.shotify.backend.config.RabbitMQConfig;
import com.alper.shotify.backend.model.messages.PhotoMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendToObjectDetection(PhotoMessage message) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.OBJECT_DETECTION_QUEUE, message);
        System.out.println("Mesaj gönderildi: " + message.getUrl());
    }
}
