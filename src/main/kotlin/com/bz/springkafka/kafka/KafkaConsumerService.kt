package com.bz.springkafka.kafka

import com.bz.springkafka.storage.AppStorage
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service

@Service
class KafkaConsumerService {

    private val log = LoggerFactory.getLogger(this.javaClass)

    @KafkaListener(topics = ["my-topic"], groupId = "my-consumer-group")
    fun consume(record: ConsumerRecord<String, String>) {
        log.info("Received message key - {}, value - {}", record.key(), record.value())
        AppStorage.add(record.key(), record.value())
    }
}
