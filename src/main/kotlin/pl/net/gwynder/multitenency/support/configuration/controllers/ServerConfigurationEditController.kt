package pl.net.gwynder.multitenency.support.configuration.controllers

import com.zaxxer.hikari.hibernate.HikariConfigurationUtil.loadConfiguration
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.scene.control.*
import javafx.util.StringConverter
import org.springframework.stereotype.Component
import pl.net.gwynder.multitenency.support.configuration.entities.ServerType
import pl.net.gwynder.multitenency.support.configuration.services.DataSourceProvider
import pl.net.gwynder.multitenency.support.configuration.services.DatabaseServerConfigurationService
import pl.net.gwynder.multitenency.support.configuration.services.ServerConfigurationEditContainer
import pl.net.gwynder.multitenency.support.stage.main.MainNavigation
import pl.net.gwynder.multitenency.support.utils.base.BaseController
import java.lang.RuntimeException

@Component
class ServerConfigurationEditController(
        private val service: DatabaseServerConfigurationService,
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
    fun initialize() {
        type?.converter = ServerTypeStringConverter()
        type?.items?.clear()
        type?.items?.addAll(ServerType.values())
        cancel?.onAction = EventHandler { navigation.showAllConfigurations() }
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
        loadConfiguration()
    }

    private fun loadConfiguration() {
        name?.text = configuration.current.name
        type?.value = configuration.current.serverType
        url?.text = configuration.current.serverUrl
        username?.text = configuration.current.serverUsername
        password?.text = configuration.current.serverPassword
    }

}

private class ServerTypeStringConverter : StringConverter<ServerType>() {
    override fun toString(value: ServerType?): String {
        return value?.display ?: ""
    }

    override fun fromString(string: String?): ServerType {
        if (string == null || string == "") {
            throw RuntimeException("Missing server type")
        } else {
            return ServerType.values().find { type -> type.display == string }
                    ?: throw RuntimeException("Unknown server type: $string")
        }
    }

}