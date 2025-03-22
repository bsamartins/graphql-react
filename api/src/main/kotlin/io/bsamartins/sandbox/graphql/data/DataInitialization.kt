package io.bsamartins.sandbox.graphql.data

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.annotation.PostConstruct
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Component

@Component
class DataInitialization(
    private val objectMapper: ObjectMapper,
    private val movieRepository: MovieRepository,
    private val actorRepository: ActorRepository,
    private val movieCastRepository: MovieCastRepository,
) {
    private val logger = KotlinLogging.logger {}

    @PostConstruct
    fun populate() {
        logger.info { "Populating database" }

        logger.info { "Loading actors" }
        val actors = objectMapper.readValue<List<ActorData>>(ClassPathResource("data/actors.json").file)
        val actorsByName = actors.associateBy { it.name }
        actors.forEach { actor ->
            actorRepository.save(
                Actor(id = actor.id, name = actor.name)
            )
        }

        logger.info { "Loading movies" }
        val movies = objectMapper.readValue<List<MovieData>>(ClassPathResource("data/movies.json").file)
        movies.forEach { movie ->
            movieRepository.save(
                Movie(id = movie.id, title = movie.title)
            )
            movie.actors.forEach { actorName ->
                val actor = actorsByName[actorName]
                if (actor != null) {
                    movieCastRepository.save(MovieCast(actorId = actor.id, movieId = movie.id))
                }
            }
        }

        logger.info { "Done" }
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