package pl.net.gwynder.multitenency.support.configuration.entities

enum class ConfigurationItemType {

    ROOT,
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

    override fun toString(): String {
        return display()
    }

}

class RootConfigurationItem : ConfigurationItem(ServerConfiguration(), ConfigurationItemType.ROOT) {

    override fun allDatabases(): List<String> {
        return ArrayList()
    }

    override fun display(): String {
        return "[root]"
    }

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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GroupConfigurationItem

        if (server != other.server) return false

        return true
    }

    override fun hashCode(): Int {
        return server.hashCode()
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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GroupConfigurationItem

        if (group != other.group) return false

        return true
    }

    override fun hashCode(): Int {
        return group.hashCode()
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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DatabaseConfigurationItem

        if (server != other.server) return false
        if (database != other.database) return false

        return true
    }

    override fun hashCode(): Int {
        return server.hashCode() + database.hashCode()
    }


}