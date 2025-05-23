package io.bsamartins.sandbox.graphql.modules.actors

import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsData
import com.netflix.graphql.dgs.DgsQuery
import graphql.relay.Connection
import graphql.schema.DataFetchingEnvironment
import io.bsamartins.sandbox.graphql.codegen.types.Actor
import io.bsamartins.sandbox.graphql.codegen.types.Cast
import io.bsamartins.sandbox.graphql.graphql.asConnection
import io.bsamartins.sandbox.graphql.graphql.dataloaders.ActorDataLoader
import io.bsamartins.sandbox.graphql.graphql.dataloaders.ActorProfilePhotoDataLoader
import java.util.concurrent.CompletableFuture
import io.bsamartins.sandbox.graphql.modules.actors.Actor as ActorData


@DgsComponent
class ActorsDataFetcher(
    private val actorService: ActorService,
) {
    @DgsQuery
    fun actors(env: DataFetchingEnvironment): Connection<Actor> = actorService.listAll()
        .map { it.toModel() }
        .asConnection(env)

    @DgsData(parentType = "Cast", field = "actor")
    fun castActor(env: DataFetchingEnvironment): CompletableFuture<Actor> {
        val actorDataLoader = ActorDataLoader.get(env)
        val cast = env.getSource<Cast>()!!
        return actorDataLoader.load(cast.actor.id)
    }

    @DgsData(parentType = "Actor", field = "profilePhotoUrl")
    fun profileImageUrl(env: DataFetchingEnvironment): CompletableFuture<String?> {
        val actor = env.getSource<Actor>()!!
        return ActorProfilePhotoDataLoader.get(env)
            .load(actor.id)
    }
}

internal fun ActorData.toModel(): Actor =
    Actor(
        id = id,
        name = name,
    )
