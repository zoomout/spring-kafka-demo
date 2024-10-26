package com.bz.springkafka.springkafka.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaProducerService {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public String sendMessage(String topic, String message) {
        String key = UUID.randomUUID().toString();
        kafkaTemplate.send(topic, key, message);
        log.info("Sent message key - {}, value - {}", key, message);
        return key;
    }

}
