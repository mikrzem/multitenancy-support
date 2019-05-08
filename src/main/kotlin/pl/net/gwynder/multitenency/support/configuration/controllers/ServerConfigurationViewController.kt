package pl.net.gwynder.multitenency.support.configuration.controllers

import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.scene.control.Alert
import javafx.scene.control.Button
import javafx.scene.control.Label
import pl.net.gwynder.multitenency.support.configuration.entities.DatabaseServerConfiguration
import pl.net.gwynder.multitenency.support.configuration.services.DatabaseServerConfigurationService
import pl.net.gwynder.multitenency.support.stage.main.MainNavigation
import pl.net.gwynder.multitenency.support.utils.base.BaseComponent
import javafx.scene.control.ButtonType

class ServerConfigurationViewController(
        private var configuration: DatabaseServerConfiguration,
        private val service: DatabaseServerConfigurationService,
        private val navigation: MainNavigation,
        private val onDelete: () -> Unit
) : BaseComponent() {

    @FXML
    var name: Label? = null

    @FXML
    var type: Label? = null

    @FXML
    var url: Label? = null

    @FXML
    var edit: Button? = null

    @FXML
    var remove: Button? = null

    @FXML
    fun initialize() {
        name?.text = configuration.name
        type?.text = configuration.serverType.display
        url?.text = configuration.serverUrl
        edit?.onAction = EventHandler { navigation.showEditConfiguration(configuration) }
        remove?.onAction = EventHandler {
            val dialog = Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to remove this configuration?")
            dialog.showAndWait().filter { button -> button == ButtonType.YES }.ifPresent {
                service.delete(configuration)
                onDelete()
            }
        }
    }

}