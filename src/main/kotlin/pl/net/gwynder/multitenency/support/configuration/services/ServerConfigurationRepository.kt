package pl.net.gwynder.multitenency.support.configuration.services

import pl.net.gwynder.multitenency.support.configuration.entities.ServerConfiguration
import pl.net.gwynder.multitenency.support.utils.database.BaseEntityRepository

interface ServerConfigurationRepository : BaseEntityRepository<ServerConfiguration> {
}