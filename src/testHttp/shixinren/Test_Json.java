package testHttp.shixinren;

import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import testHttp.dao.TestConn;

import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

/**
 * Created with IntelliJ IDEA.
 * User: zhangkl
 * Date: 2016/3/16
 * Time: 17:31
 * To change this template use File | Settings | File Templates.
 */
public class Test_Json {
    private static Logger logger = Logger.getLogger("Test_4.class");
    private int sucessCount = 0;
    private long dateCount = 0;
    public TestConn testConn;
    public Statement statement;
    public Statement statement2;

    public static void main(String[] args) {
        try {
            Test_Json tj = new Test_Json();
            String path = "C:\\Users\\lenovo-01\\Desktop\\error.txt";
            File file = new File(path);
            tj.read(file);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void read(File src) {
        String line = null;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(src), "UTF-16"));
            Test_Json tj = new Test_Json();
            tj.testConn = new TestConn();
            tj.statement = tj.testConn.creatStatement();
            tj.statement2 = tj.testConn.creatStatement();
            while ((line = reader.readLine()) != null) {
                int index = line.indexOf("{\"iname\"");
                if (index > 0) {
                    System.out.println(line.substring(index));
                    String json = line.substring(index);
                    tj.saveJson(json);
                }
            }

            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void saveJson(String json) throws IOException {
        JSONObject jsonObject2 = null;
        try {
            jsonObject2 = JSONObject.fromObject(json);;
            StringBuffer sql = new StringBuffer();
            sql.append("insert into CRED_DISHONESTY (IID, SSTDSTG, SSTDSTL, DUPDATE_TIME, SLOC, DLASTMOD, SCHANGEFREQ, SPRIORITY, SSITELINK, SINAME, STYPE, SCARDNUM," +
                    " SCASECODE, IAGE, SSEXY, SFOCUSNUMBER, SAREANAME, SBUSINESSENTITY, SCOURTNAME, SDUTY, SPERFORMANCE, SDISRUPTTYPENAME, DPUBLISHDATE, " +
                    "SPARTYTYPENAME, SGISTID, DREGDATE, SGISTUNIT, SPERFORMEDPART, SUNPERFORMPART, SPUBLISHDATESTAMP, SSITEID) values (");
            int iid = getSeqNextVal("SEQ_CRED_DISHONESTY");
            sql.append(iid + ",");  //iid
            sql.append("'" + jsonObject2.getString("StdStg") + "'" + ",");
            sql.append("'" + jsonObject2.getString("StdStl") + "'" + ",");
            String _update_time = jsonObject2.getString("_update_time");
            while (_update_time.length() < 13) {
                _update_time += "0";
            }
            Timestamp timestamp = new Timestamp(Long.valueOf(_update_time));
            sql.append("to_timestamp('" + timestamp + "', 'yyyy-mm-dd hh24:mi:ss:ff')" + ",");
            sql.append("'" + jsonObject2.getString("loc") + "'" + ",");
            StringBuffer lastmod = new StringBuffer(jsonObject2.getString("lastmod"));
            lastmod.replace(10, 11, " ");
            sql.append("to_date('" + lastmod + "','yyyy-mm-dd hh24:mi:ss'),");
            sql.append("'" + jsonObject2.getString("changefreq") + "'" + ",");
            sql.append("'" + jsonObject2.getString("priority") + "'" + ",");
            sql.append("'" + jsonObject2.getString("sitelink") + "'" + ",");
            sql.append("'" + jsonObject2.getString("iname") + "'" + ",");
            sql.append("'" + jsonObject2.getString("type") + "'" + ",");
            sql.append("'" + jsonObject2.getString("cardNum").replace("****", "---") + "'" + ",");
            sql.append("'" + jsonObject2.getString("caseCode") + "'" + ",");
            int age = Integer.valueOf(jsonObject2.getString("age"));
            sql.append(age + ",");
            sql.append("'" + jsonObject2.getString("sexy") + "'" + ",");
            sql.append("'" + jsonObject2.getString("focusNumber") + "'" + ",");
            sql.append("'" + jsonObject2.getString("areaName") + "'" + ",");
            sql.append("'" + jsonObject2.getString("businessEntity") + "'" + ",");
            sql.append("'" + jsonObject2.getString("courtName") + "'" + ",");
            sql.append("'" + jsonObject2.getString("duty") + "'" + ",");
            sql.append("'" + jsonObject2.getString("performance") + "'" + ",");
            sql.append("'" + jsonObject2.getString("disruptTypeName") + "'" + ",");
            String publishDate = jsonObject2.getString("publishDate");
            publishDate = publishDate.replaceAll("[^0-9]", "-").substring(0, publishDate.length() - 1);
            sql.append("to_date('" + publishDate + "','yyyy-mm-dd hh24:mi:ss'),");
            sql.append("'" + jsonObject2.getString("partyTypeName") + "'" + ",");
            sql.append("'" + jsonObject2.getString("gistId") + "'" + ",");
            String regDate = jsonObject2.getString("regDate");
            String year = regDate.substring(0, 4);
            String month = regDate.substring(4, 6);
            String day = regDate.substring(6, 8);
            regDate = year + "-" + month + "-" + day;
            sql.append("to_date('" + regDate + "','yyyy-mm-dd hh24:mi:ss'),");
            sql.append("'" + jsonObject2.getString("gistUnit") + "'" + ",");
            sql.append("'" + jsonObject2.getString("performedPart") + "'" + ",");
            sql.append("'" + jsonObject2.getString("unperformPart") + "'" + ",");
            String publishDateStamp = jsonObject2.getString("publishDateStamp");
            while (publishDateStamp.length() < 13) {
                publishDateStamp += "0";
            }
            Timestamp timestamp2 = new Timestamp(Long.valueOf(_update_time));
            sql.append("to_timestamp('" + timestamp2 + "', 'yyyy-mm-dd hh24:mi:ss:ff')" + ",");
            sql.append("'" + jsonObject2.getString("SiteId") + "'");
            sql.append(")");
            statement.addBatch(String.valueOf(sql));
            int[] suc = statement.executeBatch();
            for (int i = 0; i < suc.length; i++) {
                sucessCount += suc[i];
            }
            String str ="成功插入条数:" + sucessCount + ",当前查询条件总条数:" + dateCount;
            logger.info(str);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(Thread.currentThread().getName() + ":" + e.getMessage());
            logger.error(Thread.currentThread().getName() + ":" + jsonObject2.toString());
        }
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
