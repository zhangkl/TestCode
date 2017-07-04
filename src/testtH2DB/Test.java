/*******************************************************************************
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 ******************************************************************************/

package testtH2DB;

import dishonest.dao.TestConn;
import org.h2.tools.Server;

import java.sql.*;

/**
 * Created with IntelliJ IDEA.
 * User: zhangkl
 * Date: 2016/4/8
 * Time: 16:44
 * To change this template use File | Settings | File Templates.
 */
public class Test {

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        Class.forName("org.h2.Driver");
        Connection conn = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/test", "sa", "sa");
        Server.createTcpServer().start();
        /*statement.execute("DROP TABLE IF EXISTS TEST");
        statement.execute("CREATE TABLE TEST(NAME VARCHAR)");*/
        TestConn testConn = TestConn.getInstance();
        Statement ora_statement = testConn.creatStatement();
        String sql = "select * from CRED_DISHONESTY";
        ResultSet rs = ora_statement.executeQuery(sql);
        StringBuffer stringBuffer = new StringBuffer("INSERT INTO CRED_DISHONESTY values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
        PreparedStatement statement = conn.prepareStatement(stringBuffer.toString());
        while (rs.next()){
                statement.setInt(1, rs.getInt("iid"));
                statement.setString(2, rs.getString("sstdstg"));
                statement.setString(3, rs.getString("sstdstl"));
                statement.setTimestamp(4, rs.getTimestamp("dupdate_time"));
                statement.setString(5, rs.getString("sloc"));
                statement.setDate(6, rs.getDate("dlastmod"));
                statement.setString(7, rs.getString("schangefreq"));
                statement.setString(8, rs.getString("spriority"));
                statement.setString(9, rs.getString("ssitelink"));
                statement.setString(10, rs.getString("siname"));
                statement.setString(11, rs.getString("stype"));
                statement.setString(12, rs.getString("scardnum"));
                statement.setString(13, rs.getString("scasecode"));
                statement.setInt(14, rs.getInt("iage"));
                statement.setString(15, rs.getString("ssexy"));
                statement.setString(16, rs.getString("sfocusnumber"));
                statement.setString(17, rs.getString("sareaname"));
                statement.setString(18, rs.getString("sbusinessentity"));
                statement.setString(19, rs.getString("scourtname"));
                statement.setString(20, rs.getString("sduty"));
                statement.setString(21, rs.getString("sperformance"));
                statement.setString(22, rs.getString("sdisrupttypename"));
                statement.setDate(23, rs.getDate("dpublishdate"));
                statement.setString(24, rs.getString("spartytypename"));
                statement.setString(25, rs.getString("sgistid"));
                statement.setDate(26, rs.getDate("dregdate"));
                statement.setString(27, rs.getString("sgistunit"));
                statement.setString(28, rs.getString("sperformedpart"));
                statement.setString(29, rs.getString("sunperformpart"));
                statement.setTimestamp(30, rs.getTimestamp("spublishdatestamp"));
                statement.setString(31, rs.getString("ssiteid"));
                statement.execute();
        }

    }

    public static void ora_db() throws SQLException {
        TestConn testConn = TestConn.getInstance();
        Statement ora_statement = testConn.creatStatement();
        String sql = "select * from CRED_DISHONESTY";
        ResultSet rs = ora_statement.executeQuery(sql);
        while (rs.next()){

        }
    }
}
