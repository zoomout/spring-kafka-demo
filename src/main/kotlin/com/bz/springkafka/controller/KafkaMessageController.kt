package com.bz.springkafka.controller

import com.bz.springkafka.kafka.KafkaProducerService
import com.bz.springkafka.storage.AppStorage
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/messages")
class KafkaMessageController(private val kafkaProducerService: KafkaProducerService) {

    @PostMapping
    fun sendMessage(@RequestBody message: String): String {
        return kafkaProducerService.sendMessage("my-topic", message)
    }

    @GetMapping("/{id}")
    fun readMessage(@PathVariable id: String): ResponseEntity<String> {
        return AppStorage.get(id)?.let { ResponseEntity.ok(it) } ?: ResponseEntity.notFound().build()
    }
}
