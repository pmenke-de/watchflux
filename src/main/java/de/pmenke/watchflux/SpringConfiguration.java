package de.pmenke.watchflux;

import de.pmenke.watchflux.postgres.PostgresWatchConfig;
import de.pmenke.watchflux.postgres.archiver.ArchiverStatCollector;
import de.pmenke.watchflux.postgres.replication.ReplicationStatCollector;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.concurrent.TimeUnit;

/**
 * Created by pmenke on 02.07.17.
 */
@Configuration
@EnableScheduling
@EnableAutoConfiguration
@EnableConfigurationProperties
public class SpringConfiguration {
    
    @Autowired
    private Environment env;
    
    @Bean
    public PostgresWatchConfig postgresWatchConfig() {
        return new PostgresWatchConfig();
    }
    
    @Bean
    public InfluxDB influxDB() {
        final InfluxDB influxDB = InfluxDBFactory.connect(
                env.getRequiredProperty("influx.url"),
                env.getRequiredProperty("influx.username"),
                env.getRequiredProperty("influx.password"));
        influxDB.enableBatch(1000, 1000, TimeUnit.MILLISECONDS);
        influxDB.setDatabase(env.getRequiredProperty("influx.database"));
        return influxDB;
    }
    
    @Bean
    public ArchiverStatCollector pgArchiverStatCollector() {
        return new ArchiverStatCollector(postgresWatchConfig(), influxDB());
    }
    
    @Bean
    public ReplicationStatCollector pgReplicationStatCollector() {
        return new ReplicationStatCollector(postgresWatchConfig(), influxDB());
    }
}
