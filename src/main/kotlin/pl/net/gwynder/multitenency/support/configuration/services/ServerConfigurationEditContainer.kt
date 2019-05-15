package pl.net.gwynder.multitenency.support.configuration.services

import org.springframework.stereotype.Service
import pl.net.gwynder.multitenency.support.configuration.entities.ServerConfiguration
import pl.net.gwynder.multitenency.support.configuration.entities.ServerConfigurationGroup
import pl.net.gwynder.multitenency.support.utils.base.BaseComponent

@Service
class ServerConfigurationEditContainer : BaseComponent() {

    private var _current: ServerConfiguration = ServerConfiguration()

    var currentGroup: ServerConfigurationGroup = ServerConfigurationGroup()

    var current: ServerConfiguration
        get() = _current
        set(value) {
            _current = value
            if (currentGroup.configuration != _current) {
                currentGroup = ServerConfigurationGroup(_current)
            }
        }


    fun createNew() {
        current = ServerConfiguration()
    }

}