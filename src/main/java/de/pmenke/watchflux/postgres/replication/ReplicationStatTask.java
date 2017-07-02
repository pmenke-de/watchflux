package de.pmenke.watchflux.postgres.replication;

/**
 * Created by pmenke on 02.07.17.
 */
public class ReplicationStatTask {

    private String label;
    private String url;
    private String username;
    private String password;
    private String subscriptionName;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSubscriptionName() {
        return subscriptionName;
    }

    public void setSubscriptionName(String subscriptionName) {
        this.subscriptionName = subscriptionName;
    }

    @Override
    public String toString() {
        return "ReplicationStatTask{" +
                "label='" + label + '\'' +
                ", url='" + url + '\'' +
                ", username='" + username + '\'' +
                ", password='<filtered/>'" +
                ", subscriptionName='" + subscriptionName + '\'' +
                '}';
    }

}
