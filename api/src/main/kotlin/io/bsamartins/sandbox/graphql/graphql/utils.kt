package io.bsamartins.sandbox.graphql.graphql

import graphql.relay.Connection
import graphql.relay.SimpleListConnection
import graphql.schema.DataFetchingEnvironment

fun <T> List<T>.asConnection(env: DataFetchingEnvironment): Connection<T> = SimpleListConnection(this).get(env)