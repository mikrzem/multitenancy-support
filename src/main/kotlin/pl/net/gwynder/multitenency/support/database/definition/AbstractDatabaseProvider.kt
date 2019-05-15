package pl.net.gwynder.multitenency.support.database.definition

import pl.net.gwynder.multitenency.support.configuration.entities.ServerConfiguration
import pl.net.gwynder.multitenency.support.configuration.services.DataSourceProvider
import pl.net.gwynder.multitenency.support.database.definition.query.QueryParser
import pl.net.gwynder.multitenency.support.database.definition.schema.SchemaReader
import pl.net.gwynder.multitenency.support.utils.base.BaseComponent

abstract class AbstractDatabaseProvider(
        protected val dataSourceProvider: DataSourceProvider
) : BaseComponent() {

    abstract fun schema(configuration: ServerConfiguration): SchemaReader

    abstract fun queryParser(): QueryParser

}