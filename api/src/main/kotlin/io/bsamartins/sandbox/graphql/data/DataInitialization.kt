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
    private val movieRepository: MovieRepository,
    private val actorRepository: ActorRepository,
) {
    @PostConstruct
    fun populate() {
        val actors = objectMapper.readValue<List<ActorData>>(ClassPathResource("data/actors.json").file)
        actors.forEach { actor ->
            actorRepository.save(
                Actor(id = actor.id, name = actor.name)
            )
        }

        val movies = objectMapper.readValue<List<MovieData>>(ClassPathResource("data/movies.json").file)
        movies.forEach { movie ->
            movieRepository.save(
                Movie(id = movie.id, title = movie.title)
            )
        }
    }

    private data class MovieData(
        @JsonProperty("objectID")
        val id: String,
        val title: String,
        val actors: List<String>
    )

    private data class ActorData(
        @JsonProperty("objectID")
        val id: String,
        val name: String,
    )
}