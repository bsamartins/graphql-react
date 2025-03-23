package io.bsamartins.sandbox.graphql.graphql

import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsQuery
import com.netflix.graphql.dgs.InputArgument
import graphql.relay.Connection
import graphql.schema.DataFetchingEnvironment
import io.bsamartins.sandbox.graphql.codegen.types.Movie
import io.bsamartins.sandbox.graphql.data.MovieService
import io.bsamartins.sandbox.graphql.data.Movie as MovieData

@DgsComponent
class MoviesDataFetcher(
    private val movieService: MovieService,
) {
    @DgsQuery
    fun movies(
        env: DataFetchingEnvironment,
        @InputArgument("query") query: String?,
    ): Connection<Movie> {
        val pageRequest = env.getPageRequest()
        return movieService.search(pageRequest, query = query)
            .map { it.toModel() }
            .asConnection(env)
    }
}

private fun MovieData.toModel(): Movie =
    Movie(
        id = id.toInt(),
        name = title,
        cast = emptyList()
    )
