package pl.net.gwynder.multitenency.support.stage.main

import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.scene.Parent
import javafx.scene.control.MenuItem
import javafx.scene.layout.Priority
import javafx.scene.layout.VBox
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource
import org.springframework.stereotype.Component
import pl.net.gwynder.multitenency.support.configuration.entities.ServerConfiguration
import pl.net.gwynder.multitenency.support.configuration.entities.ServerConfigurationGroup
import pl.net.gwynder.multitenency.support.configuration.services.ServerConfigurationEditContainer
import pl.net.gwynder.multitenency.support.utils.FxmlBuilder
import pl.net.gwynder.multitenency.support.utils.base.BaseController
import java.util.*

@Component
class MainController(
        private val fxml: FxmlBuilder,
        @Value("classpath:fxml/configuration/list.fxml")
        private val configurationList: Resource,
        @Value("classpath:fxml/configuration/edit.fxml")
        private val configurationEdit: Resource,
        @Value("classpath:fxml/configuration/group/tree.fxml")
        private val groupTreeEdit: Resource,
        @Value("classpath:fxml/configuration/group/edit.fxml")
        private val groupEdit: Resource,
        @Value("classpath:fxml/script/execution/main.fxml")
        private val mainScriptExecution: Resource,
        private val configurationContainer: ServerConfigurationEditContainer
) : BaseController(), MainNavigation {

    private val navigationStack: Deque<Parent> = ArrayDeque()

    @FXML
    var viewAllConfigurations: MenuItem? = null

    @FXML
    var addNewConfigurations: MenuItem? = null

    @FXML
    var newScriptExecution: MenuItem? = null

    @FXML
    var contentContainer: VBox? = null

    @FXML
    fun initialize() {
        viewAllConfigurations?.onAction = EventHandler { showAllConfigurations() }
        addNewConfigurations?.onAction = EventHandler { showNewConfiguration() }
        newScriptExecution?.onAction = EventHandler { showScriptExecution() }
        showAllConfigurations()
    }

    private fun show(component: Parent) {
        navigationStack.push(component)
        VBox.setVgrow(component, Priority.ALWAYS)
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

    override fun showEditConfiguration(configuration: ServerConfiguration) {
        configurationContainer.current = configuration
        show(fxml.component(configurationEdit))
    }

    override fun showGroupEditTree(configuration: ServerConfiguration) {
        configurationContainer.current = configuration
        show(fxml.component(groupTreeEdit))
    }

    override fun showGroupEdit(configuration: ServerConfiguration, group: ServerConfigurationGroup) {
        configurationContainer.current = configuration
        configurationContainer.currentGroup = group
        show(fxml.component(groupEdit))
    }

    override fun showScriptExecution() {
        show(fxml.component(mainScriptExecution))
    }

    override fun goBack() {
        if (!navigationStack.isEmpty()) {
            navigationStack.removeFirst()
            val previous = navigationStack.pop()
            show(previous)
        }
    }
}