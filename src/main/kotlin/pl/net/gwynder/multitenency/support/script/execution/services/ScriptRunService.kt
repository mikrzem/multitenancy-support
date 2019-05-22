package pl.net.gwynder.multitenency.support.script.execution.services

import org.springframework.stereotype.Service
import pl.net.gwynder.multitenency.support.configuration.entities.ServerConfiguration
import pl.net.gwynder.multitenency.support.database.definition.query.ParsedQuery
import pl.net.gwynder.multitenency.support.script.execution.entities.*
import pl.net.gwynder.multitenency.support.utils.base.BaseComponent
import java.lang.RuntimeException
import java.time.LocalDateTime

@Service
class ScriptRunService(
        private val repository: ScriptRunRepository,
        private val resultRepository: ScriptResultRepository
) : BaseComponent() {

    fun newRun(targets: Map<ServerConfiguration, List<String>>, queries: List<ParsedQuery>): ScriptRun {
        val run = ScriptRun(
                parseTargets(targets),
                parseQueries(queries),
                LocalDateTime.now(),
                LocalDateTime.now(),
                finished = false,
                success = false
        )
        return repository.save(run)
    }

    private fun parseTargets(targets: Map<ServerConfiguration, List<String>>): MutableSet<ScriptTarget> {
        return targets.map { entry ->
            ScriptTarget(
                    entry.key.name,
                    entry.key.serverUrl,
                    entry.value.map { database -> ScriptTargetDatabase(database) }
                            .toMutableSet()
            )
        }.toMutableSet()
    }

    private fun parseQueries(queries: List<ParsedQuery>): MutableSet<ScriptQuery> {
        return queries.map { query -> ScriptQuery(query.query) }
                .toMutableSet()
    }

    fun startResult(configuration: ServerConfiguration, database: String, query: ParsedQuery, run: ScriptRun): ScriptResult {
        val result = ScriptResult(
                run,
                findTarget(run, configuration, database),
                findQuery(run, query),
                LocalDateTime.now(),
                LocalDateTime.now(),
                success = false
        )
        return resultRepository.save(result)
    }

    private fun findTarget(run: ScriptRun, configuration: ServerConfiguration, database: String): ScriptTargetDatabase {
        return run.targets.find { target -> target.name == configuration.name }
                ?.databases
                ?.find { targetDatabase -> targetDatabase.database == database }
                ?: throw RuntimeException("Unable to find saved target database")
    }

    private fun findQuery(run: ScriptRun, query: ParsedQuery): ScriptQuery {
        return run.queries.find { runQuery -> runQuery.query == query.query }
                ?: throw RuntimeException("Unable to find saved query")
    }

    fun finishResult(result: ScriptResult, success: Boolean, affectedRows: Int = 0): ScriptResult {
        result.endTime = LocalDateTime.now()
        result.success = success
        result.affectedRows = affectedRows
        return resultRepository.save(result)
    }

    fun finishRun(run: ScriptRun, success: Boolean): ScriptRun {
        run.finished = true
        run.endTime = LocalDateTime.now()
        run.success = success
        return repository.save(run)
    }

}