package pl.net.gwynder.multitenency.support.script.execution.controllers

import javafx.fxml.FXML
import javafx.scene.Parent
import javafx.scene.layout.Priority
import javafx.scene.layout.VBox
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource
import org.springframework.stereotype.Component
import pl.net.gwynder.multitenency.support.script.execution.services.ScriptExecutionContext
import pl.net.gwynder.multitenency.support.script.execution.services.ScriptExecutionNavigation
import pl.net.gwynder.multitenency.support.utils.FxmlBuilder
import pl.net.gwynder.multitenency.support.utils.base.BaseController

@Component
class ScriptExecutingController(
        private val context: ScriptExecutionContext,
        private val fxml: FxmlBuilder,
        @Value("classpath:fxml/script/execution/selection.fxml")
        private val selection: Resource,
        @Value("classpath:fxml/script/execution/query.fxml")
        private val query: Resource,
        @Value("classpath:fxml/script/execution/summary.fxml")
        private val summary: Resource,
        @Value("classpath:fxml/script/execution/progress.fxml")
        private val progress: Resource
) : BaseController(), ScriptExecutionNavigation {

    @FXML
    var stepContainer: VBox? = null

    @FXML
    fun initialize() {
        context.reset()
        showSelection()
    }

    private fun show(component: Parent) {
        VBox.setVgrow(component, Priority.ALWAYS)
        stepContainer?.children?.clear()
        stepContainer?.children?.add(component)
    }

    override fun showSelection() {
        show(fxml.component(selection))
    }

    override fun showQuery() {
        show(fxml.component(query))
    }

    override fun showSummary() {
        show(fxml.component(summary))
    }

    override fun showInProgress() {
        show(fxml.component(progress))
    }

    override fun showResults() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}