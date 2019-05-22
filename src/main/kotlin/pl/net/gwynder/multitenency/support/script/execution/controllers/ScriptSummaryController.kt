package pl.net.gwynder.multitenency.support.script.execution.controllers

import javafx.beans.value.ChangeListener
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.ComboBox
import javafx.scene.control.TreeItem
import javafx.scene.control.TreeView
import javafx.util.StringConverter
import org.springframework.stereotype.Component
import pl.net.gwynder.multitenency.support.script.execution.services.ScriptErrorBehavior
import pl.net.gwynder.multitenency.support.script.execution.services.ScriptExecutionContext
import pl.net.gwynder.multitenency.support.script.execution.services.ScriptExecutionNavigation
import pl.net.gwynder.multitenency.support.script.execution.services.ScriptTransactionBehavior
import pl.net.gwynder.multitenency.support.utils.base.BaseController

@Component
class ScriptSummaryController(
        private val context: ScriptExecutionContext,
        private val navigation: ScriptExecutionNavigation
) : BaseController() {

    @FXML
    var selectedDatabases: TreeView<String>? = null

    @FXML
    var editSelection: Button? = null

    @FXML
    var editQuery: Button? = null

    @FXML
    var executeQuery: Button? = null

    @FXML
    var errorBehavior: ComboBox<ScriptErrorBehavior>? = null

    @FXML
    var transactionBehavior: ComboBox<ScriptTransactionBehavior>? = null

    @FXML
    fun initialize() {
        val targets = context.targetDatabases
        val root = TreeItem<String>()
        root.children.addAll(
                targets.map { entry ->
                    val server = TreeItem<String>(entry.key.name)
                    server.isExpanded = true
                    server.children.addAll(
                            entry.value.map { database -> TreeItem<String>(database) }
                    )
                    server
                }
        )
        errorBehavior?.items?.setAll(*ScriptErrorBehavior.values())
        errorBehavior?.converter = ScriptErrorBehaviorStringConverter()
        errorBehavior?.selectionModel?.select(context.errorBehavior)
        errorBehavior?.selectionModel?.selectedItemProperty()?.addListener { _, _, value ->
            context.errorBehavior = value
        }
        transactionBehavior?.items?.setAll(*ScriptTransactionBehavior.values())
        transactionBehavior?.converter = ScriptTransactionBehaviorStringConverter()
        transactionBehavior?.selectionModel?.select(context.transactionBehavior)
        transactionBehavior?.selectionModel?.selectedItemProperty()?.addListener { _, _, value ->
            context.transactionBehavior = value
        }
        selectedDatabases?.root = root
        editSelection?.onAction = EventHandler { navigation.showSelection() }
        editQuery?.onAction = EventHandler { navigation.showQuery() }
        executeQuery?.onAction = EventHandler {
            context.startExecution()
            navigation.showInProgress()
        }
    }

}

private class ScriptErrorBehaviorStringConverter : StringConverter<ScriptErrorBehavior>() {

    override fun toString(value: ScriptErrorBehavior?): String {
        return value?.description ?: ""
    }

    override fun fromString(string: String?): ScriptErrorBehavior {
        return ScriptErrorBehavior.values().find { behavior -> behavior.description == string }
                ?: throw RuntimeException("Invalid string: $string")
    }

}

private class ScriptTransactionBehaviorStringConverter : StringConverter<ScriptTransactionBehavior>() {

    override fun toString(value: ScriptTransactionBehavior?): String {
        return value?.description ?: ""
    }

    override fun fromString(string: String?): ScriptTransactionBehavior {
        return ScriptTransactionBehavior.values().find { behavior -> behavior.description == string }
                ?: throw RuntimeException("Invalid string: $string")
    }

}