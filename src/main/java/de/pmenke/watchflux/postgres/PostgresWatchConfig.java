package de.pmenke.watchflux.postgres;

import de.pmenke.watchflux.postgres.archiver.ArchiverStatTask;
import de.pmenke.watchflux.postgres.replication.ReplicationStatTask;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * Created by pmenke on 02.07.17.
 */
@ConfigurationProperties(prefix = "postgres")
public class PostgresWatchConfig {
    private List<ArchiverStatTask>    archiver;
    private List<ReplicationStatTask> replication;

    public List<ArchiverStatTask> getArchiver() {
        return archiver;
    }

    public void setArchiver(List<ArchiverStatTask> archiver) {
        this.archiver = archiver;
    }
    
    public List<ReplicationStatTask> getReplication() {
        return replication;
    }

    public void setReplication(List<ReplicationStatTask> replication) {
        this.replication = replication;
    }

    @Override
    public String toString() {
        return "PostgresWatchConfig{" +
                "archiver=" + archiver +
                ", replication=" + replication +
                '}';
    }
}
