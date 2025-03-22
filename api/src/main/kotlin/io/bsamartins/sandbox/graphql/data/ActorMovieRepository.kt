package io.bsamartins.sandbox.graphql.data

import jakarta.persistence.Embeddable
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.IdClass
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MovieCastRepository :  JpaRepository<MovieCast, MovieCast.Key>

@Entity
@IdClass(MovieCast.Key::class)
class MovieCast(
    @Id
    val actorId: String,

    @Id
    val movieId: String,

) {
    @Embeddable
    data class Key(
        val actorId: String,
        val movieId: String,
    )
}

