package testHttp.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;

public class ConnectionPool {
    private Hashtable connections = new Hashtable();
    private Properties props;

    public ConnectionPool(String driverClassName, String dbURL, String user, String password, int initialConnections) throws SQLException, ClassNotFoundException {
        props = new Properties();
        props.put("connection.driver", driverClassName);
        props.put("connection.url", dbURL);
        props.put("user", user);
        props.put("password", password);
        initializePool(props, initialConnections);
    }

    public Connection getConnection() throws SQLException {
        Connection con = null;

        Enumeration cons = connections.keys();

        synchronized (connections) {
            while (cons.hasMoreElements()) {
                con = (Connection) cons.nextElement();

                Boolean b = (Boolean) connections.get(con);
                if (b == Boolean.FALSE) {
                    try {
                        con.setAutoCommit(true);
                    } catch (SQLException e) {
                        try {
                            con.close();
                        } catch (SQLException ignored) {
                        }
                        connections.remove(con);
                        con = getNewConnection();
                    }
                    connections.put(con, Boolean.TRUE);
                    return con;
                }
            }
            con = getNewConnection();
            connections.put(con, Boolean.TRUE);
            return con;
        }
    }

    public void returnConnection(Connection returned) {
        if (connections.containsKey(returned)) {
            connections.put(returned, Boolean.FALSE);
        }
    }

    private void initializePool(Properties props, int initialConnections) throws SQLException, ClassNotFoundException {
        Class.forName(props.getProperty("connection.driver"));
        for (int i = 0; i < initialConnections; i++) {
            Connection con = getNewConnection();
            connections.put(con, Boolean.FALSE);
        }
    }

    private Connection getNewConnection() throws SQLException {
        return DriverManager.getConnection(props.getProperty("connection.url"), props);
    }


}
