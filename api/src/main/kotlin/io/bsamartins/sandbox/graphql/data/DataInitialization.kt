package io.bsamartins.sandbox.graphql.data

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import jakarta.annotation.PostConstruct
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Component

@Component
class DataInitialization(
    private val objectMapper: ObjectMapper,
    private val movieRepository: MovieRepository
) {
    @PostConstruct
    fun populate() {
        val movies = objectMapper.readValue<List<MovieData>>(ClassPathResource("data/movies.json").file)
        movies.forEach {
            movieRepository.save(
                Movie(id = it.id, title = it.title)
            )
        }
    }

    private data class MovieData(
        @JsonProperty("objectID")
        val id: String,
        val title: String,
        val actors: List<String>
    )
}