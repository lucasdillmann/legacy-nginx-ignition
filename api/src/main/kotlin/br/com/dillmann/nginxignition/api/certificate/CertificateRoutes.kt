package br.com.dillmann.nginxignition.api.certificate

import br.com.dillmann.nginxignition.api.common.routing.*
import br.com.dillmann.nginxignition.api.certificate.handler.*

internal class CertificateRoutes(
    private val listProvidersHandler: ListAvailableProvidersHandler,
    private val issueHandler: IssueCertificateHandler,
    private val listHandler: ListCertificatesHandler,
    private val deleteByIdHandler: DeleteCertificateByIdHandler,
    private val getByIdHandler: GetCertificateByIdHandler,
    private val renewByIdHandler: RenewCertificateByIdHandler,
): RouteProvider {
    override fun apiRoutes(): RouteNode =
        basePath("/api/certificates") {
            requireAuthentication {
                get(listHandler)
                post("/issue", issueHandler)
                get("/available-providers", listProvidersHandler)

                path("/{id}") {
                    get(getByIdHandler)
                    delete(deleteByIdHandler)
                    post("/renew", renewByIdHandler)
                }
            }
        }
}
