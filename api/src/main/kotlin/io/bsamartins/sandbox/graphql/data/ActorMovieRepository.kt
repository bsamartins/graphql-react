package io.bsamartins.sandbox.graphql.data

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.IdClass
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MovieCastRepository :  JpaRepository<MovieCast, MovieCast.Key> {
    fun findAllByMovieIdOrderByOrder(movieId: Int): List<MovieCast>
}

@Entity
@IdClass(MovieCast.Key::class)
class MovieCast(
    @Id
    val actorId: Int,

    @Id
    val movieId: Int,

    val character: String,

    @Column(name = "\"order\"")
    val order: Int,
) {
    @Embeddable
    data class Key(
        val actorId: Int,
        val movieId: Int,
    )
}

