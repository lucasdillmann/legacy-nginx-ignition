package br.com.dillmann.nginxignition.core.nginx.command

import java.util.UUID

fun interface GetNginxHostLogsCommand {
    suspend fun getHostLogs(hostId: UUID, qualifier: String, lines: Int): List<String>
}
