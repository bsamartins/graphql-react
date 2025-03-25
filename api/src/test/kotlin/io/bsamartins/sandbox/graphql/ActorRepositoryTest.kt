package io.bsamartins.sandbox.graphql

import io.bsamartins.sandbox.graphql.modules.actors.Actor
import io.bsamartins.sandbox.graphql.modules.actors.ActorRepository
import io.bsamartins.sandbox.graphql.modules.movies.Movie
import io.bsamartins.sandbox.graphql.modules.movies.MovieRepository
import net.datafaker.Faker
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.KeysetScrollPosition
import org.springframework.data.domain.ScrollPosition
import org.springframework.data.support.WindowIterator

class ActorRepositoryTest : AbstractIntegrationTest() {

    @Autowired
    private lateinit var actorRepository: ActorRepository

    private val faker = Faker()

    @BeforeEach
    fun setup() {
        (0..<7).forEach { i ->
            actorRepository.save(Actor(id = i, name = faker.name().name()))
        }
    }

    @AfterEach
    fun tearDown() {
        actorRepository.deleteAll()
    }

    @Test
    fun manual() {
        var window = actorRepository.findFirst5By(ScrollPosition.keyset())
        println(window)
        val nextPosition = window.positionAt(window.size() - 1) as KeysetScrollPosition
        println(nextPosition)
        window = actorRepository.findFirst5By(nextPosition)
        println(window)

    }

    @Test
    fun all() {
        val iterator = WindowIterator.of { position ->
            println("position=$position")
            actorRepository.findFirst5By(position as KeysetScrollPosition)
        }.startingAt(ScrollPosition.keyset())

        iterator.forEach { println(it) }
    }
}