package io.bsamartins.sandbox.graphql.graphql

import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsQuery
import com.netflix.graphql.dgs.InputArgument
import graphql.relay.Connection
import graphql.schema.DataFetchingEnvironment
import io.bsamartins.sandbox.graphql.codegen.types.Movie
import io.bsamartins.sandbox.graphql.data.MovieService

@DgsComponent
class MoviesDataFetcher(
    private val movieService: MovieService,
) {
    @DgsQuery
    fun movies(
        env: DataFetchingEnvironment,
        @InputArgument("first") first: Int?,
        @InputArgument("last") last: Int?,
        @InputArgument("after") after: String?,
        @InputArgument("before") before: Int?,
    ): Connection<Movie> = movieService.listAll(first ?: 30).asConnection(env)
}
