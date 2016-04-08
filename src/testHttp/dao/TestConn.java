package testHttp.dao;



import java.sql.*;

public class TestConn {

    //dbUrl数据库连接串信息，其中“1521”为端口，“ora9”为sid
    String dbUrl = "jdbc:oracle:thin:@192.168.0.196:1521:cred";
    //theUser为数据库用户名
    String theUser = "cred";
    //thePw为数据库密码
    String thePw = "cred";
    //几个数据库变量
    public Connection connection = null;
    PreparedStatement ps;
    Statement statement;
    ResultSet rs = null;
    ConnectionPool conPool;
    //初始化连接
    public TestConn() {
        try {
/*            Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
     //与url指定的数据源建立连接

            c = DriverManager.getConnection(dbUrl, theUser, thePw);*/

            conPool = new ConnectionPool("oracle.jdbc.driver.OracleDriver",dbUrl,theUser,thePw,2);
            //采用Statement进行查询
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Statement creatStatement(){

        try {
            connection = conPool.getConnection();
            statement = connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return statement;
    }

    public PreparedStatement creatPStatement(String sql){

        try {
            connection = conPool.getConnection();
            ps = connection.prepareStatement(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ps;
    }
    //执行查询
    public ResultSet executeQuery(String sql) {
        rs = null;
        try {
            connection = conPool.getConnection();
            statement = connection.createStatement();
            rs = statement.executeQuery(sql);
            conPool.returnConnection(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }

    public boolean isClosed() throws SQLException {
        if (connection==null || connection.isClosed())
            return true;
        else
            return false;
    }

    public ResultSet executeSave(String sql, Clob clob) {
        rs = null;
        try {
            connection = conPool.getConnection();
            ps = connection.prepareStatement(sql);
            ps.setClob(1, clob);
            ps.execute();
            conPool.returnConnection(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }  finally {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return rs;
    }
    public void close() {
        try {
            if (rs!=null)
                rs.close();
            if(statement !=null)
                statement.close();
            if(connection!=null)
                connection.close();
            if (ps!=null)
                ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
