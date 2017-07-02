package de.pmenke.watchflux.postgres.archiver;

import de.pmenke.watchflux.postgres.PostgresWatchConfig;
import org.influxdb.InfluxDB;
import org.influxdb.dto.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

import java.sql.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by pmenke on 02.07.17.
 */
public class ArchiverStatCollector {
    private final Logger LOG = LoggerFactory.getLogger(ArchiverStatCollector.class);
    
    private final PostgresWatchConfig watchConfig;
    private final InfluxDB influxDB;

    public ArchiverStatCollector(PostgresWatchConfig watchConfig, InfluxDB influxDB) {
        this.watchConfig = watchConfig;
        this.influxDB = influxDB;
        LOG.info("Registered PostgreSQL archiver checks: {}", watchConfig.getArchiver());
    }
    
    @Scheduled(fixedDelay = 10000)
    public void runIteration() {
        for (ArchiverStatTask statTask : watchConfig.getArchiver()) {
            try(final Connection c = DriverManager.getConnection(statTask.getUrl(), statTask.getUsername(), statTask.getPassword());
                final Statement stmt = c.createStatement();
                final ResultSet rs = stmt.executeQuery("SELECT * FROM pg_stat_archiver")) {
                while (rs.next()) {
                    final Point.Builder point = Point.measurement("postgres_archiver")
                            .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                            .tag("label", statTask.getLabel())
                            .addField("archived_count", rs.getLong("archived_count"))
                            .addField("failed_count", rs.getLong("failed_count"));
    
                    final String lastArchivedWal = rs.getString("last_archived_wal");
                    if (lastArchivedWal != null) {
                        point.addField("last_archived_wal", lastArchivedWal);
                    }
                    final Timestamp lastArchivedTime = rs.getTimestamp("last_archived_time");
                    if (lastArchivedTime != null) {
                        point.addField("archive_delay", System.currentTimeMillis() - lastArchivedTime.getTime());
                    }
                    
                    influxDB.write(point.build());
                }
            } catch (SQLException e) {
                LOG.error("Unable to get database connection", e);
            }

        }
    }
}
