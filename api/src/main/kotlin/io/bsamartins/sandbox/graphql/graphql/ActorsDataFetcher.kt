package io.bsamartins.sandbox.graphql.graphql

import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsData
import com.netflix.graphql.dgs.DgsQuery
import graphql.relay.Connection
import graphql.schema.DataFetchingEnvironment
import io.bsamartins.sandbox.graphql.codegen.types.Actor
import io.bsamartins.sandbox.graphql.codegen.types.Cast
import io.bsamartins.sandbox.graphql.codegen.types.Movie
import io.bsamartins.sandbox.graphql.data.ActorService
import io.bsamartins.sandbox.graphql.data.MovieService
import io.bsamartins.sandbox.graphql.graphql.dataloaders.ActorDataLoader
import java.util.concurrent.CompletableFuture
import io.bsamartins.sandbox.graphql.data.Actor as ActorData


@DgsComponent
class ActorsDataFetcher(
    private val actorService: ActorService,
    private val movieService: MovieService,
) {
    @DgsQuery
    fun actors(env: DataFetchingEnvironment): Connection<Actor> = actorService.listAll()
        .map { it.toModel() }
        .asConnection(env)

    @DgsData(parentType = "Movie", field = "cast")
    fun movieActors(env: DataFetchingEnvironment): CompletableFuture<List<Cast>> {
        val actorDataLoader = ActorDataLoader.get(env)
        val movie = env.getSource<Movie>()!!
        return movieService.getCast(movie.id.toLong()).let {
            actorDataLoader.loadMany(it.toList())
                .thenApply { actors -> actors.map { actor -> Cast(actor = actor, character = "") }.toList() }
        }
    }
}

internal fun ActorData.toModel(): Actor =
    Actor(
        id = id.toInt(),
        name = name,
    )
