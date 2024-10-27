package com.bz.springkafka;

import io.restassured.RestAssured;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.shaded.org.awaitility.Awaitility;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class KafkaMessageControllerIntegrationTest {

    @LocalServerPort
    private int port;

    private static KafkaContainer kafkaContainer;

    @BeforeAll
    static void setup() {
        kafkaContainer = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:latest"));
        kafkaContainer.start();
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @DynamicPropertySource
    static void setKafkaProperties(DynamicPropertyRegistry registry) {
        String bootstrapServers = kafkaContainer.getBootstrapServers();
        registry.add("spring.kafka.bootstrap-servers", () -> bootstrapServers);
    }

    @Test
    public void whenSendMessage_thenGetMessageShouldBeTheSame() {
        RestAssured.port = port;
        String message = "Hello Kafka!";
        String id = RestAssured.given()
                .body(message)
                .when()
                .post("/api/messages")
                .then()
                .statusCode(200)
                .extract().body().asString();

        Awaitility.await().atMost(Duration.ofSeconds(5)).untilAsserted(() -> {
            String actual = RestAssured.when().get("/api/messages/" + id)
                    .then()
                    .statusCode(200)
                    .extract().body().asString();
            Assertions.assertThat(actual).isEqualTo(message);
        });
    }

}
