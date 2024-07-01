package com.alexamy.nsa2.example.logging.kafka.reactive.r2dbc.repository;

import com.alexamy.nsa2.example.logging.kafka.reactive.r2dbc.model.ErrorLogNotification;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface ErrorLogNotificationRepository extends R2dbcRepository<ErrorLogNotification, Long>  {
}
