package pl.net.gwynder.multitenency.support.database.usage

import org.springframework.stereotype.Service
import pl.net.gwynder.multitenency.support.configuration.entities.ServerType
import pl.net.gwynder.multitenency.support.database.mysql.MySQLProxyDataSourceProvider
import javax.sql.DataSource

@Service
class UniversalProxyDataSourceProvider(
        private val mysql: MySQLProxyDataSourceProvider
) {

    fun proxyDataSource(type: ServerType, dataSource: DataSource, database: String): DataSource {
        return when (type) {
            ServerType.MYSQL -> mysql.proxyDataSource(dataSource, database)
            else -> throw RuntimeException("Unknown server type: ${type.display}")
        }
    }

}