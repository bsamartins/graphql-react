package io.bsamartins.sandbox.graphql.graphql

import Actors
import com.netflix.dgs.codegen.generated.types.Actor
import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsQuery

@DgsComponent
class ActorsDataFetcher {
    @DgsQuery
    fun actors(): List<Actor> = Actors.actors
}
