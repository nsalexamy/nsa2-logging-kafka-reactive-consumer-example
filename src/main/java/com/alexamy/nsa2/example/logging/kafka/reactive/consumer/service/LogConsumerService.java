package com.alexamy.nsa2.example.logging.kafka.reactive.consumer.service;

import com.alexamy.nsa2.example.logging.kafka.reactive.config.ReactiveKafkaConsumerConfig;
import com.alexamy.nsa2.example.logging.kafka.reactive.consumer.model.LogPayload;
import com.alexamy.nsa2.example.logging.kafka.reactive.mapper.LogPayloadMapper;
import com.alexamy.nsa2.example.logging.kafka.reactive.r2dbc.model.ErrorLogNotification;
import com.alexamy.nsa2.example.logging.kafka.reactive.r2dbc.repository.ErrorLogNotificationRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
//@Service
@Component
public class LogConsumerService {
    @Qualifier(ReactiveKafkaConsumerConfig.BEAN_NAME_KAFKA_CONSUMER_TEMPLATE)
    private final ReactiveKafkaConsumerTemplate<UUID, LogPayload> logConsumerTemplate;

    private final ErrorLogNotificationRepository notificationRepository;

    private final LogPayloadMapper logPayloadMapper;

//    @EventListener(ApplicationStartedEvent.class)
    @PostConstruct
    public void init() {
//        startConsuming().subscribe();
        startConsuming_2().subscribe();
    }

    public Flux<LogPayload> startConsuming() {
        log.info("=====> Starting to consume logs");

        return logConsumerTemplate.receiveAutoAck()
                .map(record -> record.value())
                .doOnNext(record -> log.info("Received log: {}", record))
                .doOnNext(this::processLog)
                .doOnError(e -> log.error("Error occurred while consuming log", e));

    }

    public Flux<ErrorLogNotification> startConsuming_2() {
        log.info("=====> Starting to consume logs");

        return logConsumerTemplate.receiveAutoAck()
                .map(record -> record.value())
                .doOnNext(record -> log.info("Received log: {}", record))
                .map(logPayloadMapper::mapToErrorLogNotification)
                .flatMap(notificationRepository::save)
                .doOnNext(saved -> log.info("Saved error log notification: {}", saved))
                .doOnError(e -> log.error("Error occurred while consuming log", e));

    }

    void processLog(LogPayload logPayload) {
        log.info("Processing log: {}", logPayload);

        ErrorLogNotification errorLogNotification =
                logPayloadMapper.mapToErrorLogNotification(logPayload);

        notificationRepository.save(errorLogNotification)
                .doOnSuccess(saved -> log.info("Saved error log notification: {}", saved))
                .doOnError(e -> log.error("Error occurred while saving error log notification", e))
                .subscribe();

    }




//    ErrorLogNotification map(LogPayload logPayload) {
//        return ErrorLogNotification.builder()
//                .timestamp(logPayload.getTimestamp())
//                .applicationName(logPayload.getApplicationName())
//                .loggerClass(logPayload.getLoggerClass())
//                .message(logPayload.getMessage())
//                .stackTrace(logPayload.getLog());



}

