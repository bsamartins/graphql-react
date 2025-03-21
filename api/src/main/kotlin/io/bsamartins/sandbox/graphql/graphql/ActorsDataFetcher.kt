package io.bsamartins.sandbox.graphql.graphql

import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsQuery
import graphql.relay.Connection
import graphql.schema.DataFetchingEnvironment
import io.bsamartins.sandbox.graphql.codegen.types.Actor
import io.bsamartins.sandbox.graphql.data.ActorService

@DgsComponent
class ActorsDataFetcher(
    private val actorService: ActorService,
) {
    @DgsQuery
    fun actors(env: DataFetchingEnvironment): Connection<Actor> = actorService.listAll().asConnection(env)
}
