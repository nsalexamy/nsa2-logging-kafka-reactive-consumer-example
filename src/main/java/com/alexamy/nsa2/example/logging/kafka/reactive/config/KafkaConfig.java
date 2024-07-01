package com.alexamy.nsa2.example.logging.kafka.reactive.config;

import com.alexamy.nsa2.example.logging.kafka.reactive.consumer.model.LogPayload;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.security.scram.ScramLoginModule;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.UUIDDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.Map;
import java.util.UUID;

//@EnableKafka
//@Configuration
@Slf4j
public class KafkaConfig {
    @Value("${app.kafka.topic}")
    private String kafkaTopic;

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;

    @Value("${spring.kafka.consumer.auto-offset-reset}")
    private String autoOffsetReset;

    @Bean
    public ConsumerFactory<UUID, LogPayload> logConsumerFactory() {
        log.info("=====> Creating logConsumerFactory");
        Map<String, Object> consumerProps = Map.of(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers,
                ConsumerConfig.GROUP_ID_CONFIG, groupId,
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, UUIDDeserializer.class,
//                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName(),
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class,
                ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffsetReset,
                CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SASL_PLAINTEXT",
                SaslConfigs.SASL_MECHANISM, "SCRAM-SHA-256",
                SaslConfigs.SASL_JAAS_CONFIG, String.format(
                        "%s required username=\"%s\" " + "password=\"%s\";", ScramLoginModule.class.getName(), "iclinic", "Test2010!"
                )
        );


        return new DefaultKafkaConsumerFactory<>(consumerProps,
                new UUIDDeserializer(),
                new JsonDeserializer<>(LogPayload.class));

    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<UUID, LogPayload> logListenerContainerFactory() {
        log.info("=====> Creating logListenerContainerFactory");
        ConcurrentKafkaListenerContainerFactory<UUID, LogPayload> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(logConsumerFactory());
        return factory;
    }

}
