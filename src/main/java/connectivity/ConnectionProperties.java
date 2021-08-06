package connectivity;

public class ConnectionProperties {
    private final String hostname;

    private final int port;
    private final String userLogin;

    private final String password;

    public ConnectionProperties(String hostname, int port, String userLogin, String password) {
        this.hostname = hostname;
        this.port = port;
        this.userLogin = userLogin;
        this.password = password;
    }

    public String getOracleURL(){
        return "jdbc:oracle:thin:@" + hostname + ":" + port + ":XE";
    }

    public String getUserLogin() {
        return userLogin;
    }

    public String getPassword() {
        return password;
    }

    public String getHostname() {
        return hostname;
    }

    public int getPort() {
        return port;
    }
}
