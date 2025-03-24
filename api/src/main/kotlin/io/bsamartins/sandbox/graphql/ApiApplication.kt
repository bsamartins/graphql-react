package io.bsamartins.sandbox.graphql

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

fun main() {
    runApplication<ApiApplication>()
}

@SpringBootApplication
@EnableJpaRepositories
@EnableCaching
class ApiApplication
