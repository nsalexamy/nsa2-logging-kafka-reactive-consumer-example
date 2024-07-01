package com.alexamy.nsa2.example.logging.kafka.reactive.mapper;

import com.alexamy.nsa2.example.logging.kafka.reactive.consumer.model.LogPayload;
import com.alexamy.nsa2.example.logging.kafka.reactive.r2dbc.model.ErrorLogNotification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

//@ExtendWith(SpringExtension.class)
class LogPayloadMapperTest {
    private static final String LEVEL = "ERROR";
    private static final String APPLICATION_NAME = "nsa2-logging-kafka-reactive-consumer-example";
    private static final String LOGGER_CLASS = "org.alexamy.nsa2.example.logging.kafka.reactive.consumer.LoggingKafkaReactiveConsumer";
    private static final String MESSAGE = "Error occurred while processing message";
    private static final String STACK_TRACE = "java.lang.NullPointerException: null pointer exception";
    private static final LocalDateTime LOG_TIME = LocalDateTime.of(2024, 6, 30, 10, 20, 5);

    private LogPayloadMapper logPayloadMapper;


    @BeforeEach
    void setUp() {
        logPayloadMapper = new LogPayloadMapperImpl();
    }

    @Test
    void testMapToErrorLogNotification() {

        // given
        LogPayload logPayload = new LogPayload();
        logPayload.setTimestamp("2024-06-30T10:20:05.000Z");
        logPayload.setLogTime(LOG_TIME);
        logPayload.setLevel(LEVEL);
        logPayload.setAppName(APPLICATION_NAME);
        logPayload.setLoggerClass(LOGGER_CLASS);
        logPayload.setMessage(MESSAGE);
        logPayload.setLog(MESSAGE);

        // when
        ErrorLogNotification errorLogNotification = logPayloadMapper.mapToErrorLogNotification(logPayload);

        // then
        assertNotNull(errorLogNotification);
        assertNull(errorLogNotification.getId());
//        assertEquals(logPayload.getTimestamp(), errorLogNotification.getTimestamp());
        assertEquals(logPayload.getLogTime(), errorLogNotification.getLogTime());
        assertEquals(logPayload.getLevel(), errorLogNotification.getLogLevel());
        assertEquals(logPayload.getAppName(), errorLogNotification.getApplicationName());
        assertEquals(logPayload.getLoggerClass(), errorLogNotification.getLoggerClass());
        assertEquals(logPayload.getMessage(), errorLogNotification.getMessage());
        assertEquals(logPayload.getLog(), errorLogNotification.getStackTrace());

        assertNull(errorLogNotification.getId());
    }
}