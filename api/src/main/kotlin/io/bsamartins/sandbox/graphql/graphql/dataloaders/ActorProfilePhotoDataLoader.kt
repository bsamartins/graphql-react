package io.bsamartins.sandbox.graphql.graphql.dataloaders

import com.netflix.graphql.dgs.DgsDataLoader
import graphql.schema.DataFetchingEnvironment
import io.bsamartins.sandbox.graphql.client.tmdb.PosterSize
import io.bsamartins.sandbox.graphql.client.tmdb.ProfilePictureSize
import io.bsamartins.sandbox.graphql.client.tmdb.TmdbClient
import io.github.oshai.kotlinlogging.KotlinLogging
import org.dataloader.BatchLoader
import org.dataloader.DataLoader
import org.dataloader.MappedBatchLoader
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletionStage

@DgsDataLoader(name = "actorImage")
class ActorProfilePhotoDataLoader(
    val tmdbClient: TmdbClient,
) : BatchLoader<Int, String?> {

    companion object {
        fun get(env: DataFetchingEnvironment): DataLoader<Int, String?> = env.getDataLoader("actorImage")!!
    }

    private val logger = KotlinLogging.logger {}

    override fun load(keys: List<Int>): CompletionStage<List<String?>> {
        return CompletableFuture.supplyAsync {
            logger.info { "Loading actor photos" }
            keys.map { actorId ->
                try {
                    tmdbClient.personProfilePictures(actorId, ProfilePictureSize.MEDIUM)?.firstOrNull()
                } catch (e: Exception) {
                    logger.warn(e) { "Unable to retrieve photo" }
                    null
                }
            }
        }
    }
}