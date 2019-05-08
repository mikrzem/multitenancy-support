package pl.net.gwynder.multitenency.support.configuration.controllers

import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.layout.VBox
import javafx.util.Callback
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource
import org.springframework.stereotype.Component
import pl.net.gwynder.multitenency.support.configuration.services.DatabaseServerConfigurationService
import pl.net.gwynder.multitenency.support.stage.main.MainNavigation
import pl.net.gwynder.multitenency.support.utils.FxmlBuilder
import pl.net.gwynder.multitenency.support.utils.base.BaseController

@Component
class ServerConfigurationListController(
        private val navigation: MainNavigation,
        private val service: DatabaseServerConfigurationService,
        private val fxml: FxmlBuilder,
        @Value("classpath:fxml/configuration/view.fxml")
        private val view: Resource
) : BaseController() {

    @FXML
    var listContainer: VBox? = null

    @FXML
    var addConfiguration: Button? = null

    @FXML
    fun initialize() {
        addConfiguration?.onAction = EventHandler { navigation.showNewConfiguration() }
        showList()
    }

    private fun showList() {
        listContainer?.children?.clear()
        listContainer?.children?.addAll(
                service.select().map { configuration ->
                    fxml.component(view, Callback {
                        ServerConfigurationViewController(
                                configuration,
                                service,
                                navigation
                        ) { showList() }
                    })
                }
        )
    }

}