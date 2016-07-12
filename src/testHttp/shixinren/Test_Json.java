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
            /*Test_Json tj = new Test_Json();
            String path = "C:\\Users\\lenovo-01\\Desktop\\error.txt";
            File file = new File(path);
            tj.read(file);*/
            Test_Json tj = new Test_Json();
            tj.testConn = new TestConn();
            tj.statement = tj.testConn.creatStatement();
            tj.statement2 = tj.testConn.creatStatement();
            tj.saveJson("{\n" +
                    "    \"iname\": \"王彬\",\n" +
                    "    \"StdStg\": 6899,\n" +
                    "    \"changefreq\": \"always\",\n" +
                    "    \"StdStl\": 8,\n" +
                    "    \"type\": \"失信被执行人名单\",\n" +
                    "    \"_update_time\": \"1467877413\",\n" +
                    "    \"publishDateStamp\": \"1467043200\",\n" +
                    "    \"performedPart\": \"\",\n" +
                    "    \"sexy\": \"男\",\n" +
                    "    \"disruptTypeName\": \"其他有履行能力而拒不履行生效法律文书确定义务\",\n" +
                    "    \"courtName\": \"颍州区人民法院\",\n" +
                    "    \"duty\": \"被告支付原告189448.6元\",\n" +
                    "    \"partyTypeName\": \"0\",\n" +
                    "    \"gistUnit\": \"安徽省阜阳市颍州区人民法院\",\n" +
                    "    \"priority\": \"1.0\",\n" +
                    "    \"performance\": \"全部未履行\",\n" +
                    "    \"age\": \"36\",\n" +
                    "    \"gistId\": \"2003州刑初字第5号\",\n" +
                    "    \"businessEntity\": \"\",\n" +
                    "    \"publishDate\": \"2016年06月28日\",\n" +
                    "    \"SiteId\": 2004188,\n" +
                    "    \"cardNum\": \"34122219800****6872\",\n" +
                    "    \"focusNumber\": \"0\",\n" +
                    "    \"regDate\": \"20030227\",\n" +
                    "    \"loc\": \"http://shixin.court.gov.cn/detail?id=120843028\",\n" +
                    "    \"sitelink\": \"http://shixin.court.gov.cn/\",\n" +
                    "    \"lastmod\": \"2016-07-07T14:28:12\",\n" +
                    "    \"_select_time\": 1467877299,\n" +
                    "    \"_version\": 14712,\n" +
                    "    \"caseCode\": \"(2003)皖1202执字第00174号\",\n" +
                    "    \"areaName\": \"安徽\",\n" +
                    "    \"unperformPart\": \"\"\n" +
                    "}");
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
            Timestamp timestamp2 = new Timestamp(Long.valueOf(publishDateStamp));
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
