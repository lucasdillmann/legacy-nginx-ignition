package br.com.dillmann.nginxignition.core.common.lifecycle

interface StartupCommand {
    private companion object {
        private const val DEFAULT_PRIORITY = 100
    }

    val priority: Int
        get() = DEFAULT_PRIORITY

    suspend fun execute()
}
