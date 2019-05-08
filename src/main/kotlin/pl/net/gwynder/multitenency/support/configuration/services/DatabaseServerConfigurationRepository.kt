package pl.net.gwynder.multitenency.support.configuration.services

import pl.net.gwynder.multitenency.support.configuration.entities.DatabaseServerConfiguration
import pl.net.gwynder.multitenency.support.utils.database.BaseEntityRepository

interface DatabaseServerConfigurationRepository : BaseEntityRepository<DatabaseServerConfiguration> {
}