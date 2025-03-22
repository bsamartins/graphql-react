package io.bsamartins.sandbox.graphql.data

import org.springframework.stereotype.Service

@Service
class MovieService(
    private val movieRepository: MovieRepository,
    private val movieCastRepository: MovieCastRepository,
) {
    fun listAll(pageRequest: PageRequest): List<Movie> = movieRepository.findAll()

    fun search(pageRequest: PageRequest, query: String? = null): List<Movie> = movieRepository.searchAll(query)

    fun getCast(movieId: String): Set<String> =
        movieCastRepository.findAllByMovieId(movieId).map { it.actorId }.toSet()
}
