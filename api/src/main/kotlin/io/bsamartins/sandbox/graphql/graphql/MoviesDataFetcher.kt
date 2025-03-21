package io.bsamartins.sandbox.graphql.graphql

import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsQuery
import graphql.relay.Connection
import graphql.schema.DataFetchingEnvironment
import io.bsamartins.sandbox.graphql.codegen.types.Movie
import io.bsamartins.sandbox.graphql.data.MovieService

@DgsComponent
class MoviesDataFetcher(
    private val movieService: MovieService,
) {
    @DgsQuery
    fun movies(env: DataFetchingEnvironment): Connection<Movie> = movieService.listAll().asConnection(env)
}
