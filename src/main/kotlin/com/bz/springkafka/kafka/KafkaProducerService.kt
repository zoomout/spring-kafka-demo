package com.bz.springkafka.kafka

import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import java.util.*

@Service
class KafkaProducerService(private val kafkaTemplate: KafkaTemplate<String, String>) {

    private val log = LoggerFactory.getLogger(this.javaClass)

    fun sendMessage(topic: String, message: String): String {
        val key = UUID.randomUUID().toString()
        kafkaTemplate.send(topic, key, message)
        log.info("Sent message key - {}, value - {}", key, message)
        return key
    }
}
