package pl.net.gwynder.multitenency.support.script.execution.controllers

import javafx.fxml.FXML
import javafx.scene.control.Label
import javafx.scene.control.ListCell
import javafx.scene.control.ListView
import javafx.util.Callback
import org.springframework.stereotype.Component
import pl.net.gwynder.multitenency.support.database.definition.query.ParsedQuery
import pl.net.gwynder.multitenency.support.script.execution.services.ScriptExecutionContext
import pl.net.gwynder.multitenency.support.utils.base.BaseController

@Component
class ScriptParsedQueryController(
        private val context: ScriptExecutionContext
) : BaseController() {

    @FXML
    var parsedQueries: ListView<ParsedQuery>? = null

    @FXML
    fun initialize() {
        parsedQueries?.cellFactory = Callback { ParsedQueryCell() }
        onQueryChange()
    }

    fun onQueryChange() {
        parsedQueries?.items?.setAll(context.parsedQueries)
    }

}

private class ParsedQueryCell : ListCell<ParsedQuery>() {

    override fun updateItem(item: ParsedQuery?, empty: Boolean) {
        super.updateItem(item, empty)
        text = if (empty || item == null) {
            ""
        } else {
            item.display
        }
    }

}