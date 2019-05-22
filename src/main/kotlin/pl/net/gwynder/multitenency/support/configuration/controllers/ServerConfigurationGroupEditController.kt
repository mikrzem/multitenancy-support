package pl.net.gwynder.multitenency.support.configuration.controllers

import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.scene.control.*
import org.springframework.stereotype.Component
import pl.net.gwynder.multitenency.support.configuration.entities.ServerConfigurationGroup
import pl.net.gwynder.multitenency.support.configuration.services.ServerConfigurationEditContainer
import pl.net.gwynder.multitenency.support.configuration.services.ServerConfigurationGroupService
import pl.net.gwynder.multitenency.support.database.usage.ConfigurationReader
import pl.net.gwynder.multitenency.support.stage.main.MainNavigation
import pl.net.gwynder.multitenency.support.utils.base.BaseController

@Component
class ServerConfigurationGroupEditController(
        private val configuration: ServerConfigurationEditContainer,
        private val reader: ConfigurationReader,
        private val navigation: MainNavigation,
        private val service: ServerConfigurationGroupService
) : BaseController() {

    @FXML
    var serverName: Label? = null

    @FXML
    var parentName: Label? = null

    @FXML
    var name: TextField? = null

    @FXML
    var save: Button? = null

    @FXML
    var cancel: Button? = null

    @FXML
    var allDatabases: ListView<String>? = null

    @FXML
    var groupDatabases: ListView<String>? = null

    @FXML
    var addAllDatabases: Button? = null

    @FXML
    var addSelectedDatabases: Button? = null

    @FXML
    var removeSelectedDatabases: Button? = null

    @FXML
    var removeAllDatabases: Button? = null

    @FXML
    fun initialize() {
        save?.onAction = EventHandler {
            onSave()
        }
        cancel?.onAction = EventHandler {
            navigation.goBack()
        }
        allDatabases?.selectionModel?.selectionMode = SelectionMode.MULTIPLE
        groupDatabases?.selectionModel?.selectionMode = SelectionMode.MULTIPLE
        addAllDatabases?.onAction = EventHandler {
            val current = groupDatabases?.items?.toMutableSet() ?: HashSet()
            val all = allDatabases?.items?.toList() ?: ArrayList()
            current.addAll(all)
            groupDatabases?.items?.clear()
            groupDatabases?.items?.addAll(current.sorted())
        }
        addSelectedDatabases?.onAction = EventHandler {
            val current = groupDatabases?.items?.toMutableSet() ?: HashSet()
            val all = allDatabases?.selectionModel?.selectedItems?.toList() ?: ArrayList()
            current.addAll(all)
            groupDatabases?.items?.clear()
            groupDatabases?.items?.addAll(current.sorted())
        }
        removeAllDatabases?.onAction = EventHandler {
            groupDatabases?.items?.clear()
        }
        removeSelectedDatabases?.onAction = EventHandler {
            val current = groupDatabases?.items?.toMutableSet() ?: HashSet()
            val toRemove = groupDatabases?.selectionModel?.selectedItems?.toList() ?: ArrayList()
            current.removeAll(toRemove)
            groupDatabases?.items?.clear()
            groupDatabases?.items?.addAll(current.sorted())
        }
        loadFields()
    }

    private fun onSave() {
        val newName = name?.text ?: ""
        if (newName == "") {
            Alert(Alert.AlertType.WARNING, "Missing required field: Name", ButtonType.OK).show()
            return
        }
        val group = configuration.currentGroup
        group.configuration = configuration.current
        group.name = newName
        group.databases.clear()
        group.databases.addAll(groupDatabases?.items ?: listOf())
        configuration.currentGroup = service.save(group)
        navigation.goBack()
    }

    private fun loadFields() {
        val group = configuration.currentGroup
        serverName?.text = configuration.current.name
        parentName?.text = group.parent?.display() ?: "[root]"
        name?.text = group.name
        allDatabases?.items?.clear()
        allDatabases?.items?.addAll(reader.schema(configuration.current).selectDatabases().sorted())
        groupDatabases?.items?.clear()
        groupDatabases?.items?.addAll(group.databases.sorted())
    }
}