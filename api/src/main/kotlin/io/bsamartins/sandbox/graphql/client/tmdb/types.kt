package io.bsamartins.sandbox.graphql.client.tmdb

import com.fasterxml.jackson.annotation.JsonProperty

data class ConfigurationResponse(
    @JsonProperty("images")
    val images: Images,
) {
    data class Images(
        @JsonProperty("base_url")
        val baseUrl: String,
        @JsonProperty("secure_base_url")
        val secureBaseUrl: String,
    )
}

data class PersonImagesResponse(
    val id: Int,
    val profiles: List<Profile>,
) {
    data class Profile(
        @JsonProperty("aspect_ratio")
        val aspectRatio: Long,
        val height: Int,
        val width: Int,
        @JsonProperty("iso_639_1")
        val iso6391: Int,
        @JsonProperty("file_path")
        val filePath: String,
        @JsonProperty("vote_average")
        val voteAverage: Long,
        @JsonProperty("vote_count")
        val voteCount: Long,
    )
}
