package pl.net.gwynder.multitenency.support.configuration.services

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import org.springframework.transaction.support.TransactionTemplate
import javax.sql.DataSource

class DataSourceContainer(
        dataSource: DataSource
) {

    private val transactionManager = DataSourceTransactionManager(dataSource)
    val jdbc = JdbcTemplate(transactionManager.dataSource!!)
    private val transaction = TransactionTemplate(transactionManager)

    fun inTransaction(callback: (jdbcTemplate: JdbcTemplate) -> Unit) {
        transaction.execute {
            callback(jdbc)
        }
    }


}