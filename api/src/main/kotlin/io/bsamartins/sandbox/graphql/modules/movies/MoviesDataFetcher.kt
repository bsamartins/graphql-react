package io.bsamartins.sandbox.graphql.modules.movies

import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsData
import com.netflix.graphql.dgs.DgsQuery
import com.netflix.graphql.dgs.InputArgument
import graphql.relay.Connection
import graphql.schema.DataFetchingEnvironment
import io.bsamartins.sandbox.graphql.codegen.types.Actor
import io.bsamartins.sandbox.graphql.codegen.types.Cast
import io.bsamartins.sandbox.graphql.codegen.types.Movie
import io.bsamartins.sandbox.graphql.graphql.asConnection
import io.bsamartins.sandbox.graphql.graphql.dataloaders.MoviePosterDataLoader
import io.bsamartins.sandbox.graphql.graphql.getPageRequest
import java.util.concurrent.CompletableFuture

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

    @DgsData(parentType = "Movie", field = "cast")
    fun movieActors(env: DataFetchingEnvironment): List<Cast> {
        val movie = env.getSource<Movie>()!!
        return movieService.getCast(movie.id)
            .map {
                Cast(
                    actor = Actor(id = it.actorId, name = ""),
                    character = it.character,
                )
            }
    }

    @DgsData(parentType = "Movie", field = "posterUrl")
    fun moviePoster(env: DataFetchingEnvironment): CompletableFuture<String?> {
        val movie = env.getSource<Movie>()!!
        return MoviePosterDataLoader.get(env)
            .load(movie.id)
    }
}

fun io.bsamartins.sandbox.graphql.modules.movies.Movie.toModel(): Movie =
    Movie(
        id = id,
        title = title,
        cast = emptyList()
    )
