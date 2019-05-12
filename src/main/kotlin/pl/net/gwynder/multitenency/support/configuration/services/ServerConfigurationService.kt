package pl.net.gwynder.multitenency.support.configuration.services

import org.springframework.stereotype.Service
import pl.net.gwynder.multitenency.support.configuration.entities.ServerConfiguration
import pl.net.gwynder.multitenency.support.utils.base.BaseComponent

@Service
class ServerConfigurationService(
        private val repository: ServerConfigurationRepository,
        private val dataSourceProvider: DataSourceProvider
) : BaseComponent() {

    fun select(): List<ServerConfiguration> {
        return repository.findAll()
    }

    fun save(configuration: ServerConfiguration): ServerConfiguration {
        val saved = repository.save(configuration)
        dataSourceProvider.clear(saved)
        return saved
    }

    fun delete(configuration: ServerConfiguration) {
        repository.delete(configuration)
        dataSourceProvider.clear(configuration)
    }

}