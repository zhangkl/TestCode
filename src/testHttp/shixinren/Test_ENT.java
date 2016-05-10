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
public class Test_ENT implements Runnable {
    private static Logger logger = Logger.getLogger("Test_4.class");
    private static String[] areaName = {"北京","天津","河北","山西","内蒙古","吉林","黑龙江","上海","江苏","浙江","安徽","福建","江西","山东","河南","湖北","湖南","广东","广西","海南","重庆","四川","贵州","云南","西藏","陕西","甘肃","青海","宁夏","新疆","香港","澳门","台湾"};
    private static String[] cardNumArray = {"0","1","2","3","4","5","6","7","8","9","x"};
    private int sucessCount = 0;
    private long dateCount = 0;
    TestConn testConn;
    Statement statement;
    Statement statement2;
    String cardNum;
    int pn;

    public Test_ENT(int sucessCount, long dateCount, TestConn testConn, Statement statement, Statement statement2, String cardNum, int pn) {
        this.sucessCount = sucessCount;
        this.dateCount = dateCount;
        this.testConn = testConn;
        this.statement = statement;
        this.statement2 = statement2;
        this.cardNum = cardNum;
        this.pn = pn;
    }

    public static void main(String[] args) {
        try {
            for (int i = 0; i < cardNumArray.length; i++) {
                TestConn testConn = new TestConn();
                Test_ENT test4 = new Test_ENT(0, 0, testConn,testConn.creatStatement(),testConn.creatStatement(),cardNumArray[i],0);
                Thread thread = new Thread(test4);
                thread.setName("thread"+i);
                thread.start();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        //System.out.println(Thread.currentThread().getName());
        getData(cardNum);
    }

    public void getData(String entCardNum) {
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
            currentQuery = entCardNum;
            map.put("cardNum", entCardNum);
            for (int j = 0; j < areaName.length; j++) {
                map.put("areaName", areaName[j]);
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
                        String str = Thread.currentThread().getName()+":"+"查询条件:" + currentQuery + "," + areaName[j] + ",pn:" + pn + ",当前查询条件总条数:" + dateCount;
                        logger.info(str);
                        ReadWriteFileWithEncode.write("D:\\code\\TestCode\\logs\\ent\\"+Thread.currentThread().getName()+".txt",str,"UTF-8");
                        break;
                    }
                    String str = Thread.currentThread().getName()+":"+"查询条件:" + currentQuery + "," + areaName[j] + ",pn:" + pn + ",目前成功插入条数:" + sucessCount + ",当前查询条件总条数:" + dateCount;
                    logger.info(str);
                    ReadWriteFileWithEncode.write("D:\\code\\TestCode\\logs\\ent\\" + Thread.currentThread().getName() + ".txt",str,"UTF-8");
                    pn += 50;
                }while(pn<=dateCount);
                pn = 0;
            }
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
