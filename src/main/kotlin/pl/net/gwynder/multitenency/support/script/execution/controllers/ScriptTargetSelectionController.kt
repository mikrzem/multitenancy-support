package pl.net.gwynder.multitenency.support.script.execution.controllers

import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.scene.control.*
import javafx.scene.control.cell.CheckBoxTreeCell
import org.springframework.stereotype.Component
import pl.net.gwynder.multitenency.support.configuration.entities.ConfigurationItem
import pl.net.gwynder.multitenency.support.configuration.entities.ServerType
import pl.net.gwynder.multitenency.support.configuration.services.ServerConfigurationGroupService
import pl.net.gwynder.multitenency.support.configuration.services.ServerTypeStringConverter
import pl.net.gwynder.multitenency.support.script.execution.services.ScriptExecutionContext
import pl.net.gwynder.multitenency.support.script.execution.services.ScriptExecutionNavigation
import pl.net.gwynder.multitenency.support.utils.base.BaseController

@Component
class ScriptTargetSelectionController(
        private val context: ScriptExecutionContext,
        private val service: ServerConfigurationGroupService,
        private val navigation: ScriptExecutionNavigation
) : BaseController() {

    @FXML
    var serverType: ComboBox<ServerType>? = null

    @FXML
    var selectionTree: TreeView<ConfigurationItem>? = null

    @FXML
    var confirm: Button? = null

    var rootCheckbox: CheckBoxTreeItem<ConfigurationItem> = CheckBoxTreeItem()

    @FXML
    fun initialize() {
        serverType?.converter = ServerTypeStringConverter()
        serverType?.items?.setAll(*ServerType.values())
        serverType?.value = context.serverType
        serverType?.valueProperty()?.addListener { _, _, type ->
            context.serverType = type
            loadTree()
        }
        selectionTree?.isShowRoot = false
        selectionTree?.cellFactory = CheckBoxTreeCell.forTreeView()
        confirm?.onAction = EventHandler { onConfirm() }
        loadTree()
    }

    private fun loadTree() {
        rootCheckbox = buildCheckboxes(service.selectTree(context.serverType))
        selectionTree?.root = rootCheckbox
    }

    private fun buildCheckboxes(item: ConfigurationItem): CheckBoxTreeItem<ConfigurationItem> {
        val result = CheckBoxTreeItem<ConfigurationItem>(item)
        result.isSelected = context.isSelected(item)
        result.isExpanded = true
        result.children.setAll(item.children.map { child -> buildCheckboxes(child) })
        return result
    }

    private fun onConfirm() {
        val foundChecked = HashSet<ConfigurationItem>()
        findChecked(rootCheckbox, foundChecked)
        context.selectedDatabases.clear()
        context.selectedDatabases.addAll(foundChecked)
        if (context.executeOnDatabases().isEmpty()) {
            Alert(Alert.AlertType.WARNING, "At least one database must be selected")
            return
        }
        navigation.showQuery()
    }

    private fun findChecked(checkbox: CheckBoxTreeItem<ConfigurationItem>, found: HashSet<ConfigurationItem>) {
        if (checkbox.isSelected) {
            found.add(checkbox.value)
        } else {
            for (child in checkbox.children) {
                if (child is CheckBoxTreeItem<ConfigurationItem>) {
                    findChecked(child, found)
                }
            }
        }
    }

}