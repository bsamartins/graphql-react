package io.bsamartins.sandbox.graphql.graphql

import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsQuery
import com.netflix.graphql.dgs.InputArgument


@DgsComponent
class HelloDataFetcher {
    @DgsQuery
    fun hello(@InputArgument message: String): String {
        return "Hello $message"
    }
}