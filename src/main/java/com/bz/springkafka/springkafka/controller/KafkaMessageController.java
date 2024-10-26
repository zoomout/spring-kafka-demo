package com.bz.springkafka.springkafka.controller;

import com.bz.springkafka.springkafka.kafka.KafkaProducerService;
import com.bz.springkafka.springkafka.storage.AppStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class KafkaMessageController {

    private final KafkaProducerService kafkaProducerService;

    @PostMapping
    public String sendMessage(@RequestBody String message) {
        return kafkaProducerService.sendMessage("my-topic", message);
    }

    @GetMapping("/{id}")
    public String readMessage(@PathVariable String id) {
        return AppStorage.get(id);
    }

}
