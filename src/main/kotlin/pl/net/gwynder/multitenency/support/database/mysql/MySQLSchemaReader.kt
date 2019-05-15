package pl.net.gwynder.multitenency.support.database.mysql

import pl.net.gwynder.multitenency.support.configuration.services.DataSourceContainer
import pl.net.gwynder.multitenency.support.database.definition.schema.SchemaReader

class MySQLSchemaReader(
        private val database: DataSourceContainer
) : SchemaReader {

    override fun selectDatabases(): List<String> {
        return database.jdbc.queryForList("SHOW DATABASES", String::class.java)
    }
}