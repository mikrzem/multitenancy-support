package pl.net.gwynder.multitenency.support.configuration.controllers

import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.TreeView
import org.springframework.stereotype.Component
import pl.net.gwynder.multitenency.support.configuration.entities.ConfigurationItem
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
    fun initialize() {

    }

}