package io.bsamartins.sandbox.graphql.client.tmdb

interface TmdbClient {
    fun movieImages(id: Int, languages: Set<String?>? = null): MovieImagesResponse?

    fun moviePosters(id: Int, size: PosterSize): List<String>?

    fun personImages(id: Int): PersonImagesResponse?

    fun personProfilePictures(id: Int, size: ProfilePictureSize): List<String>?
}