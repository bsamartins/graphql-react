package io.bsamartins.sandbox.graphql.modules.actors

import io.bsamartins.sandbox.graphql.modules.movies.Movie
import jakarta.persistence.Entity
import jakarta.persistence.Id
import org.springframework.data.domain.KeysetScrollPosition
import org.springframework.data.domain.Window
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ActorRepository : JpaRepository<Actor, Int> {
    fun findByName(name: String): Actor?

    fun findFirst5By(position: KeysetScrollPosition): Window<Actor>
}

@Entity
class Actor(
    @Id
    val id: Int,

    val name: String,
)