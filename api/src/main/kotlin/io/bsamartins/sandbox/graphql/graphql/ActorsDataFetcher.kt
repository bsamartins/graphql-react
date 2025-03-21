package io.bsamartins.sandbox.graphql.graphql

import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsData
import com.netflix.graphql.dgs.DgsDataLoader
import com.netflix.graphql.dgs.DgsQuery
import graphql.relay.Connection
import graphql.schema.DataFetchingEnvironment
import io.bsamartins.sandbox.graphql.codegen.types.Actor
import io.bsamartins.sandbox.graphql.codegen.types.Movie
import io.bsamartins.sandbox.graphql.data.ActorService
import org.dataloader.MappedBatchLoader
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletionStage
import io.bsamartins.sandbox.graphql.data.Actor as ActorData


@DgsComponent
class ActorsDataFetcher(
    private val actorService: ActorService,
) {
    @DgsQuery
    fun actors(env: DataFetchingEnvironment): Connection<Actor> = actorService.listAll()
        .map { it.toModel() }
        .asConnection(env)

    @DgsData(parentType = "Movie", field = "actors")
    fun movieActors(env: DataFetchingEnvironment): CompletableFuture<List<Actor>> {
        val dataLoader = env.getDataLoader<String, Actor>("actors")!!
        val movie = env.getSource<Movie>()!!
        return dataLoader.loadMany(movie.actors.map { it.name })
            .thenApply { it.filterNotNull() }
    }
}

@DgsDataLoader(name = "actors")
class DirectorsDataLoader(
    var actorService: ActorService,
) : MappedBatchLoader<String, Actor> {

    override fun load(keys: Set<String>): CompletionStage<Map<String, Actor>> {
        return CompletableFuture.supplyAsync {
            keys.mapNotNull { key -> actorService.findByName(key)?.let { key to it.toModel() } }
                .toMap()
        }
    }
}

internal fun ActorData.toModel(): Actor =
    Actor(
        id = id,
        name = name,
    )

internal fun actorPartial(name: String): Actor =
    Actor(id = "", name = name)
