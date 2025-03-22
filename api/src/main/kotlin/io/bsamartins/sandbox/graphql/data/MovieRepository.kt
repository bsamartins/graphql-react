package io.bsamartins.sandbox.graphql.data

import jakarta.persistence.Entity
import jakarta.persistence.EntityManager
import jakarta.persistence.Id
import org.hibernate.search.mapper.orm.Search
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository

@Repository
interface MovieRepository : JpaRepository<Movie, String>, JpaSpecificationExecutor<Movie>, CustomMovieRepository

interface CustomMovieRepository {
    fun searchAll(query: String? = null): List<Movie>
}

@Repository
class CustomMovieRepositoryImpl(
    private val entityManager: EntityManager
) : CustomMovieRepository {
    override fun searchAll(query: String?): List<Movie> {
        Search.session(entityManager)
            .search(Movie::class.java)
            .where { f ->
                f.match()
                    .fields(Movie::title.name)
                    .matching(query)
            }
        return emptyList()
    }
}

@Entity
@Indexed
class Movie(
    @Id
    val id: String,

    @FullTextField
    val title: String,
)