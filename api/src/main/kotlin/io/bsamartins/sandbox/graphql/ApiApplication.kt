package io.bsamartins.sandbox.graphql

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

fun main() {
    runApplication<ApiApplication>()
}

@SpringBootApplication
@EnableJpaRepositories
class ApiApplication
