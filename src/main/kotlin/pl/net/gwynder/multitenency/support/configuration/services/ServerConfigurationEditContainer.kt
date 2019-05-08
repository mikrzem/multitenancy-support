package pl.net.gwynder.multitenency.support.configuration.services

import org.springframework.stereotype.Service
import pl.net.gwynder.multitenency.support.configuration.entities.DatabaseServerConfiguration
import pl.net.gwynder.multitenency.support.utils.base.BaseComponent

@Service
class ServerConfigurationEditContainer : BaseComponent() {

    var current: DatabaseServerConfiguration = DatabaseServerConfiguration()

    fun createNew() {
        current = DatabaseServerConfiguration()
    }

}