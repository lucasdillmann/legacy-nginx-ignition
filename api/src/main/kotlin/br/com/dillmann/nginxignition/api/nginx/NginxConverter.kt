package br.com.dillmann.nginxignition.api.nginx

import br.com.dillmann.nginxignition.api.nginx.model.NginxActionErrorResponse
import br.com.dillmann.nginxignition.core.nginx.exception.NginxCommandException
import org.mapstruct.Mapper
import org.mapstruct.ReportingPolicy

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
internal fun interface NginxConverter {
    fun toResponse(exception: NginxCommandException): NginxActionErrorResponse
}
