package com.alper.shotify.backend.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.core.env.Environment;

@Configuration
public class RabbitMQConfig {

    public static final String OBJECT_DETECTION_QUEUE = "object_detection_queue";
    public static final String DETECTION_RESULTS_QUEUE = "detection_results_queue";
    public static final String RECOMMENDATION_QUEUE = "recommendation_queue";
    public static final String VIDEO_CREATION_QUEUE = "video_creation_queue";
    public static final String VIDEO_CREATION_RESULTS_QUEUE = "video_creation_results_queue";


    @Bean
    public CachingConnectionFactory connectionFactory(Environment environment) {
        String rabbitmqHost = environment.getProperty("spring.rabbitmq.host");
        int rabbitmqPort = Integer.parseInt(environment.getProperty("spring.rabbitmq.port"));

        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(rabbitmqHost);
        connectionFactory.setPort(rabbitmqPort);
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        return connectionFactory;
    }

    @Bean
    public Queue objectDetectionQueue() {
        return new Queue(OBJECT_DETECTION_QUEUE, true);
    }

    @Bean
    public Queue detectionResultsQueue() {
        return new Queue(DETECTION_RESULTS_QUEUE, true);
    }

    @Bean
    public Queue recommendationQueue() {
        return new Queue(RECOMMENDATION_QUEUE, true);
    }

    @Bean
    public Queue videoCreationQueue() {
        return new Queue(VIDEO_CREATION_QUEUE, true);
    }

    @Bean
    public Queue videoCreationResultsQueue() {
        return new Queue(VIDEO_CREATION_RESULTS_QUEUE, true);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }
}
