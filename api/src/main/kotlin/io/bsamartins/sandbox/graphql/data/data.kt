import com.netflix.dgs.codegen.generated.types.Actor
import com.netflix.dgs.codegen.generated.types.Movie
import net.datafaker.Faker

internal val faker = Faker()

object Movies {
    val movies: List<Movie> =
        (1..1000).map {
            Movie(
                id = faker.idNumber().peselNumber(),
                name = faker.movie().name(),
            )
        }
}

object Actors {
    val actors: List<Actor> =
        (1..1000).map {
            Actor(
                id = faker.idNumber().peselNumber(),
                name = faker.name().fullName(),
            )
        }
}
