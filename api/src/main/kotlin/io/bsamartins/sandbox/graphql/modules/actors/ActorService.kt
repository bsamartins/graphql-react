package io.bsamartins.sandbox.graphql.modules.actors

import org.springframework.data.domain.KeysetScrollPosition
import org.springframework.data.domain.Window
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class ActorService(
    private val actorRepository: ActorRepository,
) {
    fun listAll(): List<Actor> = actorRepository.findAll()

    fun findByName(name: String): Actor? = actorRepository.findByName(name)

    fun findById(id: Int): Actor? = actorRepository.findByIdOrNull(id)

    fun findWindow(position: KeysetScrollPosition): Window<Actor> = actorRepository.findFirst5By(position)
}
