package io.bsamartins.sandbox.graphql.graphql

import graphql.relay.Connection
import graphql.relay.SimpleListConnection
import graphql.schema.DataFetchingEnvironment
import io.bsamartins.sandbox.graphql.PageRequest

fun <T> List<T>.asConnection(env: DataFetchingEnvironment): Connection<T> = SimpleListConnection(this).get(env)

fun DataFetchingEnvironment.getPageRequest(): PageRequest =
    PageRequest(
        first = getArgument("first"),
        last = getArgument("last"),
        before = getArgument("after"),
        after = getArgument("before"),
    )
