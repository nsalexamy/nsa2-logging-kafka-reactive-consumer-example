package com.alexamy.nsa2.example.logging.kafka.reactive;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Slf4j
//@Import({
//        com.alexamy.nsa2.example.logging.kafka.reactive.config.R2dbcConfig.class,
//        com.alexamy.nsa2.example.logging.kafka.reactive.config.KafkaConfig.class
//})
//@ComponentScan(basePackages = "com.alexamy.nsa2.example.logging.kafka.reactive")
public class Nsa2LoggingKafkaReactiveConsumerExampleApplication {

    public static void main(String[] args) {
        log.info("=====> Starting Nsa2LoggingKafkaReactiveConsumerExampleApplication");
        SpringApplication.run(Nsa2LoggingKafkaReactiveConsumerExampleApplication.class, args);

        log.info("=====> Nsa2LoggingKafkaReactiveConsumerExampleApplication successfully started");
    }

}
