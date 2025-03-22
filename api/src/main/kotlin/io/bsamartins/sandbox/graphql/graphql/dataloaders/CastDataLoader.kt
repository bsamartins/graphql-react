package io.bsamartins.sandbox.graphql.graphql.dataloaders

import com.netflix.graphql.dgs.DgsDataLoader
import graphql.schema.DataFetchingEnvironment
import io.bsamartins.sandbox.graphql.codegen.types.Cast
import io.bsamartins.sandbox.graphql.data.ActorService
import io.bsamartins.sandbox.graphql.data.MovieCastRepository
import io.bsamartins.sandbox.graphql.graphql.toModel
import org.dataloader.DataLoader
import org.dataloader.MappedBatchLoader
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletionStage

@DgsDataLoader(name = "cast")
class CastDataLoader(
    var actorService: ActorService,
    var movieCastRepository: MovieCastRepository,
) : MappedBatchLoader<String, List<Cast>> {

    companion object {
        fun get(env: DataFetchingEnvironment): DataLoader<String, List<Cast>> = env.getDataLoader("cast")!!
    }

    override fun load(keys: Set<String>): CompletionStage<Map<String, List<Cast>>> {
        return CompletableFuture.supplyAsync {
            keys.associateWith { movieId -> resolveMovieCast(movieId) }
        }
    }

    private fun resolveMovieCast(movieId: String): List<Cast> {
        return movieCastRepository.findAllByMovieId(movieId)
            .mapNotNull { cast -> actorService.findById(cast.actorId) }
            .map { actor -> Cast(actor = actor.toModel()) }
    }
}