package com.bz.springkafka.spring_kafka;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.shaded.org.awaitility.Awaitility;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("it")
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
        String id = given()
                .body(message)
                .when()
                .post("/api/messages")
                .then()
                .statusCode(200)
                .extract().body().asString();

        Awaitility.await().atMost(Duration.ofSeconds(5)).untilAsserted(() -> {
            String actual = when().get("/api/messages/" + id)
                    .then()
                    .statusCode(200)
                    .extract().body().asString();
            assertThat(actual).isEqualTo(message);
        });
    }

}
