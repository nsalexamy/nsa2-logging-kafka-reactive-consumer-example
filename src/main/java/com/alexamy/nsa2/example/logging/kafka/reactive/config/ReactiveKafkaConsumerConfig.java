package com.alexamy.nsa2.example.logging.kafka.reactive.config;

import com.alexamy.nsa2.example.logging.kafka.reactive.consumer.model.LogPayload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.security.scram.ScramLoginModule;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.UUIDDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import reactor.kafka.receiver.ReceiverOptions;

import java.util.*;

@Slf4j
@RequiredArgsConstructor
@EnableKafka
@Configuration
public class ReactiveKafkaConsumerConfig {
    public static final String BEAN_NAME_KAFKA_CONSUMER_TEMPLATE = "logConsumerTemplate";
    @Value("${app.kafka.topic}")
    private String kafkaTopic;

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;

    @Value("${spring.kafka.consumer.auto-offset-reset}")
    private String autoOffsetReset;


    private final KafkaProperties kafkaProperties;

    // https://utronics.hashnode.dev/spring-webflux-reactive-kafka-cassandra-complete-reactive-spring-apps
    @Bean
    public ReceiverOptions<UUID, LogPayload> kafkaReceiverOptions() {
//        log.info("=====> Creating Kafka receiver options. bootstrapServers: {}, groupId: {}, kafkaTopic: {}",
//                bootstrapServers, groupId, kafkaTopic);
//        log.info("=====> Kafka properties: {}", kafkaProperties);

//        Map<String, Object> consumerProperties = kafkaProperties.buildConsumerProperties();
//        log.info("=====> Kafka consumer properties: {}", consumerProperties);

        ReceiverOptions<UUID, LogPayload> basicReceiverOptions =
                ReceiverOptions.create(kafkaProperties.buildConsumerProperties());


        return basicReceiverOptions.subscription(Collections.singleton(kafkaTopic))
                .addAssignListener(partitions -> log.info("=====> Assigned partitions: {}", partitions))
                .addRevokeListener(partitions -> log.info("=====> Revoked partitions: {}", partitions));

    }

//    @Bean
    public ReceiverOptions<UUID, LogPayload> kafkaReceiverOptions_____() {
        final String jaasConfig = String.format(
                "%s required username=\"%s\" " + "password=\"%s\";", ScramLoginModule.class.getName(), "iclinic", "Test2010!"
        );

        Map<String, Object> consumerProps = new HashMap<>();

        consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, UUIDDeserializer.class.getName());
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class.getName());
        consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffsetReset);
        consumerProps.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false);
        consumerProps.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        consumerProps.put(JsonDeserializer.VALUE_DEFAULT_TYPE, LogPayload.class.getName());

        consumerProps.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SASL_PLAINTEXT");
        consumerProps.put(SaslConfigs.SASL_MECHANISM, "SCRAM-SHA-256");
        consumerProps.put(SaslConfigs.SASL_JAAS_CONFIG, jaasConfig);


        return ReceiverOptions.<UUID, LogPayload>create(consumerProps)
                .withKeyDeserializer(new UUIDDeserializer())
                .withValueDeserializer(new JsonDeserializer<>(LogPayload.class))
                .subscription(Set.of(kafkaTopic));
    }

    @Bean(BEAN_NAME_KAFKA_CONSUMER_TEMPLATE)
    public ReactiveKafkaConsumerTemplate<UUID, LogPayload> logConsumerTemplate() {
        return new ReactiveKafkaConsumerTemplate<>(kafkaReceiverOptions());
    }
}
