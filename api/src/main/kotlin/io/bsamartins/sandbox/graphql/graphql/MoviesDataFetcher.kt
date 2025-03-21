package io.bsamartins.sandbox.graphql.graphql

import com.netflix.dgs.codegen.generated.types.Movie
import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsQuery
import io.bsamartins.sandbox.graphql.data.MovieService

@DgsComponent
class MoviesDataFetcher(
    private val movieService: MovieService,
) {
    @DgsQuery
    fun movies(): List<Movie> = movieService.listAll()
}
