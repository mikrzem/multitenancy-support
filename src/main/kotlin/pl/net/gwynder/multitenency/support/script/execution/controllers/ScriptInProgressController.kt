package pl.net.gwynder.multitenency.support.script.execution.controllers

import javafx.fxml.FXML
import javafx.scene.control.Label
import javafx.scene.control.ProgressBar
import org.springframework.stereotype.Component
import pl.net.gwynder.multitenency.support.script.execution.entities.ScriptProgress
import pl.net.gwynder.multitenency.support.script.execution.services.ScriptExecutionContext
import pl.net.gwynder.multitenency.support.script.execution.services.ScriptExecutionNavigation
import pl.net.gwynder.multitenency.support.utils.base.BaseController

@Component
class ScriptInProgressController(
        private val context: ScriptExecutionContext,
        private val navigation: ScriptExecutionNavigation
) : BaseController() {

    @FXML
    var progressBar: ProgressBar? = null

    @FXML
    var total: Label? = null

    @FXML
    var success: Label? = null

    @FXML
    var skipped: Label? = null

    @FXML
    var failed: Label? = null

    @FXML
    fun initialize() {
        context.executionProgress.subscribe({ progress ->
            showProgress(progress)
        }, {
            showError()
        }, {
            showCompleted()
        })
    }

    private fun showProgress(progress: ScriptProgress) {
        progressBar?.progress = progress.finished.toDouble() / progress.successful.toDouble()
        total?.text = progress.total.toString()
        success?.text = progress.successful.toString()
        skipped?.text = progress.skipped.toString()
        failed?.text = progress.failed.toString()
    }

    private fun showError() {
        navigation.showResults()
    }

    private fun showCompleted() {
        navigation.showResults()
    }

}