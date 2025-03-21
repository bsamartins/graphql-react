package io.bsamartins.sandbox.graphql.data

import com.fasterxml.jackson.databind.ObjectMapper
import com.netflix.dgs.codegen.generated.types.Actor
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Service

@Service
class ActorService(
    objectMapper: ObjectMapper,
) {
    private val actors: List<Actor> by lazy {
        val arrayNode = objectMapper.readTree(ClassPathResource("data/actors.json").file)
        arrayNode.map { node ->
            Actor(id = node.get("objectID").asText(), name = node.get("name").asText())
        }
    }

    fun listAll(): List<Actor> = actors
}