package io.bsamartins.sandbox.graphql.data

import jakarta.persistence.Entity
import jakarta.persistence.Id
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ActorRepository : JpaRepository<Actor, Int> {
    fun findByName(name: String): Actor?
}

@Entity
class Actor(
    @Id
    val id: Int,

    val name: String,
)