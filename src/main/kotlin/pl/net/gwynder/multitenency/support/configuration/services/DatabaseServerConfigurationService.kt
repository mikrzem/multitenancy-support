package pl.net.gwynder.multitenency.support.configuration.services

import org.springframework.stereotype.Service
import pl.net.gwynder.multitenency.support.configuration.entities.DatabaseServerConfiguration
import pl.net.gwynder.multitenency.support.utils.base.BaseComponent

@Service
class DatabaseServerConfigurationService(
        private val repository: DatabaseServerConfigurationRepository,
        private val dataSourceProvider: DataSourceProvider
) : BaseComponent() {

    fun select(): List<DatabaseServerConfiguration> {
        return repository.findAll()
    }

    fun save(configuration: DatabaseServerConfiguration): DatabaseServerConfiguration {
        val saved = repository.save(configuration)
        dataSourceProvider.clear(saved)
        return saved
    }

    fun delete(configuration: DatabaseServerConfiguration) {
        repository.delete(configuration)
        dataSourceProvider.clear(configuration)
    }

}