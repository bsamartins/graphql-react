package io.bsamartins.sandbox.graphql.data

import jakarta.persistence.Entity
import jakarta.persistence.EntityManager
import jakarta.persistence.Id
import org.hibernate.search.engine.search.query.dsl.SearchQueryOptionsStep
import org.hibernate.search.engine.search.query.dsl.SearchQuerySelectStep
import org.hibernate.search.mapper.orm.Search
import org.hibernate.search.mapper.orm.session.SearchSession
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository

@Repository
interface MovieRepository : JpaRepository<Movie, Int>, JpaSpecificationExecutor<Movie>, CustomMovieRepository

interface CustomMovieRepository {
    fun searchAll(query: String? = null): List<Movie>
}

@Repository
class CustomMovieRepositoryImpl(
    private val entityManager: EntityManager
) : CustomMovieRepository {
    override fun searchAll(query: String?): List<Movie> {
        val searchSession = Search.session(entityManager)
        val movieSearch = searchSession.search<Movie>()
        return movieSearch
            .where { f ->
                if (query != null) {
                    f.match()
                        .fields(Movie::title.name)
                        .matching(query)
                } else {
                    f.matchAll()
                }
            }.fetchAllHits()
    }
}

inline fun <reified T> SearchSession.search() =
    search(T::class.java) as SearchQuerySelectStep<out SearchQueryOptionsStep<*, T, *, *, *>, T, *, *, *, *>

@Entity
@Indexed
class Movie(
    @Id
    val id: Int,

    @FullTextField
    val title: String,
)