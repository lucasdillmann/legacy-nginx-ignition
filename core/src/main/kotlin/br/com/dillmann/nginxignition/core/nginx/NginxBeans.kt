package br.com.dillmann.nginxignition.core.nginx

import br.com.dillmann.nginxignition.core.common.lifecycle.ShutdownCommand
import br.com.dillmann.nginxignition.core.common.lifecycle.StartupCommand
import br.com.dillmann.nginxignition.core.common.scheduler.ScheduledTask
import br.com.dillmann.nginxignition.core.nginx.command.*
import br.com.dillmann.nginxignition.core.nginx.configuration.NginxConfigurationFacade
import br.com.dillmann.nginxignition.core.nginx.configuration.NginxConfigurationFileProvider
import br.com.dillmann.nginxignition.core.nginx.configuration.provider.*
import br.com.dillmann.nginxignition.core.nginx.configuration.provider.MainConfigurationFileProvider
import br.com.dillmann.nginxignition.core.nginx.lifecycle.NginxShutdown
import br.com.dillmann.nginxignition.core.nginx.lifecycle.NginxStartup
import br.com.dillmann.nginxignition.core.nginx.log.NginxLogReader
import br.com.dillmann.nginxignition.core.nginx.log.NginxLogRotator
import br.com.dillmann.nginxignition.core.nginx.task.LogRotationScheduledTask
import org.koin.core.module.Module
import org.koin.dsl.bind
import org.koin.dsl.binds

internal fun Module.nginxBeans() {
    single { NginxProcessManager(get()) }
    single { NginxSemaphore() }
    single { NginxLogReader(get()) }
    single { NginxConfigurationFacade(get(), getAll(), get()) }
    single { NginxLogRotator(get(), get(), get(), get()) }
    single { NginxStartup(get()) } bind StartupCommand::class
    single { NginxShutdown(get()) } bind ShutdownCommand::class
    single { LogRotationScheduledTask(get(), get()) } bind ScheduledTask::class
    single { MainConfigurationFileProvider(get()) } bind NginxConfigurationFileProvider::class
    single { MimeTypesConfigurationFileProvider() } bind NginxConfigurationFileProvider::class
    single { HostConfigurationFileProvider(get(), get()) } bind NginxConfigurationFileProvider::class
    single { HostCertificateFileProvider(get(), get()) } bind NginxConfigurationFileProvider::class
    single { AccessListFileProvider(get()) } bind NginxConfigurationFileProvider::class
    single { HostRouteSourceCodeFileProvider() } bind NginxConfigurationFileProvider::class
    single { NginxService(get(), get(), get(), get(), get()) } binds arrayOf(
        ReloadNginxCommand::class,
        StartNginxCommand::class,
        StopNginxCommand::class,
        GetStatusNginxCommand::class,
        GetNginxHostLogsCommand::class,
        GetNginxMainLogsCommand::class,
    )
}
