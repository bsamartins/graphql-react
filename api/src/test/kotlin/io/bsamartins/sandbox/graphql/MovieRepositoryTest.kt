package io.bsamartins.sandbox.graphql

import io.bsamartins.sandbox.graphql.modules.movies.Movie
import io.bsamartins.sandbox.graphql.modules.movies.MovieRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.KeysetScrollPosition
import org.springframework.data.domain.ScrollPosition
import org.springframework.data.support.WindowIterator

class MovieRepositoryTest : AbstractIntegrationTest() {

    @Autowired
    private lateinit var movieRepository: MovieRepository

    @BeforeEach
    fun setup() {
        (0..<7).forEach { i ->
            movieRepository.save(Movie(id = i, title = "a"))
        }
    }

    @AfterEach
    fun tearDown() {
        movieRepository.deleteAll()
    }

    @Test
    fun manual() {
//        val window = movieRepository.findAll(ScrollPosition.keyset())
//        println(window)
//        val position = window.positionAt(window.size() - 1)
//        println(position)
    }

    @Test
    fun all() {
        val iterator = WindowIterator.of { position ->
            println("position=$position")
            movieRepository.findFirst5ByOrderByTitle(position as KeysetScrollPosition)
        }.startingAt(ScrollPosition.keyset())

        iterator.forEach { println(it) }
    }
}