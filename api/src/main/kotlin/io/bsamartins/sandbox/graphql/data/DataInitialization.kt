package io.bsamartins.sandbox.graphql.data

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.boot.actuate.endpoint.annotation.WriteOperation
import org.springframework.boot.actuate.endpoint.web.annotation.WebEndpoint
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Component
import java.util.concurrent.atomic.AtomicBoolean

@Component
@WebEndpoint(id = "load-data")
class DataInitialization(
    private val objectMapper: ObjectMapper,
    private val movieRepository: MovieRepository,
    private val actorRepository: ActorRepository,
    private val movieCastRepository: MovieCastRepository,
) {
    private val logger = KotlinLogging.logger {}
    private val atomicBoolean = AtomicBoolean(false)

    @WriteOperation
    fun populate() {
        if (atomicBoolean.get()) {
            atomicBoolean.set(true)
            try {
                load()
            } finally {
                atomicBoolean.set(false)
            }
        } else {
            logger.info { "Load in progress" }
        }
    }

    private fun load() {
        logger.info { "Populating database" }

        logger.info { "Loading actors" }
        val actors = objectMapper.readValue<List<ActorData>>(ClassPathResource("data/actors.json").file)
        val actorsByName = actors.associateBy { it.name }
        actors.stream().parallel().forEach { actor ->
            actorRepository.save(
                Actor(id = actor.id, name = actor.name)
            )
        }

        logger.info { "Loading movies" }
        val movies = objectMapper.readValue<List<MovieData>>(ClassPathResource("data/movies.json").file)
        var progress = 0
        movies.chunked(50).stream().parallel().forEach { chunks ->
            val toInsert = chunks.map { movie -> Movie(id = movie.id, title = movie.title) }
            movieRepository.saveAll(toInsert)
            progress += toInsert.count()
            logger.info { "Movie inserted $progress/${movies.size}" }
        }

        movies.flatMap { movie ->
            movie.actors.mapNotNull { actorName ->
                actorsByName[actorName]?.let { actor -> movie to actor }
            }
        }.parallelStream()
            .forEach { (movie, actor) ->
                movieCastRepository.save(MovieCast(actor.id, movie.id))
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