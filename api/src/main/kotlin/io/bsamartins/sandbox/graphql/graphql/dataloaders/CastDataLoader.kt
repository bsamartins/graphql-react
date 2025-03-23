package io.bsamartins.sandbox.graphql.graphql.dataloaders

import com.netflix.graphql.dgs.DgsDataLoader
import graphql.schema.DataFetchingEnvironment
import io.bsamartins.sandbox.graphql.data.MovieCastRepository
import io.github.oshai.kotlinlogging.KotlinLogging
import org.dataloader.BatchLoaderEnvironment
import org.dataloader.DataLoader
import org.dataloader.MappedBatchLoaderWithContext
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletionStage

@DgsDataLoader(name = "cast")
class CastDataLoader(
    var movieCastRepository: MovieCastRepository,
) : MappedBatchLoaderWithContext<Long, List<Long>> {

    companion object {
        fun get(env: DataFetchingEnvironment): DataLoader<Long, List<Long>> = env.getDataLoader("cast")!!
    }

    private val logger = KotlinLogging.logger {}

    override fun load(keys: Set<Long>, environment: BatchLoaderEnvironment): CompletionStage<Map<Long, List<Long>>> {
        return CompletableFuture.supplyAsync {
            logger.info { "Loading cast ${keys.size}" }
            keys.associateWith { movieId -> resolveMovieCast(movieId) }
        }
    }

    private fun resolveMovieCast(movieId: Long): List<Long> {
        return movieCastRepository.findAllByMovieIdOrderByOrder(movieId)
            .map { it.actorId }
    }
}