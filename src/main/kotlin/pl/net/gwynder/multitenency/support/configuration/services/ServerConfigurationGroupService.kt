package pl.net.gwynder.multitenency.support.configuration.services

import org.springframework.stereotype.Service
import pl.net.gwynder.multitenency.support.configuration.entities.*
import pl.net.gwynder.multitenency.support.utils.base.BaseComponent

@Service
class ServerConfigurationGroupService(
        private val repository: ServerConfigurationGroupRepository,
        private val configurationService: ServerConfigurationService
) : BaseComponent() {

    fun selectAll(configuration: ServerConfiguration): List<ServerConfigurationGroup> {
        return repository.findByConfiguration(configuration)
    }

    fun selectTree(configuration: ServerConfiguration): ServerConfigurationItem {
        val result = ServerConfigurationItem(configuration)
        val all = selectAll(configuration)
        all.filter { group -> group.parent == null }
                .forEach { group ->
                    result.children.add(createGroupItem(result, group, all))
                }
        return result
    }

    fun selectTree(serverType: ServerType): RootConfigurationItem {
        val result = RootConfigurationItem()
        configurationService.select()
                .filter { configuration -> configuration.serverType == serverType }
                .forEach { configuration ->
                    result.children.add(selectTree(configuration))
                }
        return result
    }

    private fun createGroupItem(
            parent: ConfigurationItem,
            group: ServerConfigurationGroup,
            allGroups: List<ServerConfigurationGroup>
    ): GroupConfigurationItem {
        val result = GroupConfigurationItem(parent.server, group, parent)
        allGroups.filter { other -> other.parent?.id == group.id }
                .forEach { other ->
                    result.children.add(createGroupItem(result, other, allGroups))
                }
        group.databases.forEach { database ->
            result.children.add(createDatabaseItem(result, database))
        }
        return result
    }

    private fun createDatabaseItem(parent: GroupConfigurationItem, database: String): ConfigurationItem {
        return DatabaseConfigurationItem(
                parent.server,
                database,
                parent
        )
    }

    fun save(group: ServerConfigurationGroup): ServerConfigurationGroup {
        return repository.save(group)
    }

}