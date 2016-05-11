package testHttp.wangdaizhijia;

import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.htmlparser.util.ParserException;
import testHttp.dao.TestConn;
import testHttp.httpUtil.HttpRespons;
import testHttp.httpUtil.TestHttp;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: zhangkl
 * Date: 16-2-22
 * Time: 下午2:13
 * To change this template use File | Settings | File Templates.
 */
public class GetWDZJ_ForStatus extends Thread {
    private static Logger logger = Logger.getLogger("GetWDZJ.class");

    private int sucessCount = 0;
    private int dateCount = 0;
    TestConn testConn;
    PreparedStatement ps;
    Statement statement2;
    int start;
    int end;

    public GetWDZJ_ForStatus(int sucessCount, int dateCount, TestConn testConn, PreparedStatement preparedStatement, Statement statement2, int start, int end) {
        this.sucessCount = sucessCount;
        this.dateCount = dateCount;
        this.testConn = testConn;
        this.ps = preparedStatement;
        this.statement2 = statement2;
        this.start = start;
        this.end = end;
    }

    public static void main(String[] args) {
        try {
            int j = 1;
            for (int i = 1; i < 129; i += 8) {
                String sql = new String("update cred_wdzj_platinfo set splatstatus = ? where splatid = ?");
                TestConn testConn = new TestConn();
                GetWDZJ_ForStatus getWDZJ = new GetWDZJ_ForStatus(0, 0, testConn, testConn.creatPStatement(sql), testConn.creatStatement(), i, i + 8);
                Thread thread = new Thread(getWDZJ);

                thread.setName("thread" + j++);
                thread.start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            TestHttp testHttp = new TestHttp();
            Map map = new HashMap();
            for (int i = start; i < end; i++) {
                if (i > 128) {
                    break;
                }
                String url = "http://www.wdzj.com/front_select-plat?currPage=" + i;
                HttpRespons hr = testHttp.send(url, "GET", map, null);
                String result = hr.getContent();
                json2Model(result);
                System.out.println(Thread.currentThread().getName() + "：当前页：" + i + "，成功条数:" + sucessCount);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void json2Model(String json) throws IOException, SQLException, ParserException {
        JSONObject jsonObject = JSONObject.fromObject(json);
        String rowCount = jsonObject.get("rowCount").toString();
        dateCount = Integer.parseInt(rowCount);
        List list = (List) jsonObject.get("list");
        for (int i = 0; i < list.size(); i++) {
            Map map = (Map) list.get(i);
            String splatid = (String) map.get("platId");
            int splatStatus = (Integer) map.get("platStatus");
            ps.setInt(1, splatStatus);  //iid
            ps.setString(2, splatid);//
            ps.execute();
            sucessCount++;
        }
    }

    private String formatString(String sshareholderstructure) {
        Pattern p = Pattern.compile("\\s*|\t|\r|\n");
        sshareholderstructure = p.matcher(sshareholderstructure).replaceAll("");
        return sshareholderstructure;
    }

    private synchronized int getSeqNextVal(String seqName) throws SQLException {
        ResultSet newrs;
        int id = 0;
        newrs = statement2.executeQuery("select " + seqName + ".nextval as id from dual");
        try {
            if (newrs.next()) {
                id = newrs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }

}
