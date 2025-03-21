package io.bsamartins.sandbox.graphql.data

import com.fasterxml.jackson.databind.ObjectMapper
import com.netflix.dgs.codegen.generated.types.Actor
import com.netflix.dgs.codegen.generated.types.Movie
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Service

@Service
class MovieService(
    objectMapper: ObjectMapper,
) {
    private val movies: List<Movie> by lazy {
        val arrayNode = objectMapper.readTree(ClassPathResource("data/movies.json").file)
        arrayNode.map { node ->
            Movie(id = node.get("objectID").asText(), name = node.get("title").asText())
        }
    }

    fun listAll(): List<Movie> = movies
}