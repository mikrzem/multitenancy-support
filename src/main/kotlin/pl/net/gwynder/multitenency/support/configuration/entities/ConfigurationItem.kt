package pl.net.gwynder.multitenency.support.configuration.entities

enum class ConfigurationItemType {

    SERVER,
    GROUP,
    DATABASE

}

abstract class ConfigurationItem(
        val server: ServerConfiguration,
        val type: ConfigurationItemType,
        val parent: ConfigurationItem? = null
) {

    val children: MutableList<ConfigurationItem> = ArrayList()

    abstract fun allDatabases(): List<String>

    abstract fun display(): String

}

class ServerConfigurationItem(
        server: ServerConfiguration
) : ConfigurationItem(server, ConfigurationItemType.SERVER) {

    override fun allDatabases(): List<String> {
        return children.filterIsInstance(GroupConfigurationItem::class.java)
                .flatMap { group -> group.allDatabases() }
                .distinct()
    }

    override fun display(): String {
        return server.name
    }

}

class GroupConfigurationItem(
        server: ServerConfiguration,
        val group: ServerConfigurationGroup,
        parent: ConfigurationItem? = null
) : ConfigurationItem(server, ConfigurationItemType.GROUP, parent) {

    override fun allDatabases(): List<String> {
        val result = children.filterIsInstance(GroupConfigurationItem::class.java)
                .flatMap { child -> child.allDatabases() }
        return result.union(group.databases).distinct().toList()
    }

    override fun display(): String {
        return group.name
    }

}

class DatabaseConfigurationItem(
        server: ServerConfiguration,
        val database: String,
        parent: ConfigurationItem
) : ConfigurationItem(server, ConfigurationItemType.DATABASE, parent) {

    override fun allDatabases(): List<String> {
        return arrayListOf(database)
    }

    override fun display(): String {
        return database
    }

}