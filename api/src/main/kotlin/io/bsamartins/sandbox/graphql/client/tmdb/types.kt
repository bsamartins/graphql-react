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
        @JsonProperty("poster_sizes")
        val posterSizes: Set<String>,
        @JsonProperty("profile_sizes")
        val profileSizes: Set<String>,
    )
}

data class MovieImagesResponse(
    val id: Int,
    val backdrops: List<Image>,
    val logos: List<Image>,
    val posters: List<Image>,
)

data class PersonImagesResponse(
    val id: Int,
    val profiles: List<Image>,
)

data class Image(
    @JsonProperty("aspect_ratio")
    val aspectRatio: Long,
    val height: Int,
    val width: Int,
    @JsonProperty("iso_639_1")
    val iso6391: String?,
    @JsonProperty("file_path")
    val filePath: String,
    @JsonProperty("vote_average")
    val voteAverage: Long,
    @JsonProperty("vote_count")
    val voteCount: Long,
)

enum class ProfilePictureSize(
    val size: String
) {
    SMALL("w45"),
    MEDIUM("w185"),
    LARGE("h632"),
    ORIGINAL("original")
}

enum class PosterSize(
    val size: String
) {
    XS("w92"),
    S("w154"),
    M("w185"),
    L("w342"),
    XL("w500"),
    XXL("w780"),
    ORIGINAL("original")
}
