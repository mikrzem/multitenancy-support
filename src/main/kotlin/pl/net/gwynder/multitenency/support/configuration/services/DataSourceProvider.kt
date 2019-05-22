package pl.net.gwynder.multitenency.support.configuration.services

import com.github.thomasnield.rxkotlinfx.subscribeOnFx
import io.reactivex.Single
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Service
import pl.net.gwynder.multitenency.support.configuration.entities.ServerConfiguration
import pl.net.gwynder.multitenency.support.configuration.entities.ServerType
import pl.net.gwynder.multitenency.support.database.usage.UniversalProxyDataSourceProvider
import pl.net.gwynder.multitenency.support.utils.base.BaseComponent
import javax.sql.DataSource

@Service
class DataSourceProvider(
        private val proxyDataSourceProvider: UniversalProxyDataSourceProvider
) : BaseComponent() {

    private fun dataSource(
            type: ServerType,
            url: String,
            username: String,
            password: String
    ): DataSource {
        return DataSourceBuilder.create()
                .driverClassName(type.driver)
                .url(url)
                .username(username)
                .password(password)
                .build()
    }

    fun testConnection(
            type: ServerType,
            url: String,
            username: String,
            password: String
    ): Single<Boolean> {
        return Single.create<Boolean> { emitter ->
            try {
                val dataSource = dataSource(type, url, username, password)
                dataSource.connection.use { connection ->
                    val statement = connection.createStatement()
                    statement.executeQuery("SELECT 1")
                }
                emitter.onSuccess(true)
            } catch (ex: Exception) {
                logger.error("Error connecting to database")
                emitter.onSuccess(false)
            }
        }.subscribeOnFx()
    }

    private fun configurationId(configuration: ServerConfiguration) =
            configuration.id ?: 0

    private val cache: MutableMap<Long, DataSource> = HashMap()

    private fun dataSource(configuration: ServerConfiguration): DataSource {
        val configurationId = configurationId(configuration)
        val found = cache[configurationId]
        return if (found == null) {
            val created = dataSource(
                    configuration.serverType,
                    configuration.serverUrl,
                    configuration.serverUsername,
                    configuration.serverPassword
            )
            cache[configurationId] = created
            created
        } else {
            found
        }
    }

    fun container(configuration: ServerConfiguration): DataSourceContainer {
        return DataSourceContainer(dataSource(configuration), configuration)
    }

    fun container(configuration: ServerConfiguration, database: String): DataSourceContainer {
        return DataSourceContainer(
                proxyDataSourceProvider.proxyDataSource(
                        configuration.serverType,
                        dataSource(configuration),
                        database
                ),
                configuration,
                database
        )
    }

    fun clear(configuration: ServerConfiguration) {
        if (configuration.id != null) {
            val configurationId = configurationId(configuration)
            cache.remove(configurationId)
        }
    }

}