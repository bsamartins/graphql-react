package io.bsamartins.sandbox.graphql.data

data class PageRequest(
    val first: Int?,
    val last: Int?,
    val before: String?,
    val after: String?,
)
