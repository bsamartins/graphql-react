package io.bsamartins.sandbox.graphql.graphql

import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsData
import com.netflix.graphql.dgs.DgsDataLoader
import com.netflix.graphql.dgs.DgsQuery
import graphql.relay.Connection
import graphql.schema.DataFetchingEnvironment
import io.bsamartins.sandbox.graphql.codegen.types.Actor
import io.bsamartins.sandbox.graphql.codegen.types.Cast
import io.bsamartins.sandbox.graphql.codegen.types.Movie
import io.bsamartins.sandbox.graphql.data.ActorService
import io.bsamartins.sandbox.graphql.data.MovieCastRepository
import org.dataloader.DataLoader
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

    @DgsData(parentType = "Movie", field = "cast")
    fun movieActors(env: DataFetchingEnvironment): CompletableFuture<List<Cast>> {
        val dataLoader = DirectorsDataLoader.get(env)
        val movie = env.getSource<Movie>()!!
        return dataLoader.load(movie.id)
    }
}

@DgsDataLoader(name = "actors")
class DirectorsDataLoader(
    var actorService: ActorService,
    var movieCastRepository: MovieCastRepository,
) : MappedBatchLoader<String, List<Cast>> {

    companion object {
        fun get(env: DataFetchingEnvironment): DataLoader<String, List<Cast>> = env.getDataLoader("actors")!!
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

internal fun ActorData.toModel(): Actor =
    Actor(
        id = id,
        name = name,
    )

internal fun actorPartial(name: String): Actor =
    Actor(id = "", name = name)
