package io.bsamartins.sandbox.graphql.graphql

import com.netflix.dgs.codegen.generated.types.Movie
import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsQuery
import net.datafaker.Faker


@DgsComponent
class MoviesDataFetcher {

    private val faker = Faker()

    private val movies: List<Movie> =
        (1..1000).map {
            Movie(
                id = faker.idNumber().peselNumber(),
                name = faker.movie().name(),
            )
        }

    @DgsQuery
    fun movies(): List<Movie> = movies
}