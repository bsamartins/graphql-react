package io.bsamartins.sandbox.graphql.modules.movies

import io.bsamartins.sandbox.graphql.PageRequest
import org.springframework.stereotype.Service

@Service
class MovieService(
    private val movieRepository: MovieRepository,
    private val movieCastRepository: MovieCastRepository,
) {
    fun listAll(pageRequest: PageRequest): List<Movie> = movieRepository.findAll()

    fun search(pageRequest: PageRequest, query: String? = null): List<Movie> = movieRepository.searchAll(query)

    fun getCast(movieId: Int): List<MovieCast> =
        movieCastRepository.findAllByMovieIdOrderByOrder(movieId)
}
