package com.bz.springkafka

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class SpringKafkaApplication {
    fun main(args: Array<String>) {
        SpringApplication.run(SpringKafkaApplication::class.java, *args)
    }
}
