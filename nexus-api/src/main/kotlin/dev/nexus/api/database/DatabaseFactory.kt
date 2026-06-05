package dev.nexus.api.database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database
import org.slf4j.LoggerFactory

object DatabaseFactory {

    private val log = LoggerFactory.getLogger(DatabaseFactory::class.java)

    fun init(config: DatabaseConfig) {
        val dataSource = buildDataSource(config)
        runMigrations(dataSource, config)
        Database.connect(dataSource)
        log.info("Database connected — pool size: ${config.maxPoolSize}")
    }

    private fun buildDataSource(config: DatabaseConfig) = HikariDataSource(
        HikariConfig().apply {
            jdbcUrl = config.url
            username = config.user
            password = config.password
            driverClassName = "org.postgresql.Driver"
            maximumPoolSize = config.maxPoolSize
            minimumIdle = 2
            idleTimeout = 600_000
            connectionTimeout = 30_000
            validationTimeout = 5_000
            poolName = "nexus-api-pool"
        }
    )

    private fun runMigrations(dataSource: HikariDataSource, config: DatabaseConfig) {
        log.info("Running Flyway migrations…")
        Flyway.configure()
            .dataSource(dataSource)
            .locations("classpath:db/migration")
            .baselineOnMigrate(config.baselineOnMigrate)
            .load()
            .migrate()
            .also { result ->
                log.info("Migrations applied: ${result.migrationsExecuted}")
            }
    }
}

data class DatabaseConfig(
    val url: String,
    val user: String,
    val password: String,
    val maxPoolSize: Int = 10,
    val baselineOnMigrate: Boolean = false,
)
