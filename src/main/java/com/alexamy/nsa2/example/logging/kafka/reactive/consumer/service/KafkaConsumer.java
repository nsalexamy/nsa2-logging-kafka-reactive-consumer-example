package com.alexamy.nsa2.example.logging.kafka.reactive.consumer.service;

import com.alexamy.nsa2.example.logging.kafka.reactive.consumer.model.LogPayload;
import com.alexamy.nsa2.example.logging.kafka.reactive.mapper.LogPayloadMapper;
import com.alexamy.nsa2.example.logging.kafka.reactive.r2dbc.repository.ErrorLogNotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
//@Component
@RequiredArgsConstructor
public class KafkaConsumer {

    private final LogPayloadMapper logPayloadMapper;
    private final ErrorLogNotificationRepository notificationRepository;


    @KafkaListener(topics = "${app.logging.kafka.topic}",
                     groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "logListenerContainerFactory")
    public void consume(LogPayload message) {
        log.info("==> Consumed message: {}", message);

        notificationRepository.save(logPayloadMapper.mapToErrorLogNotification(message))
                .doOnSuccess(saved -> log.info("Saved error log notification: {}", saved))
                .doOnError(e -> log.error("Error occurred while saving error log notification", e))
                .subscribe();
    }
}
