package com.alexamy.nsa2.example.logging.kafka.reactive.mapper;

import com.alexamy.nsa2.example.logging.kafka.reactive.consumer.model.LogPayload;
import com.alexamy.nsa2.example.logging.kafka.reactive.r2dbc.model.ErrorLogNotification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LogPayloadMapper {
//    @Mapping(source = "logTime", target = "logTime", dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")

    @Mapping(target = "logLevel", source = "level")
    @Mapping(target = "applicationName", source = "appName")
    @Mapping(target = "stackTrace", source = "log")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "timestamp", expression = "java(java.time.LocalDateTime.now())")
    ErrorLogNotification mapToErrorLogNotification(LogPayload payload);

//    LogPayload mapToLogPayload(ErrorLogNotification errorLogNotification);
//    ErrorLogNotification sourceToDestination(LogPayload payload);
//    LogPayload destinationToSource(ErrorLogNotification errorLogNotification);
}
