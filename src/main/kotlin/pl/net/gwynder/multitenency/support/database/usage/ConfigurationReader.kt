package pl.net.gwynder.multitenency.support.database.usage

import org.springframework.stereotype.Service
import pl.net.gwynder.multitenency.support.configuration.entities.ServerConfiguration
import pl.net.gwynder.multitenency.support.configuration.entities.ServerType
import pl.net.gwynder.multitenency.support.database.definition.AbstractDatabaseProvider
import pl.net.gwynder.multitenency.support.database.definition.query.QueryParser
import pl.net.gwynder.multitenency.support.database.definition.schema.SchemaReader
import pl.net.gwynder.multitenency.support.database.mysql.MySQLProvider
import java.lang.RuntimeException

@Service
class ConfigurationReader(
        mysql: MySQLProvider
) {

    private val providers: Map<ServerType, AbstractDatabaseProvider> = mapOf(
            Pair(ServerType.MYSQL, mysql)
    )

    private fun provider(configuration: ServerConfiguration): AbstractDatabaseProvider {
        return provider(configuration.serverType)
    }

    private fun provider(type: ServerType): AbstractDatabaseProvider {
        return providers[type]
                ?: throw RuntimeException("Missing database provider for type: ${type.display}")
    }

    fun schema(configuration: ServerConfiguration): SchemaReader {
        return provider(configuration).schema(configuration)
    }

    fun queryParser(type: ServerType): QueryParser {
        return provider(type).queryParser()
    }

}