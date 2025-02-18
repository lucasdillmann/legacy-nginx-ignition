package br.com.dillmann.nginxignition.core.certificate

import br.com.dillmann.nginxignition.core.common.pagination.Page
import java.util.*

interface CertificateRepository {
    suspend fun findById(id: UUID): Certificate?
    suspend fun existsById(id: UUID): Boolean
    suspend fun deleteById(id: UUID)
    suspend fun save(certificate: Certificate)
    suspend fun findPage(pageSize: Int, pageNumber: Int, searchTerms: String?): Page<Certificate>
    suspend fun findAllDueToRenew(): List<Certificate>
}
