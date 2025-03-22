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
import io.bsamartins.sandbox.graphql.graphql.dataloaders.CastDataLoader
import java.util.concurrent.CompletableFuture
import io.bsamartins.sandbox.graphql.data.Actor as ActorData


@DgsComponent
class ActorsDataFetcher(
    private val actorService: ActorService,
) {
    @DgsQuery
    fun actors(env: DataFetchingEnvironment): Connection<Actor> = actorService.listAll()
        .map { it.toModel() }
        .asConnection(env)

    @DgsData(parentType = "Movie", field = "cast")
    fun movieActors(env: DataFetchingEnvironment): CompletableFuture<List<Cast>> {
        val dataLoader = CastDataLoader.get(env)
        val movie = env.getSource<Movie>()!!
        return dataLoader.load(movie.id)
    }
}

internal fun ActorData.toModel(): Actor =
    Actor(
        id = id,
        name = name,
    )

internal fun actorPartial(name: String): Actor =
    Actor(id = "", name = name)
