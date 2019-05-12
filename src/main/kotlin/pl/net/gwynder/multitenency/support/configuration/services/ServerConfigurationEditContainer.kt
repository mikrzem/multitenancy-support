package pl.net.gwynder.multitenency.support.configuration.services

import org.springframework.stereotype.Service
import pl.net.gwynder.multitenency.support.configuration.entities.ServerConfiguration
import pl.net.gwynder.multitenency.support.utils.base.BaseComponent

@Service
class ServerConfigurationEditContainer : BaseComponent() {

    var current: ServerConfiguration = ServerConfiguration()

    fun createNew() {
        current = ServerConfiguration()
    }

}