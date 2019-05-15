package pl.net.gwynder.multitenency.support.configuration.controllers

import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.scene.control.*
import javafx.util.StringConverter
import org.springframework.stereotype.Component
import pl.net.gwynder.multitenency.support.configuration.entities.ServerType
import pl.net.gwynder.multitenency.support.configuration.services.DataSourceProvider
import pl.net.gwynder.multitenency.support.configuration.services.ServerConfigurationEditContainer
import pl.net.gwynder.multitenency.support.configuration.services.ServerConfigurationService
import pl.net.gwynder.multitenency.support.configuration.services.ServerTypeStringConverter
import pl.net.gwynder.multitenency.support.stage.main.MainNavigation
import pl.net.gwynder.multitenency.support.utils.base.BaseController

@Component
class ServerConfigurationEditController(
        private val service: ServerConfigurationService,
        private val navigation: MainNavigation,
        private val configuration: ServerConfigurationEditContainer,
        private val dataSourceProvider: DataSourceProvider
) : BaseController() {

    @FXML
    var name: TextField? = null

    @FXML
    var type: ComboBox<ServerType>? = null

    @FXML
    var url: TextField? = null

    @FXML
    var username: TextField? = null

    @FXML
    var password: PasswordField? = null

    @FXML
    var save: Button? = null

    @FXML
    var cancel: Button? = null

    @FXML
    var testConnection: Button? = null

    @FXML
    var editGroups: Button? = null

    @FXML
    fun initialize() {
        type?.converter = ServerTypeStringConverter()
        type?.items?.clear()
        type?.items?.addAll(ServerType.values())
        cancel?.onAction = EventHandler { navigation.goBack() }
        save?.onAction = EventHandler {
            if (name?.text ?: "" == ""
                    || url?.text ?: "" == ""
                    || username?.text ?: "" == ""
                    || password?.text ?: "" == "") {
                Alert(Alert.AlertType.WARNING, "All values are required", ButtonType.OK).show()
            } else {
                val current = configuration.current
                current.name = name?.text ?: ""
                current.serverType = type?.value ?: ServerType.MYSQL
                current.serverUrl = url?.text ?: ""
                current.serverUsername = username?.text ?: ""
                current.serverPassword = password?.text ?: ""
                configuration.current = service.save(current)
                loadConfiguration()
                Alert(Alert.AlertType.INFORMATION, "Configuration saved", ButtonType.OK).show()
            }
        }
        testConnection?.onAction = EventHandler {
            dataSourceProvider.testConnection(
                    type?.value ?: ServerType.MYSQL,
                    url?.text ?: "",
                    username?.text ?: "",
                    password?.text ?: ""
            ).subscribe { success ->
                if (success) {
                    Alert(Alert.AlertType.INFORMATION, "Connection successful!", ButtonType.OK).show()
                } else {
                    Alert(Alert.AlertType.WARNING, "Connection failed!", ButtonType.OK).show()
                }
            }
        }
        editGroups?.onAction = EventHandler {
            navigation.showGroupEditTree(configuration.current)
        }
        loadConfiguration()
    }

    private fun loadConfiguration() {
        name?.text = configuration.current.name
        type?.value = configuration.current.serverType
        url?.text = configuration.current.serverUrl
        username?.text = configuration.current.serverUsername
        password?.text = configuration.current.serverPassword
        editGroups?.isVisible = configuration.current.id != null
    }

}