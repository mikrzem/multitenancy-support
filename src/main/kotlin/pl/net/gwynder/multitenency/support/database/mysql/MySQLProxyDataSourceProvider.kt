package pl.net.gwynder.multitenency.support.database.mysql

import org.springframework.jdbc.datasource.DelegatingDataSource
import org.springframework.stereotype.Service
import pl.net.gwynder.multitenency.support.database.definition.proxy.ProxyDataSourceProvider
import java.sql.Connection
import javax.sql.DataSource

@Service
class MySQLProxyDataSourceProvider : ProxyDataSourceProvider {

    override fun proxyDataSource(dataSource: DataSource, database: String): DataSource {
        return MySQLDatabaseDataSource(dataSource, database)
    }

}

private class MySQLDatabaseDataSource(
        dataSource: DataSource,
        private val database: String
) : DelegatingDataSource(dataSource) {
    override fun getConnection(): Connection {
        val connection = super.getConnection()
        connection.createStatement().execute("USE $database")
        return connection
    }
}