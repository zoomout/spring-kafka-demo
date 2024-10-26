package com.bz.springkafka.spring_kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class KafkaMessageController {

    private final KafkaSender kafkaSender;

    @PostMapping
    public Mono<String> sendMessage(@RequestBody String message) {
        return kafkaSender.sendMessage(message);
    }

    @GetMapping("/{id}")
    public Mono<String> getMessage(@PathVariable String id) {
        return Mono.justOrEmpty(String.valueOf(AppStorage.get(id)));
    }

}
