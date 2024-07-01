package com.alexamy.nsa2.example.logging.kafka.reactive.consumer.model;

//public record LogPayload(String timestamp, String logTime, String level,
//                         String appName, String loggerClass, String message, String log, String message_key) {
//}

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LogPayload {
    String timestamp;
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    LocalDateTime logTime;
    String level;
    String appName;
    String loggerClass;
    String message;
    String log;
}