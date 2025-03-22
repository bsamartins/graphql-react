package io.bsamartins.sandbox.graphql.data

import jakarta.persistence.Entity
import jakarta.persistence.Id
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ActorRepository : JpaRepository<Actor, String> {
    fun findByName(name: String): Actor?
}

@Entity
class Actor(
    @Id
    val id: String,

    val name: String,
)