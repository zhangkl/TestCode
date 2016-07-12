package testHttp.dao;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;

public class JDBCutils {

    private static Connection conn;
    private static ComboPooledDataSource ds = new ComboPooledDataSource();

    public static Connection getConnection() {
        try {
            ds.setDriverClass("oracle.jdbc.driver.OracleDriver");
            ds.setJdbcUrl("jdbc:oracle:thin:@192.168.0.196:1521:cred?useUnicode=true&characterEncoding=UTF8&useServerPrepStmts=true&prepStmtCacheSqlLimit=256&cachePrepStmts=true&prepStmtCacheSize=256&rewriteBatchedStatements=true");
            ds.setUser("cred");
            ds.setPassword("cred");
            conn = ds.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        }
        return conn;
    }
}