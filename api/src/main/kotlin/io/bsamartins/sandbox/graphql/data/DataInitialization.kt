package io.bsamartins.sandbox.graphql.data

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.MappingIterator
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.csv.CsvMapper
import com.fasterxml.jackson.dataformat.csv.CsvParser
import com.fasterxml.jackson.module.kotlin.readValue
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.boot.actuate.endpoint.annotation.WriteOperation
import org.springframework.boot.actuate.endpoint.web.annotation.WebEndpoint
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Component
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicLong
import java.util.zip.ZipFile


@Component
@WebEndpoint(id = "load-data")
class DataInitialization(
    private val objectMapper: ObjectMapper,
    private val movieRepository: MovieRepository,
    private val actorRepository: ActorRepository,
    private val movieCastRepository: MovieCastRepository,
) {
    private val logger = KotlinLogging.logger {}
    private val csvMapper = CsvMapper()
    private val loadInProgress = AtomicBoolean(false)

    @WriteOperation
    fun populate() {
        if (!loadInProgress.get()) {
            loadInProgress.set(true)
            try {
                load()
            } finally {
                loadInProgress.set(false)
            }
        } else {
            logger.info { "Load in progress" }
        }
    }

    private fun load() {
        logger.info { "Populating database" }

        logger.info { "Opening dataset" }
        ZipFile(ClassPathResource("data/dataset.zip").file).use { zip ->
            loadMovies(zip)

            val creditsEntry = zip.getEntry("tmdb_5000_credits.csv")
            zip.getInputStream(creditsEntry).use { zipStream ->
                val it: MappingIterator<List<String>> = csvMapper.readerForListOf(String::class.java)
                    .with(CsvParser.Feature.WRAP_AS_ARRAY)
                    .readValues(zipStream)

                logger.info { "Loading actors" }
                var rowNum = 0
                while (it.hasNext()) {
                    val row = it.next()
                    if (rowNum != 0) {
                        val movieId = row[0].toInt()
                        val castData = row[2]
                        val cast = try {
                            objectMapper.readValue<List<CastData>>(castData)
                        } catch (e: Exception) {
                            logger.error { "Failed to parse cast data: $castData" }
                            throw e
                        }
                        cast.forEach { castMember ->
                            if (!actorRepository.existsById(castMember.actorId)) {
                                actorRepository.save(
                                    Actor(id = castMember.actorId, name = castMember.name)
                                )
                            }
                            movieCastRepository.save(
                                MovieCast(
                                    movieId = movieId,
                                    actorId = castMember.actorId,
                                    character = castMember.character,
                                    order = castMember.order,
                                )
                            )
                        }
                    }
                    rowNum++
                }
            }
        }

        logger.info { "Done" }
    }

    private fun loadMovies(zip: ZipFile) {
        val moviesEntry = zip.getEntry("tmdb_5000_movies.csv")
        zip.getInputStream(moviesEntry).use { zipStream ->
            val it: MappingIterator<List<String>> = csvMapper.readerForListOf(String::class.java)
                .with(CsvParser.Feature.WRAP_AS_ARRAY)
                .readValues(zipStream)

            val progress = AtomicLong(0)
            val progressThread = Thread {
                while (!Thread.interrupted()) {
                    logger.info { "Loading progress: ${progress.get()}" }
                    try {
                        Thread.sleep(500)
                    } catch (e: InterruptedException) {
                        break
                    }
                }
            }

            progressThread.start()

            logger.info { "Loading movies" }
            while (it.hasNext()) {
                val row = it.next()
                if (progress.get() != 0L) {
                    val id = row[3].toInt()
                    val title = row[6]
                    movieRepository.save(Movie(id = id, title = title))
                }
                progress.incrementAndGet()
            }
            progressThread.interrupt()
            progressThread.join()

            logger.info { "Loaded ${progress.get()} movies" }
        }
    }

    private data class CastData(
        @JsonProperty("cast_id")
        val castId: Int,
        val character: String,
        val name: String,
        @JsonProperty("credit_id")
        val creditId: String,
        val gender: Int,
        @JsonProperty("id")
        val actorId: Int,
        val order: Int,
    )
}