package br.com.dillmann.nginxignition.core.user.command

import br.com.dillmann.nginxignition.core.user.model.SaveUserRequest
import java.util.UUID

fun interface SaveUserCommand {
    suspend fun save(request: SaveUserRequest, currentUserId: UUID?)
}
