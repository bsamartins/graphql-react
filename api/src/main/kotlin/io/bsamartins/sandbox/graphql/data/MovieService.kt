package io.bsamartins.sandbox.graphql.data

import org.springframework.stereotype.Service

@Service
class MovieService(
    private val movieRepository: MovieRepository,
) {
    fun listAll(pageRequest: PageRequest): List<Movie> = movieRepository.findAll()
}
