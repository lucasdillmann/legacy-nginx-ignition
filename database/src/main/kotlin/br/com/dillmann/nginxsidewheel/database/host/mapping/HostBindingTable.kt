package br.com.dillmann.nginxsidewheel.database.host.mapping

import org.jetbrains.exposed.sql.Table

internal object HostBindingTable: Table() {
    val id = uuid("id")
    val hostId = uuid("host_id") references HostTable.id
    val type = varchar("type", 64)
    val ip = varchar("ip", 256)
    val port = integer("port")
    val certificateId = uuid("certificate_id").nullable()

    override val primaryKey = PrimaryKey(id)
}
