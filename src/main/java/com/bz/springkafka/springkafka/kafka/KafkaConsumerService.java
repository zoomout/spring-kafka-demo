package com.bz.springkafka.springkafka.kafka;

import com.bz.springkafka.springkafka.storage.AppStorage;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaConsumerService {

    @KafkaListener(topics = "my-topic", groupId = "my-consumer-group")
    public void consume(ConsumerRecord<String, String> record) {
        log.info("Received message key - {}, value - {}", record.key(), record.value());
        AppStorage.add(record.key(), record.value());
    }

}
