package pl.net.gwynder.multitenency.support.configuration.controllers

import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.scene.control.*
import javafx.util.Callback
import org.kordamp.ikonli.Ikon
import org.kordamp.ikonli.fontawesome.FontAwesome
import org.kordamp.ikonli.javafx.FontIcon
import org.springframework.stereotype.Component
import pl.net.gwynder.multitenency.support.configuration.entities.ConfigurationItem
import pl.net.gwynder.multitenency.support.configuration.entities.ConfigurationItemType
import pl.net.gwynder.multitenency.support.configuration.entities.GroupConfigurationItem
import pl.net.gwynder.multitenency.support.configuration.entities.ServerConfigurationGroup
import pl.net.gwynder.multitenency.support.configuration.services.ServerConfigurationEditContainer
import pl.net.gwynder.multitenency.support.configuration.services.ServerConfigurationGroupService
import pl.net.gwynder.multitenency.support.stage.main.MainNavigation
import pl.net.gwynder.multitenency.support.utils.base.BaseComponent

@Component
class ServerConfigurationGroupEditTreeController(
        private val service: ServerConfigurationGroupService,
        private val navigation: MainNavigation,
        private val configuration: ServerConfigurationEditContainer
) : BaseComponent() {

    @FXML
    var editTree: TreeView<ConfigurationItem>? = null

    @FXML
    var newRootGroup: Button? = null

    @FXML
    var cancel: Button? = null

    @FXML
    fun initialize() {
        val server = service.selectTree(configuration.current)
        editTree?.isShowRoot = false
        editTree?.root = createTreeItem(server)
        editTree?.cellFactory = Callback<TreeView<ConfigurationItem>, TreeCell<ConfigurationItem>> {
            ConfigurationItemTreeCell(navigation, configuration)
        }
        newRootGroup?.onAction = EventHandler {
            navigation.showGroupEdit(configuration.current, ServerConfigurationGroup(configuration.current))
        }
        cancel?.onAction = EventHandler {
            navigation.goBack()
        }
    }

    private fun createTreeItem(item: ConfigurationItem): TreeItem<ConfigurationItem> {
        val result = TreeItem<ConfigurationItem>(item)
        result.children.addAll(
                item.children.map { child -> createTreeItem(child) }
        )
        result.isExpanded = true
        return result
    }

}

class ConfigurationItemTreeCell(
        private val navigation: MainNavigation,
        private val configuration: ServerConfigurationEditContainer
) : TreeCell<ConfigurationItem>() {

    override fun updateItem(item: ConfigurationItem?, empty: Boolean) {
        super.updateItem(item, empty)
        if (item != null) {
            text = item.display()
            graphic = when (item.type) {
                ConfigurationItemType.SERVER -> FontIcon.of(FontAwesome.SERVER)
                ConfigurationItemType.GROUP -> FontIcon.of(FontAwesome.FOLDER_OPEN_O)
                ConfigurationItemType.DATABASE -> FontIcon.of(FontAwesome.DATABASE)
                else -> null
            }
            contextMenu = when (item.type) {
                ConfigurationItemType.GROUP -> groupContextMenu()
                // TODO add database menu
                else -> null
            }
        }
    }

    private fun groupContextMenu(): ContextMenu {
        val menu = ContextMenu()
        menu.items.addAll(
                addChildGroup(),
                editGroupMenu(),
                removeGroupMenu()
        )
        return menu
    }

    private fun addChildGroup(): MenuItem {
        val menu = MenuItem("Add child group")
        menu.graphic = FontIcon.of(FontAwesome.PLUS_SQUARE_O)
        menu.onAction = EventHandler {
            val item = this.item
            if (item is GroupConfigurationItem) {
                navigation.showGroupEdit(
                        configuration.current,
                        ServerConfigurationGroup(configuration.current, item.group)
                )
            }
        }
        return menu
    }

    private fun editGroupMenu(): MenuItem {
        val menu = MenuItem("Edit group")
        menu.graphic = FontIcon.of(FontAwesome.EDIT)
        menu.onAction = EventHandler {
            val item = this.item
            if (item is GroupConfigurationItem) {
                navigation.showGroupEdit(configuration.current, item.group)
            }
        }
        return menu
    }

    private fun removeGroupMenu(): MenuItem {
        val menu = MenuItem("Remove group")
        menu.graphic = FontIcon.of(FontAwesome.TRASH)
        menu.onAction = EventHandler {
            // TODO
        }
        return menu
    }
}