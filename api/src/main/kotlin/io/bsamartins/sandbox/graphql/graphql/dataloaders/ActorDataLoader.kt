package io.bsamartins.sandbox.graphql.graphql.dataloaders

import com.netflix.graphql.dgs.DgsDataLoader
import graphql.schema.DataFetchingEnvironment
import io.bsamartins.sandbox.graphql.codegen.types.Actor
import io.bsamartins.sandbox.graphql.modules.actors.ActorService
import io.bsamartins.sandbox.graphql.graphql.toModel
import io.github.oshai.kotlinlogging.KotlinLogging
import org.dataloader.DataLoader
import org.dataloader.MappedBatchLoader
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletionStage

@DgsDataLoader(name = "actor")
class ActorDataLoader(
    var actorService: ActorService,
) : MappedBatchLoader<Int, Actor> {
    private val logger = KotlinLogging.logger {}

    companion object {
        fun get(env: DataFetchingEnvironment): DataLoader<Int, Actor> = env.getDataLoader("actor")!!
    }

    override fun load(keys: Set<Int>): CompletionStage<Map<Int, Actor>> {
        return CompletableFuture.supplyAsync {
            logger.info { "Loading actors ${keys.size}" }
            keys.mapNotNull { actorId -> actorService.findById(actorId)?.let { actor -> actorId to actor.toModel() } }
                .toMap()
        }
    }
}
