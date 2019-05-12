package pl.net.gwynder.multitenency.support.configuration.services

import pl.net.gwynder.multitenency.support.configuration.entities.ServerConfiguration
import pl.net.gwynder.multitenency.support.configuration.entities.ServerConfigurationGroup
import pl.net.gwynder.multitenency.support.utils.database.BaseEntityRepository

interface ServerConfigurationGroupRepository : BaseEntityRepository<ServerConfigurationGroup> {

    fun findByConfiguration(configuration: ServerConfiguration): List<ServerConfigurationGroup>

}