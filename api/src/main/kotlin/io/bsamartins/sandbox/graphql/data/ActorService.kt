package io.bsamartins.sandbox.graphql.data

import com.fasterxml.jackson.databind.ObjectMapper
import io.bsamartins.sandbox.graphql.codegen.types.Actor
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Service
import java.util.*

@Service
class ActorService(
    objectMapper: ObjectMapper,
) {
    private val actors: Map<String, Actor> by lazy {
        val arrayNode = objectMapper.readTree(ClassPathResource("data/actors.json").file)
        val result = TreeMap<String, Actor>()
        arrayNode.forEach { node ->
            val actor = Actor(id = node.get("objectID").asText(), name = node.get("name").asText())
            result[actor.id] = actor
        }
        result
    }

    fun listAll(): List<Actor> = actors.values.toList()
}