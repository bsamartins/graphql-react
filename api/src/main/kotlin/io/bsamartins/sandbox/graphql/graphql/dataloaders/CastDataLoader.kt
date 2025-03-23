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
) : MappedBatchLoaderWithContext<Int, List<Int>> {

    companion object {
        fun get(env: DataFetchingEnvironment): DataLoader<Long, List<Long>> = env.getDataLoader("cast")!!
    }

    private val logger = KotlinLogging.logger {}

    override fun load(keys: Set<Int>, environment: BatchLoaderEnvironment): CompletionStage<Map<Int, List<Int>>> {
        return CompletableFuture.supplyAsync {
            logger.info { "Loading cast ${keys.size}" }
            keys.associateWith { movieId -> resolveMovieCast(movieId) }
        }
    }

    private fun resolveMovieCast(movieId: Int): List<Int> {
        return movieCastRepository.findAllByMovieIdOrderByOrder(movieId)
            .map { it.actorId }
    }
}