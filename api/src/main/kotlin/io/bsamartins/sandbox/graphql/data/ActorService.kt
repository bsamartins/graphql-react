package io.bsamartins.sandbox.graphql.data

import org.springframework.stereotype.Service

@Service
class ActorService(
    private val actorRepository: ActorRepository,
) {
    fun listAll(): List<Actor> = actorRepository.findAll()

    fun findByName(name: String): Actor? = actorRepository.findByName(name)
}
