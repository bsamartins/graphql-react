package io.bsamartins.sandbox.graphql.data

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Service

@Service
class MovieService(
    objectMapper: ObjectMapper,
) {
    private val movies: List<Movie> = objectMapper.readValue<List<Movie>>(ClassPathResource("data/movies.json").file)

    fun listAll(pageRequest: PageRequest): List<Movie> = movies
}

data class Movie(
    @JsonProperty("objectID")
    val id: String,
    val title: String,
    val actors: List<String>
)
