package pl.net.gwynder.multitenency.support.script.execution.services

import org.springframework.stereotype.Service
import pl.net.gwynder.multitenency.support.configuration.entities.ConfigurationItem
import pl.net.gwynder.multitenency.support.configuration.entities.ServerType

@Service
class ScriptExecutionContext {

    private var type: ServerType = ServerType.MYSQL

    var serverType: ServerType
        get() = type
        set(value) {
            type = value
            selectedDatabases.removeIf { item -> item.server.serverType != type }
        }

    val selectedDatabases: MutableList<ConfigurationItem> = ArrayList()

    fun reset() {
        selectedDatabases.clear()
    }

    fun isSelected(item: ConfigurationItem): Boolean {
        return when {
            selectedDatabases.contains(item) -> true
            item.parent != null -> isSelected(item.parent)
            else -> false
        }
    }

    fun executeOnDatabases(): List<String> {
        return selectedDatabases.flatMap { item -> item.allDatabases() }
                .distinct()
                .sorted()
    }

}