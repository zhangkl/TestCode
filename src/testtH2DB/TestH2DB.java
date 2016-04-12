package testtH2DB;

import org.h2.tools.Server;

import java.sql.*;

/**
 * Created with IntelliJ IDEA.
 * User: zhangkl
 * Date: 2016/4/8
 * Time: 14:54
 * To change this template use File | Settings | File Templates.
 */

public final class TestH2DB extends Thread {
    private static Server server;
    private static TestH2DB db;
    static PreparedStatement statement;
    static Statement sta;
    static PreparedStatement statement2;

    static {
        try {
            Class.forName("org.h2.Driver");
            Connection conn = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/test", "sa", "sa");
            String querySql = "select count(*) as count from TEST";
            String insertSql = "INSERT INTO TEST values ('wang')";
            sta = conn.createStatement();
            initTable();
            statement = conn.prepareStatement(insertSql);
            statement2 = conn.prepareStatement(querySql);
            server = Server.createTcpServer().start();
            db = new TestH2DB();
            System.out.println("正在启动h2数据库...");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    //启动H2数据库服务
    public void Start() {
        try {
            server.stop();
            System.out.println("正在启动h2数据库...");
            //使用org.h2.tools.Server这个类创建一个H2数据库的服务并启动服务
            server = Server.createTcpServer().start();
            System.out.println("h2数据库启动成功...");
        } catch (SQLException e) {
            System.out.println("启动h2数据库出错：" + e.toString());
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    //停止H2数据库服务
    public void Stop() {
        if (server != null) {
            System.out.println("正在关闭h2...");
            server.stop();
            System.out.println("关闭成功.");
        }
    }

    //初始化H2表
    public static void initTable() {
        try {
            //  初始化表
            sta.execute("DROP TABLE IF EXISTS TEST");
            sta.execute("CREATE TABLE TEST(NAME VARCHAR)");
            sta.execute("INSERT INTO TEST values ('wang')");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static TestH2DB getinstance(){
        System.out.println(db==null);
        if( db == null){
            db = new TestH2DB();
            return db;
        }else{
            return db;
        }
    }

    public void insertData() throws SQLException {
        Long startTime =  System.currentTimeMillis();
        for (int i = 0; i < 8000000L; i++) {
            statement.execute();
        }
        Long insertTime  = System.currentTimeMillis();
        System.out.println("插入时间：" + (insertTime - startTime));
    }

    public void getCount() throws SQLException {
        ResultSet resultSet = statement2.executeQuery();
        while (resultSet.next()){
            System.out.println( resultSet.getString("count"));
        }
    }

    @Override
    public void run() {
        try {
            insertData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws SQLException, InterruptedException {
        Thread thread = new Thread(db);
        thread.start();
        Thread thread2 = new Thread(db);
        thread2.start();
        Thread thread3 = new Thread(db);
        thread3.start();
        while (true){
            thread.sleep(2000);
            db.getCount();
        }
    }

}

