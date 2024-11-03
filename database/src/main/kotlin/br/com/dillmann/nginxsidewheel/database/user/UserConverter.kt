package br.com.dillmann.nginxsidewheel.database.user

import br.com.dillmann.nginxsidewheel.core.user.User
import br.com.dillmann.nginxsidewheel.database.user.mapping.UserTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.statements.UpsertStatement

internal class UserConverter {
    fun apply(user: User, scope: UpsertStatement<out Any>) {
        with(UserTable) {
            scope[id] = user.id
            scope[enabled] = user.enabled
            scope[name] = user.name
            scope[username] = user.username
            scope[role] = user.role.name

            if (user.passwordHash.isNotBlank())
                scope[passwordHash] = user.passwordHash
            if (user.passwordSalt.isNotBlank())
                scope[passwordSalt] = user.passwordSalt
        }
    }

    fun toUser(user: ResultRow) =
        with(UserTable) {
            User(
                id = user[id],
                enabled = user[enabled],
                name = user[name],
                username = user[username],
                role = user[role].let(User.Role::valueOf),
                passwordHash = user[passwordHash],
                passwordSalt = user[passwordSalt],
            )
        }
}
