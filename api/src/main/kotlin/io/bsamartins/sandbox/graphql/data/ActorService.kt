package io.bsamartins.sandbox.graphql.data

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Service

@Service
class ActorService(
    objectMapper: ObjectMapper,
) {
    private val actors: List<Actor> = objectMapper.readValue<List<Actor>>(ClassPathResource("data/actors.json").file)

    fun listAll(): List<Actor> = actors

    fun findByName(name: String): Actor? = actors.firstOrNull { it.name == name }
}

data class Actor(
    @JsonProperty("objectID")
    val id: String,
    val name: String,
)
