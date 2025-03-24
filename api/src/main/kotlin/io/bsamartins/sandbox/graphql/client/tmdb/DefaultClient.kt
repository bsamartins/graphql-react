package io.bsamartins.sandbox.graphql.client.tmdb

import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient
import org.springframework.web.client.body

@Service
class DefaultClient(
    @Value("\${tmdb.api-key}") private val apiKey: String,
) {
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

    fun personImages(id: Int): PersonImagesResponse {
        return restClient.get()
            .uri(PERSON_IMAGES_URL, id)
            .retrieve()
            .body<PersonImagesResponse>()!!
    }

    fun personProfilePictures(id: Int, size: ProfilePictureSize): List<String> {
        return personImages(id)
            .profiles
            .map { profile ->
                "${configuration.images.secureBaseUrl}/${size.size}/${profile.filePath}"
            }
    }

    @PostConstruct
    fun init() {
        logger.info { configuration }
    }

    companion object {
        private const val BASE_URL = "https://api.themoviedb.org"
        private const val CONFIG_URL = "$BASE_URL/3/configuration"
        private const val PERSON_IMAGES_URL = "$BASE_URL/3/person/{personId}/images"
    }
}

enum class ProfilePictureSize(
    val size: String
) {
    SMALL("w45"),
    MEDIUM("w185"),
    LARGE("h632"),
    ORIGINAL("original")
}
