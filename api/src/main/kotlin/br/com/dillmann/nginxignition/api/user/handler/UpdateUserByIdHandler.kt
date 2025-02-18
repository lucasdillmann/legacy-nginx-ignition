package br.com.dillmann.nginxignition.api.user.handler

import br.com.dillmann.nginxignition.api.user.UserConverter
import br.com.dillmann.nginxignition.api.user.model.UserRequest
import br.com.dillmann.nginxignition.core.user.command.SaveUserCommand
import br.com.dillmann.nginxignition.api.common.request.ApiCall
import br.com.dillmann.nginxignition.api.common.request.HttpStatus
import br.com.dillmann.nginxignition.api.common.request.handler.UuidAwareRequestHandler
import br.com.dillmann.nginxignition.api.common.request.payload
import java.util.*

internal class UpdateUserByIdHandler(
    private val saveCommand: SaveUserCommand,
    private val converter: UserConverter,
): UuidAwareRequestHandler {
    override suspend fun handle(call: ApiCall, id: UUID) {
        val payload = call.payload<UserRequest>()
        val currentUser = call.principal()
        val user = converter.toDomainModel(payload).copy(id = id)
        saveCommand.save(user, currentUser?.userId)
        call.respond(HttpStatus.NO_CONTENT)
    }
}
