package com.bz.springkafka.spring_kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import reactor.core.publisher.Flux;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOptions;
import reactor.kafka.sender.SenderOptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.bz.springkafka.spring_kafka.AppGlobalVars.TOPIC;

@Configuration
@Slf4j
public class KafkaConfig {

    @Bean
    public ReceiverOptions<String, String> kafkaReceiverOptions(
            @Value("${spring.kafka.bootstrap-servers}") String server
    ) {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, server);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "my-consumer-group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        return ReceiverOptions.create(props);
    }

    @Bean
    public Flux<String> kafkaMessages(ReceiverOptions<String, String> receiverOptions) {
        return KafkaReceiver.create(receiverOptions.subscription(List.of(TOPIC)))
                .receive()
                .map(record -> {
                    String message = record.value();
                    log.info("Received message: {}", message);
                    record.receiverOffset().acknowledge();
                    return message;
                });
    }

    @Bean
    public ReactiveKafkaProducerTemplate<String, String> reactiveKafkaProducerTemplate(
            @Value("${spring.kafka.bootstrap-servers}") String server
    ) {
        Map<String, Object> configs = new HashMap<>();
        configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, server);
        configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        SenderOptions<String, String> senderOptions = SenderOptions.create(configs);
        return new ReactiveKafkaProducerTemplate<>(senderOptions);
    }

}
