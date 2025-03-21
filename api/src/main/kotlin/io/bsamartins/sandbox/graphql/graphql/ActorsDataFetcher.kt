package io.bsamartins.sandbox.graphql.graphql

import com.netflix.dgs.codegen.generated.types.Actor
import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsQuery
import io.bsamartins.sandbox.graphql.data.ActorService

@DgsComponent
class ActorsDataFetcher(
    private val actorService: ActorService,
) {
    @DgsQuery
    fun actors(): List<Actor> = actorService.listAll()
}
