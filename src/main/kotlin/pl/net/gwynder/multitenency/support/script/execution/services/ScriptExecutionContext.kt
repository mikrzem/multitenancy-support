package pl.net.gwynder.multitenency.support.script.execution.services

import io.reactivex.Observable
import org.springframework.stereotype.Service
import pl.net.gwynder.multitenency.support.configuration.entities.ConfigurationItem
import pl.net.gwynder.multitenency.support.configuration.entities.ServerConfiguration
import pl.net.gwynder.multitenency.support.configuration.entities.ServerType
import pl.net.gwynder.multitenency.support.database.definition.query.ParsedQuery
import pl.net.gwynder.multitenency.support.database.definition.query.QueryParserOptions
import pl.net.gwynder.multitenency.support.database.usage.ConfigurationReader
import pl.net.gwynder.multitenency.support.script.execution.entities.ScriptProgress
import pl.net.gwynder.multitenency.support.script.execution.entities.ScriptRun
import pl.net.gwynder.multitenency.support.utils.base.BaseComponent

@Service
class ScriptExecutionContext(
        private val databaseReader: ConfigurationReader,
        private val execution: ScriptExecution
) : BaseComponent(), ScriptExecutionConfiguration {

    private var type: ServerType = ServerType.MYSQL

    var serverType: ServerType
        get() = type
        set(value) {
            type = value
            selectedDatabases.removeIf { item -> item.server.serverType != type }
        }

    val selectedDatabases: MutableList<ConfigurationItem> = ArrayList()

    val queryOptions: QueryParserOptions = QueryParserOptions()

    private var queryText: String = ""

    var query: String
        get() = queryText
        set(value) {
            queryText = value
            parsedQueriesList = databaseReader.queryParser(type).parseQuery(queryText, queryOptions)
        }

    private var parsedQueriesList: List<ParsedQuery> = listOf()

    override val parsedQueries: List<ParsedQuery>
        get() = parsedQueriesList

    override var errorBehavior: ScriptErrorBehavior = ScriptErrorBehavior.STOP_DATABASE

    override var transactionBehavior: ScriptTransactionBehavior = ScriptTransactionBehavior.NO_TRANSACTION

    fun reset() {
        selectedDatabases.clear()
        query = ""
    }

    fun isSelected(item: ConfigurationItem): Boolean {
        return when {
            selectedDatabases.contains(item) -> true
            item.parent != null -> isSelected(item.parent)
            else -> false
        }
    }

    override val targetDatabases: Map<ServerConfiguration, List<String>>
        get() {
            return selectedDatabases.groupBy { item -> item.server }
                    .mapValues { entry ->
                        entry.value.flatMap { item -> item.allDatabases() }
                                .distinct()
                                .sorted()
                    }
        }

    var currentRun: ScriptRun? = null

    var executionProgress: Observable<ScriptProgress> = Observable.empty()

    fun startExecution() {
        executionProgress = execution.execute(this) { run -> currentRun = run }
    }
}