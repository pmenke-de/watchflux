package de.pmenke.watchflux.postgres.archiver;

/**
 * Created by pmenke on 02.07.17.
 */
public class ArchiverStatTask {

    private String label;
    private String url;
    private String username;
    private String password;

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

    @Override
    public String toString() {
        return "ReplicationStatTask{" +
                "label='" + label + '\'' +
                ", url='" + url + '\'' +
                ", username='" + username + '\'' +
                ", password='<filtered/>'" +
                '}';
    }

}
