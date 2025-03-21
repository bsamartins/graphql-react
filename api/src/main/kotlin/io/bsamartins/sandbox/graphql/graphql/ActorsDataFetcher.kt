package io.bsamartins.sandbox.graphql.graphql

import com.netflix.dgs.codegen.generated.types.Actor
import com.netflix.dgs.codegen.generated.types.Movie
import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsQuery
import net.datafaker.Faker


@DgsComponent
class ActorsDataFetcher {

    private val faker = Faker()

    private val actors: List<Actor> =
        (1..1000).map {
            Actor(
                id = faker.idNumber().peselNumber(),
                name = faker.name().fullName(),
            )
        }

    @DgsQuery
    fun actors(): List<Actor> = actors
}