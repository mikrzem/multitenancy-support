package pl.net.gwynder.multitenency.support.database.definition.proxy

import javax.sql.DataSource

interface ProxyDataSourceProvider {

    fun proxyDataSource(dataSource: DataSource, database: String): DataSource

}