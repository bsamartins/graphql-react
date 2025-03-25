package io.bsamartins.sandbox.graphql.modules.actors

import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsData
import com.netflix.graphql.dgs.DgsQuery
import graphql.relay.Connection
import graphql.relay.DefaultConnection
import graphql.relay.DefaultConnectionCursor
import graphql.relay.DefaultEdge
import graphql.relay.DefaultPageInfo
import graphql.schema.DataFetchingEnvironment
import io.bsamartins.sandbox.graphql.codegen.types.Actor
import io.bsamartins.sandbox.graphql.codegen.types.Cast
import io.bsamartins.sandbox.graphql.graphql.dataloaders.ActorDataLoader
import io.bsamartins.sandbox.graphql.graphql.dataloaders.ActorProfilePhotoDataLoader
import org.springframework.data.domain.ScrollPosition
import org.springframework.data.domain.Window
import java.util.Base64
import java.util.concurrent.CompletableFuture
import io.bsamartins.sandbox.graphql.modules.actors.Actor as ActorData


@DgsComponent
class ActorsDataFetcher(
    private val actorService: ActorService,
) {
    @DgsQuery
    fun actors(): Connection<Actor> = actorService.findWindow(ScrollPosition.keyset())
        .map { it.toModel() }
        .asConnection()

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

fun <T> Window<T>.asConnection(): Connection<T> {
    val edges = map { e ->
        DefaultEdge(e, DefaultConnectionCursor(buildCursor()))
    }.toList()
    val pageInfo = DefaultPageInfo()
    return DefaultConnection(edges, pageInfo)
}

fun buildCursor(): String {
    return Base64.getEncoder().encodeToString("".toByteArray()).toString()
}
