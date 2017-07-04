/*******************************************************************************
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 ******************************************************************************/

package testHttp.shixinren;

import dishonest.dao.TestConn;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import testFile.ReadWriteFileWithEncode;
import testHttp.httpUtil.HttpRespons;
import testHttp.httpUtil.TestHttp;

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
public class Test_Array implements Runnable {
    private static Logger logger = Logger.getLogger("Test_4.class");
    private static String[] areaName = {"北京","天津","河北","山西","内蒙古","吉林","黑龙江","上海","江苏","浙江","安徽","福建","江西","山东","河南","湖北","湖南","广东","广西","海南","重庆","四川","贵州","云南","西藏","陕西","甘肃","青海","宁夏","新疆","香港","澳门","台湾"};
    private static String[] cardNumArray = {"0011","0012","0013","0014","0015","0016","0017","0018","0019","002","0020","0021","0022","0023","0024","0025","0026","0027","0028","0029","003","0030","0031","0032","0033","0034","0035","0036","0037","0038","0039"};
    private static String[] cardNumArray1 = {"005","0050","0051","0052","0053","0054","0056","0057","0058","0059","031","0310","0311","0312","0313","0314","0315","0316","0317","0318","0319","051","0510","0511","0512","0513","0514","0515","0516","0517","0518","0519"};
    private static String[] cardNumArray2 = {"0610","0611","0612","0613","0615","0617","0618","0817","101","1010","1011","1012","1013","1014","1015","1016","1017","1018","1019","121","1210","1211","1212","1213","1214","1215","1217","1218","1219","151","1510","1511"};
    private static String[] cardNumArray3 = {"1512","1513","1514","1515","1516","1517","1518","1519","2010","2011","2012","2013","2014","2015","2016","2017","2018","2019","3010","3011","3012","3013","3014","3015","3016","3017","3018","3019"};
    TestConn testConn;
    Statement statement;
    Statement statement2;
    int start;
    int end;
    int pn;
    /*private static String[] cardNumArray1 = {"1512","0011","0311","1211","0611","1513","0012","1212","0612","0312","1514","1213","0013","0613","0313"};
    private static String[] cardNumArray2 = { "1515","1214","0014","0314","3010","1516","2010","0315","1215","0015","0510","3011","1517","1010","0016"};
    private static String[] cardNumArray3 = { "0316","2011","0511","3012","1518","1011","0317","0017","2012","0512","3013","1519","1012","2013","0318"};
    private static String[] cardNumArray4 = { "0018","0513","3014","1013","0319","2014","0019","0514","3015","1014","2015","0020","0515","3016","2016"};
    private static String[] cardNumArray5 = { "0021","1015","0516","3017","2017","0022","1016","0517","3018","2018","0023","1017","0518","3019","2019"};
    private static String[] cardNumArray6 = { "0024","1018","0519","0025","1019","0026","0027","0028","0029","0030","0031","0032","0033","0034","0035"};
    private static String[] cardNumArray7 = { "0036","0037","0038","0039","0615","1217","1218","1219","0617","0618","0817","0010","0030","0020","0050"};
    private static String[] cardNumArray8 = { "0011","0031","0021","0051","0010","0020","0050","0030","001","031","101","002","151","201","121","051","301"};
    private static String[] cardNumArray9 = { "003","005","0050","0051","0052","0053","0054","0056","0057","1510","0010","0310","0058","0610","1511","0011"};
    private static String[] cardNumArray10 = { "0059","0311","0611","1512","0012","1210","0612","1513","0312","0013","1211","0613","0313","1514","1212","1515"};
    private static String[] cardNumArray11 = { "0314","0014","1213","2010","0315","1516","0510","1010","0015","1214","2011","0316","1517","0016","0511","1011"};
    private static String[] cardNumArray12 = { "1215","3010","0317","1518","0512","0017","1012","3011","0318","2012","0513","1519","0319","3012","2013","0018"};
    private static String[] cardNumArray13 = { "1013","0514","2014","0515","1014","3013","0019","2015","0516","1015","3014","0517","2016","3015","1016","0518"};
    private static String[] cardNumArray14 = { "2017","0020","3016","1017","0519","2018","0021","3017","2019","0022","3018","1018","0023","3019","1019","0024"};
    private static String[] cardNumArray15 = { "0025","0026","0027","0028","0029","0030","0031","0032","0033","0034","0035","0036","0037","0038","0039","0615"};
    private static String[] cardNumArray16 = { "1217","1218","1219","0617","0618","0817"};*/
    //private static String[] cardNumArray = {"001","301","201"};
    private int sucessCount = 0;
    private long dateCount = 0;

    public Test_Array(int sucessCount, long dateCount, TestConn testConn, Statement statement, Statement statement2, String[] cardNumArray, int pn) {
        this.sucessCount = sucessCount;
        this.dateCount = dateCount;
        this.testConn = testConn;
        this.statement = statement;
        this.statement2 = statement2;
        Test_Array.cardNumArray = cardNumArray;
        this.pn = pn;
    }

    public static void main(String[] args) {
        try {
            TestConn testConn = TestConn.getInstance();
            Test_Array test4 = new Test_Array(0, 0, testConn,testConn.creatStatement(),testConn.creatStatement(),cardNumArray,0);
            Thread thread = new Thread(test4);
            thread.setName("thread:"+0);
            thread.start();

            Test_Array test1 = new Test_Array(0, 0, testConn, testConn.creatStatement(), testConn.creatStatement(), cardNumArray1, 0);
            Thread thread1 = new Thread(test1);
            thread1.setName("thread:" + 1);
            thread1.start();

            Test_Array test2 = new Test_Array(0, 0, testConn, testConn.creatStatement(), testConn.creatStatement(), cardNumArray2, 0);
            Thread thread2 = new Thread(test2);
            thread2.setName("thread:" + 2);
            thread2.start();

            Test_Array test3 = new Test_Array(0, 0, testConn, testConn.creatStatement(), testConn.creatStatement(), cardNumArray3, 0);
            Thread thread3 = new Thread(test3);
            thread3.setName("thread:" + 3);
            thread3.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        //System.out.println(Thread.currentThread().getName());
        getData(cardNumArray);
    }

    public void getData(String[] cardArray) {
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
            for (int i = 0; i < cardArray.length; i++) {
                String cartNum = cardArray[i];
                currentQuery = cartNum;
                map.put("cardNum", cartNum);
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
                            String str = Thread.currentThread().getName()+":"+"查询条件:" + cartNum + "," + areaName[j] + ",pn:" + pn + ",当前查询条件总条数:" + dateCount;
                            logger.error(str);
                            break;
                        }
                        String str = Thread.currentThread().getName()+":"+"查询条件:" + cartNum + "," + areaName[j] + ",pn:" + pn + ",目前成功插入条数:" + sucessCount + ",当前查询条件总条数:" + dateCount;
                        logger.info(str);
                        pn += 50;
                    }while(pn<=dateCount);
                    pn = 0;
                }
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
