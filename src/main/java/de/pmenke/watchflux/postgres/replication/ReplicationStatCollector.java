package de.pmenke.watchflux.postgres.replication;

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
public class ReplicationStatCollector {
    private final Logger LOG = LoggerFactory.getLogger(ReplicationStatCollector.class);
    
    private final PostgresWatchConfig watchConfig;
    private final InfluxDB influxDB;

    public ReplicationStatCollector(PostgresWatchConfig watchConfig, InfluxDB influxDB) {
        this.watchConfig = watchConfig;
        this.influxDB = influxDB;
        LOG.info("Registered PostgreSQL replication checks: {}", watchConfig.getArchiver());
    }
    
    @Scheduled(fixedDelay = 10000)
    public void runIteration() {
        for (ReplicationStatTask statTask : watchConfig.getReplication()) {
            try(final Connection c = DriverManager.getConnection(statTask.getUrl(), statTask.getUsername(), statTask.getPassword());
                final PreparedStatement pstmt = c.prepareStatement("SELECT status FROM pglogical.show_subscription_status(?)")) {
                pstmt.setString(1, statTask.getSubscriptionName());
                try(final ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        final Point.Builder point = Point.measurement("postgres_replication")
                                .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                                .tag("label", statTask.getLabel())
                                .addField("subscription_name", statTask.getSubscriptionName())
                                .addField("status", rs.getString("status"));
                        influxDB.write(point.build());
                    }
                }
            } catch (SQLException e) {
                LOG.error("Unable to get database connection", e);
            }

        }
    }
}
