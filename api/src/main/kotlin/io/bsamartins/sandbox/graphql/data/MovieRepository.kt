package io.bsamartins.sandbox.graphql.data

import jakarta.persistence.Entity
import jakarta.persistence.Id
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MovieRepository : JpaRepository<Movie, String>

@Entity
class Movie(
    @Id
    val id: String,

    val title: String,
)