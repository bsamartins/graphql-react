package io.bsamartins.sandbox.graphql.data

import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.persistence.EntityManager
import org.hibernate.search.mapper.orm.Search
import org.springframework.boot.actuate.endpoint.annotation.WriteOperation
import org.springframework.boot.actuate.endpoint.web.annotation.WebEndpoint
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.util.concurrent.atomic.AtomicBoolean


@Component
@WebEndpoint(id = "data-index")
class DataIndexer(
    private val entityManager: EntityManager,
) {
    private val logger = KotlinLogging.logger {}
    private val loadInProgress = AtomicBoolean(false)

    @Transactional
    @WriteOperation
    fun index() {
        if (!loadInProgress.get()) {
            loadInProgress.set(true)
            try {
                load()
            } finally {
                loadInProgress.set(false)
            }
        } else {
            logger.info { "Load in progress" }
        }
    }

    private fun load() {
        logger.info { "Indexing entities" }
        Search.session(entityManager)
            .massIndexer(Movie::class.java)
            .purgeAllOnStart(true)
            .threadsToLoadObjects(7)
            .startAndWait()
    }
}