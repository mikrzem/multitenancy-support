package pl.net.gwynder.multitenency.support.configuration.entities

import java.lang.RuntimeException

enum class ServerType(
        val driver: String,
        val display: String
) {

    MYSQL("com.mysql.cj.jdbc.Driver", "MySQL")

}