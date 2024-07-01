package com.alexamy.nsa2.example.logging.kafka.reactive.r2dbc.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "logging", name = "error_log_notification")
public class ErrorLogNotification {
    @Id
    private Long id;
    private LocalDateTime timestamp;
    @Column("log_time")
    private LocalDateTime logTime;
    @Column("log_level")
    private String logLevel;
    @Column("application_name")
    private String applicationName;
    @Column("logger_class")
    private String loggerClass;
    private String message;
    @Column("stack_trace")
    private String stackTrace;
}
