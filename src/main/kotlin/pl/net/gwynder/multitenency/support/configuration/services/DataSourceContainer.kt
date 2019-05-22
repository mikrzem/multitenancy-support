package pl.net.gwynder.multitenency.support.configuration.services

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import org.springframework.transaction.support.TransactionTemplate
import pl.net.gwynder.multitenency.support.configuration.entities.ServerConfiguration
import javax.sql.DataSource

class DataSourceContainer(
        dataSource: DataSource,
        val server: ServerConfiguration,
        val database: String? = null
) {

    private val transactionManager = DataSourceTransactionManager(dataSource)
    val jdbc = JdbcTemplate(transactionManager.dataSource!!)
    private val transaction = TransactionTemplate(transactionManager)

    fun <T> inTransaction(callback: (jdbcTemplate: JdbcTemplate) -> T): T? {
        return transaction.execute {
            callback(jdbc)
        }
    }


}