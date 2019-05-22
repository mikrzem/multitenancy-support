package pl.net.gwynder.multitenency.support.script.execution.controllers

import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.scene.control.Alert
import javafx.scene.control.Button
import javafx.scene.control.TextArea
import org.springframework.stereotype.Component
import pl.net.gwynder.multitenency.support.script.execution.services.ScriptExecutionContext
import pl.net.gwynder.multitenency.support.script.execution.services.ScriptExecutionNavigation
import pl.net.gwynder.multitenency.support.utils.base.BaseController

@Component
class ScriptQueryController(
        private val context: ScriptExecutionContext,
        private val parsedQueryComponent: ScriptParsedQueryController,
        private val navigation: ScriptExecutionNavigation
) : BaseController() {

    @FXML
    var queryText: TextArea? = null

    @FXML
    var parseQuery: Button? = null

    @FXML
    var acceptQuery: Button? = null

    @FXML
    var goBack: Button? = null

    @FXML
    fun initialize() {
        queryText?.text = context.query
        parseQuery?.onAction = EventHandler {
            saveQuery()
            parsedQueryComponent.onQueryChange()
        }
        acceptQuery?.onAction = EventHandler {
            saveQuery()
            if (context.parsedQueries.isEmpty()) {
                Alert(Alert.AlertType.WARNING, "There must be at least one valid query to execute").show()
            } else {
                navigation.showSummary()
            }
        }
        goBack?.onAction = EventHandler {
            navigation.showSelection()
        }
    }

    private fun saveQuery() {
        context.query = queryText?.text ?: ""
    }

}