package pl.net.gwynder.multitenency.support.database.mysql

import org.springframework.stereotype.Service
import pl.net.gwynder.multitenency.support.configuration.entities.ServerConfiguration
import pl.net.gwynder.multitenency.support.configuration.services.DataSourceProvider
import pl.net.gwynder.multitenency.support.database.definition.AbstractDatabaseProvider
import pl.net.gwynder.multitenency.support.database.definition.query.QueryParser
import pl.net.gwynder.multitenency.support.database.definition.schema.SchemaReader

@Service
class MySQLProvider(
        dataSourceProvider: DataSourceProvider
) : AbstractDatabaseProvider(dataSourceProvider) {

    override fun schema(configuration: ServerConfiguration): SchemaReader {
        return MySQLSchemaReader(dataSourceProvider.container(configuration))
    }

    override fun queryParser(): QueryParser {
        return MySQLQueryParser()
    }

}