package pl.net.gwynder.multitenency.support.configuration.services

import javafx.util.StringConverter
import pl.net.gwynder.multitenency.support.configuration.entities.ServerType

class ServerTypeStringConverter : StringConverter<ServerType>() {
    override fun toString(value: ServerType?): String {
        return value?.display ?: ""
    }

    override fun fromString(string: String?): ServerType {
        if (string == null || string == "") {
            throw RuntimeException("Missing server type")
        } else {
            return ServerType.values().find { type -> type.display == string }
                    ?: throw RuntimeException("Unknown server type: $string")
        }
    }

}