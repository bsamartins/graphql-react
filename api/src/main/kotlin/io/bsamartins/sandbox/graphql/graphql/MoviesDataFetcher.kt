package io.bsamartins.sandbox.graphql.graphql

import Movies
import com.netflix.dgs.codegen.generated.types.Movie
import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsQuery

@DgsComponent
class MoviesDataFetcher {
    @DgsQuery
    fun movies(): List<Movie> = Movies.movies
}
