package com.bz.springkafka.spring_kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaSender {

    private final ReactiveKafkaProducerTemplate<String, String> kafkaProducerTemplate;

    public Mono<String> sendMessage(String message) {
        final String id = UUID.randomUUID().toString();
        return kafkaProducerTemplate.send(AppGlobalVars.TOPIC, message)
                .doOnSuccess(result -> {
                    log.info("Sent message id - {}, payload - {}", id, message);
                    AppStorage.add(id, message);
                })
                .doOnError(error -> log.error("Failed to send message: {}", error.getMessage()))
                .then()
                .onErrorMap(e -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to send message", e))
                .then(Mono.just(id));
    }

}
