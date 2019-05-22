package pl.net.gwynder.multitenency.support.script.execution.services

import pl.net.gwynder.multitenency.support.configuration.entities.ServerConfiguration
import pl.net.gwynder.multitenency.support.database.definition.query.ParsedQuery

interface ScriptExecutionConfiguration {

    val targetDatabases: Map<ServerConfiguration, List<String>>

    val parsedQueries: List<ParsedQuery>

    val errorBehavior: ScriptErrorBehavior

    val transactionBehavior: ScriptTransactionBehavior

}