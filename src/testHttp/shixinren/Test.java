package testHttp.shixinren;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import testFile.ReadWriteFileWithEncode;
import testHttp.httpUtil.TestHttp;
import testHttp.dao.TestConn;
import testHttp.httpUtil.HttpRespons;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: zhangkl
 * Date: 2016/3/16
 * Time: 17:31
 * To change this template use File | Settings | File Templates.
 */
public class Test implements Runnable {
    private static Logger logger = Logger.getLogger("Test_4.class");

    private int sucessCount = 0;
    private long dateCount = 0;
    TestConn testConn;
    Statement statement;
    Statement statement2;
    int start;
    int end;
    int pn;

    public Test(int sucessCount, long dateCount, TestConn testConn, Statement statement, Statement statement2, int start, int end,int pn) {
        this.sucessCount = sucessCount;
        this.dateCount = dateCount;
        this.testConn = testConn;
        this.statement = statement;
        this.statement2 = statement2;
        this.start = start;
        this.end = end;
        this.pn = pn;
    }

    public static void main(String[] args) {
        try {
            /*for (int i = 0; i < 10000; i+=50) {
                TestConn testConn = new TestConn();
                Test_4 test4 = new Test_4(0, 0, testConn,testConn.creatStatement(),testConn.creatStatement(),i,i+50);
                Thread thread = new Thread(test4);
                thread.setName("thread:"+i+"--"+(i+50));
                thread.start();
            }*/
            TestConn testConn = new TestConn();
            Test test = new Test(0, 0, testConn,testConn.creatStatement(),testConn.creatStatement(),1110,1150,500);
            Thread thread = new Thread(test);
            thread.setName("thread:1100-1150");
            thread.start();

            TestConn testConn1 = new TestConn();
            Test test1 = new Test(0, 0, testConn1,testConn1.creatStatement(),testConn1.creatStatement(),859,900,100);
            Thread thread1 = new Thread(test1);
            thread1.setName("thread:850-900");
            thread1.start();

            TestConn testConn2 = new TestConn();
            Test test2 = new Test(0, 0, testConn2,testConn2.creatStatement(),testConn2.creatStatement(),259,300,100);
            Thread thread2 = new Thread(test2);
            thread2.setName("thread:250-300");
            thread2.start();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        //System.out.println(Thread.currentThread().getName());
        getData(start,end);
    }

    public void getData(int start,int end) {
        try {
            TestHttp testHttp = new TestHttp();
            testHttp.setDefaultContentEncoding("utf-8");
            Map map = new HashMap();
            String url;
            map.put("resource_id", "6899");
            map.put("query", "%E5%A4%B1%E4%BF%A1%E8%A2%AB%E6%89%A7%E8%A1%8C%E4%BA%BA%E5%90%8D%E5%8D%95");
            map.put("ie", "utf-8");
            map.put("oe", "utf-8");
            map.put("format", "json");
            String currentQuery = "";
            for (int i = start; i < end; i++) {
                String cartNum = String.valueOf(i);
                while(cartNum.length()<4){
                    cartNum = "0"+ cartNum;
                }
                currentQuery = cartNum;
                map.put("cardNum", cartNum);
                do  {
                    map.put("pn", String.valueOf(pn));
                    url = "https://sp0.baidu.com/8aQDcjqpAAV3otqbppnN2DJv/api.php";
                    try {
                        HttpRespons hr = testHttp.send(url, "GET", map, null);
                        String jsonString = hr.getContent();
                        json2Model(jsonString);
                    } catch (Exception e) {
                        logger.error(e.getMessage());
                        logger.error(map.toString());
                        continue;
                    }
                    /**
                     * 数据大于两千，百度取得数据为空
                     */
                    if (dateCount >= 2000) {
                        String str = Thread.currentThread().getName()+":"+"查询条件:" + cartNum + ",pn:" + pn + ",当前查询条件总条数:" + dateCount;
                        logger.error(str);
                        break;
                    }
                    String str = Thread.currentThread().getName()+":"+"查询条件:" + cartNum + ",pn:" + pn + ",目前成功插入条数:" + sucessCount + ",当前查询条件总条数:" + dateCount;
                    logger.info(str);
                    pn += 50;
                }while(pn<=dateCount);
                pn = 0;
            }
            ReadWriteFileWithEncode.write("D:\\code\\TestCode\\logs\\thred.txt",Thread.currentThread().getName() + ":循环结束。最后一次查询为：" + currentQuery,"UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void json2Model(String json) throws IOException {
        JSONObject jsonObject = JSONObject.fromObject(json);
        JSONArray jsonArray = JSONArray.fromObject(jsonObject.get("data"));
        if (jsonArray.size() > 0) {
            List list = JSONArray.fromObject(jsonArray.get(0));
            JSONObject jsonObject1 = JSONObject.fromObject(list.get(0));
            String dispNumValue = jsonObject1.getString("dispNum");
            dateCount = Integer.valueOf(dispNumValue);
            if (dateCount >= 2000) {
                return;
            }
            Iterator it = jsonObject1.keys();
            JSONObject jsonObject2 = null;
            try {
                while (it.hasNext()) {
                    String key = (String) it.next();
                    if ("result".equals(key)) {
                        JSONArray array = jsonObject1.getJSONArray(key);
                        for (int i = 0; i < array.size(); i++) {
                            StringBuffer sql = new StringBuffer();
                            sql.append("insert into CRED_DISHONESTY (IID, SSTDSTG, SSTDSTL, DUPDATE_TIME, SLOC, DLASTMOD, SCHANGEFREQ, SPRIORITY, SSITELINK, SINAME, STYPE, SCARDNUM," +
                                    " SCASECODE, IAGE, SSEXY, SFOCUSNUMBER, SAREANAME, SBUSINESSENTITY, SCOURTNAME, SDUTY, SPERFORMANCE, SDISRUPTTYPENAME, DPUBLISHDATE, " +
                                    "SPARTYTYPENAME, SGISTID, DREGDATE, SGISTUNIT, SPERFORMEDPART, SUNPERFORMPART, SPUBLISHDATESTAMP, SSITEID) values (");
                            jsonObject2 = JSONObject.fromObject(array.get(i));
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
                        }
                        int[] suc = statement.executeBatch();
                        for (int i = 0; i < suc.length; i++) {
                            sucessCount += suc[i];
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                logger.error(Thread.currentThread().getName() + ":" + e.getMessage());
                logger.error(Thread.currentThread().getName() + ":" + jsonObject2.toString());
            }
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
