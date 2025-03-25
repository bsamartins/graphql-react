package io.bsamartins.sandbox.graphql.client.tmdb

import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.annotation.Cacheable
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestClient
import org.springframework.web.client.body

@Service
class DefaultTmdbClient(
    @Value("\${tmdb.api-key}") private val apiKey: String,
) : TmdbClient {
    private val logger = KotlinLogging.logger {}

    private val restClient = RestClient.builder()
        .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer $apiKey")
        .build()

    private val configuration by lazy {
        restClient.get()
            .uri(CONFIG_URL)
            .retrieve()
            .body<ConfigurationResponse>()!!
    }

    override fun movieImages(id: Int, languages: Set<String?>?): MovieImagesResponse? {
        return try {
            restClient.get()
                .uri(MOVIE_IMAGES_URL) { uri ->
                    uri.apply {
                        languages?.also {  queryParam("include_image_language", it) }
                    }.build(id)
                }
                .retrieve()
                .body()
        } catch (e: HttpClientErrorException.NotFound) {
            null
        }
    }

    @Cacheable("movie-posters")
    override fun moviePosters(id: Int, size: PosterSize): List<String>? {
        return movieImages(id, setOf("en"))
            ?.posters
            ?.map { poster -> buildImageUrl(size.size, poster.filePath) }
            ?: emptyList()
    }

    override fun personImages(id: Int): PersonImagesResponse? {
        return try {
            restClient.get()
                .uri(PERSON_IMAGES_URL, id)
                .retrieve()
                .body()!!
        } catch (e: HttpClientErrorException.NotFound) {
            null
        }
    }

    @Cacheable("person-profile-pictures")
    override fun personProfilePictures(id: Int, size: ProfilePictureSize): List<String>? {
        return personImages(id)
            ?.profiles
            ?.map { profile -> buildImageUrl(size.size, profile.filePath) }
    }

    private fun buildImageUrl(size: String, path: String): String =
        "${configuration.images.secureBaseUrl}$size$path"

    @PostConstruct
    fun init() {
        logger.info { configuration }
    }

    companion object {
        private const val BASE_URL = "https://api.themoviedb.org"
        private const val CONFIG_URL = "$BASE_URL/3/configuration"
        private const val MOVIE_IMAGES_URL = "$BASE_URL/3/movie/{movieId}/images"
        private const val PERSON_IMAGES_URL = "$BASE_URL/3/person/{personId}/images"
    }
}
