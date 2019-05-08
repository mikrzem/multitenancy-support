package pl.net.gwynder.multitenency.support.stage.main

import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.scene.Parent
import javafx.scene.control.MenuItem
import javafx.scene.layout.VBox
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource
import org.springframework.stereotype.Component
import pl.net.gwynder.multitenency.support.configuration.entities.DatabaseServerConfiguration
import pl.net.gwynder.multitenency.support.configuration.services.ServerConfigurationEditContainer
import pl.net.gwynder.multitenency.support.utils.FxmlBuilder
import pl.net.gwynder.multitenency.support.utils.base.BaseController

@Component
class MainController(
        private val fxml: FxmlBuilder,
        @Value("classpath:fxml/configuration/list.fxml")
        private val configurationList: Resource,
        @Value("classpath:fxml/configuration/edit.fxml")
        private val configurationEdit: Resource,
        private val configurationContainer: ServerConfigurationEditContainer
) : BaseController(), MainNavigation {

    @FXML
    var viewAllConfigurations: MenuItem? = null

    @FXML
    var addNewConfigurations: MenuItem? = null

    @FXML
    var contentContainer: VBox? = null

    @FXML
    fun initialize() {
        viewAllConfigurations?.onAction = EventHandler { showAllConfigurations() }
        addNewConfigurations?.onAction = EventHandler { showNewConfiguration() }
        showAllConfigurations()
    }

    private fun show(component: Parent) {
        contentContainer?.children?.clear()
        contentContainer?.children?.add(
                component
        )
    }

    override fun showAllConfigurations() {
        show(fxml.component(configurationList))
    }

    override fun showNewConfiguration() {
        configurationContainer.createNew()
        show(fxml.component(configurationEdit))
    }

    override fun showEditConfiguration(configuration: DatabaseServerConfiguration) {
        configurationContainer.current = configuration
        show(fxml.component(configurationEdit))
    }
}