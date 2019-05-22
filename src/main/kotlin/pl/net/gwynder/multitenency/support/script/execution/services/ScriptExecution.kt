package pl.net.gwynder.multitenency.support.script.execution.services

import com.github.thomasnield.rxkotlinfx.subscribeOnFx
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import org.springframework.stereotype.Service
import pl.net.gwynder.multitenency.support.configuration.entities.ServerConfiguration
import pl.net.gwynder.multitenency.support.configuration.services.DataSourceContainer
import pl.net.gwynder.multitenency.support.configuration.services.DataSourceProvider
import pl.net.gwynder.multitenency.support.database.definition.query.ParsedQuery
import pl.net.gwynder.multitenency.support.script.execution.entities.ScriptProgress
import pl.net.gwynder.multitenency.support.script.execution.entities.ScriptRun
import pl.net.gwynder.multitenency.support.utils.base.BaseComponent
import java.lang.Exception
import java.lang.RuntimeException

@Service
class ScriptExecution(
        private val dataSourceProvider: DataSourceProvider,
        private val runService: ScriptRunService
) : BaseComponent() {

    fun execute(
            configuration: ScriptExecutionConfiguration,
            onRun: (run: ScriptRun) -> Unit
    ): Observable<ScriptProgress> {
        return Observable.create<ScriptProgress> { subscriber ->
            val target = configuration.targetDatabases
            val queries = configuration.parsedQueries
            val totalCount = queries.size * target.map { entry -> entry.value.size }.sum()
            subscriber.onNext(ScriptProgress(totalCount, 0, 0, 0))
            val run = runService.newRun(target, queries)
            onRun(run)
            val state = ScriptState(0, 0, 0)
            try {
                val success = executeQueries(target, queries, configuration, run) { progress ->
                    state.success += progress.success
                    state.skipped += progress.skipped
                    state.failed += progress.failed
                    subscriber.onNext(
                            ScriptProgress(
                                    totalCount,
                                    state.success,
                                    state.skipped,
                                    state.failed
                            )
                    )
                }
                onRun(runService.finishRun(run, success))
                subscriber.onComplete()
            } catch (ex: Exception) {
                runService.finishRun(run, false)
                subscriber.onError(ex)
            }
        }
                .observeOn(Schedulers.computation())
                .subscribeOnFx()
    }

    private fun executeQueries(
            target: Map<ServerConfiguration, List<String>>,
            queries: List<ParsedQuery>,
            configuration: ScriptExecutionConfiguration,
            run: ScriptRun,
            onProgress: (progress: ScriptContent) -> Unit
    ): Boolean {
        var success = true
        for ((index, server) in target.keys.withIndex()) {
            success = executeQueriesOnServer(
                    server,
                    target[server] ?: listOf(),
                    queries,
                    configuration,
                    run,
                    onProgress
            ) && success
            if (!success && configuration.errorBehavior == ScriptErrorBehavior.STOP_ALL) {
                onProgress(ScriptContent(0, queries.size * (target.keys.mapIndexed { targetIndex, targetServer ->
                    if (targetIndex < index) {
                        0
                    } else {
                        target[targetServer]?.size ?: 0
                    }
                }.sum()), 0))
                return false
            }
        }
        return success
    }

    private fun executeQueriesOnServer(
            server: ServerConfiguration,
            databases: List<String>,
            queries: List<ParsedQuery>,
            configuration: ScriptExecutionConfiguration,
            run: ScriptRun,
            onProgress: (progress: ScriptContent) -> Unit
    ): Boolean {
        var success = true
        for ((index, database) in databases.withIndex()) {
            val container = dataSourceProvider.container(server, database)
            success = executeQueriesOnDatabase(
                    container,
                    queries,
                    configuration,
                    run,
                    onProgress
            ) && success
            if (!success && configuration.errorBehavior == ScriptErrorBehavior.STOP_ALL) {
                onProgress(ScriptContent(0, queries.size * (databases.size - (index + 1)), 0))
                return false
            }
        }
        return success
    }

    private fun executeQueriesOnDatabase(
            container: DataSourceContainer,
            queries: List<ParsedQuery>,
            configuration: ScriptExecutionConfiguration,
            run: ScriptRun,
            onProgress: (progress: ScriptContent) -> Unit
    ): Boolean {
        return try {
            if (configuration.transactionBehavior == ScriptTransactionBehavior.NO_TRANSACTION) {
                runQueries(container, queries, configuration, run, onProgress)
            } else {
                container.inTransaction {
                    val result = runQueries(container, queries, configuration, run, onProgress)
                    if (configuration.transactionBehavior == ScriptTransactionBehavior.ROLLBACK_ALWAYS) {
                        throw ForcedRollback(result)
                    }
                    result
                } ?: false
            }
        } catch (rollback: ForcedRollback) {
            rollback.success
        } catch (ex: Exception) {
            logger.error("Error running queries", ex)
            false
        }
    }

    private fun runQueries(
            container: DataSourceContainer,
            queries: List<ParsedQuery>,
            configuration: ScriptExecutionConfiguration,
            run: ScriptRun,
            onProgress: (progress: ScriptContent) -> Unit
    ): Boolean {
        var success = true
        for ((index, query) in queries.withIndex()) {
            val result = runService.startResult(container.server, container.database ?: "[no-database]", query, run)
            try {
                val affectedRows = container.jdbc.update(query.query)
                runService.finishResult(result, true, affectedRows)
                onProgress(ScriptContent(1, 0, 0))
            } catch (ex: Exception) {
                success = false
                runService.finishResult(result, false)
                onProgress(ScriptContent(0, 0, 1))
                when (configuration.errorBehavior) {
                    ScriptErrorBehavior.STOP_ALL, ScriptErrorBehavior.STOP_DATABASE -> {
                        onProgress(ScriptContent(0, queries.size - (index + 1), 0))
                        throw ex
                    }
                    ScriptErrorBehavior.CONTINUE -> logger.error("Query failed", ex)
                }
            }
        }
        return success
    }

}

private class ScriptState(
        var success: Int,
        var skipped: Int,
        var failed: Int
)

private data class ScriptContent(
        val success: Int,
        val skipped: Int,
        val failed: Int
)

private class ForcedRollback(val success: Boolean) : RuntimeException("Forced rollback")