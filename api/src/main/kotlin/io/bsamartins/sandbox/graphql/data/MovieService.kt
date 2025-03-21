package io.bsamartins.sandbox.graphql.data

import com.fasterxml.jackson.databind.ObjectMapper
import io.bsamartins.sandbox.graphql.codegen.types.Movie
import io.bsamartins.sandbox.graphql.graphql.PageRequest
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Service
import java.util.*

@Service
class MovieService(
    objectMapper: ObjectMapper,
) {
    private val movies: Map<String, Movie> by lazy {
        val arrayNode = objectMapper.readTree(ClassPathResource("data/movies.json").file)
        val result = TreeMap<String, Movie>()
        arrayNode.forEach { node ->
            val movie = Movie(id = node.get("objectID").asText(), name = node.get("title").asText())
            result[movie.id] = movie
        }
        result
    }

    fun listAll(pageRequest: PageRequest): List<Movie> = movies.values.toList().subList(0, pageRequest.first ?: 30)
}