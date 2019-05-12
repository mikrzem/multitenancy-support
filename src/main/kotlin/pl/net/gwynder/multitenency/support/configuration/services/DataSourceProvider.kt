package pl.net.gwynder.multitenency.support.configuration.services

import com.github.thomasnield.rxkotlinfx.subscribeOnFx
import io.reactivex.Single
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Service
import pl.net.gwynder.multitenency.support.configuration.entities.ServerConfiguration
import pl.net.gwynder.multitenency.support.configuration.entities.ServerType
import pl.net.gwynder.multitenency.support.utils.base.BaseComponent
import javax.sql.DataSource

@Service
class DataSourceProvider : BaseComponent() {

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

    private fun jdbcTemplate(
            type: ServerType,
            url: String,
            username: String,
            password: String
    ): JdbcTemplate {
        return JdbcTemplate(
                dataSource(type, url, username, password)
        )
    }

    fun testConnection(
            type: ServerType,
            url: String,
            username: String,
            password: String
    ): Single<Boolean> {
        return Single.create<Boolean> { emitter ->
            try {
                val jdbcTemplate = jdbcTemplate(type, url, username, password)
                jdbcTemplate.queryForObject("SELECT 1", Int::class.java)
                emitter.onSuccess(true)
            } catch (ex: Exception) {
                logger.error("Error connecting to database")
                emitter.onSuccess(false)
            }
        }.subscribeOnFx()
    }

    private val cache: MutableMap<Long, DataSource> = HashMap()

    fun dataSource(configuration: ServerConfiguration): DataSource {
        val found = cache[configuration.id]
        return if (found == null) {
            val created = dataSource(
                    configuration.serverType,
                    configuration.serverUrl,
                    configuration.serverUsername,
                    configuration.serverPassword
            )
            cache[configuration.id ?: 0] = created
            created
        } else {
            found
        }
    }

    fun clear(configuration: ServerConfiguration) {
        if (configuration.id != null) {
            cache.remove(configuration.id ?: 0)
        }
    }

}