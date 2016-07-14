/*******************************************************************************
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 ******************************************************************************/

package com.dishonest;

import com.dishonest.dao.TestConn;
import com.dishonest.util.CheckNumber;
import com.dishonest.util.DateUtil;
import com.dishonest.util.HttpUtil;
import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;
import net.sf.json.JSONObject;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.StringReader;
import java.sql.Date;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created with IntelliJ IDEA.
 * User: chq
 * Date: 16-7-11
 * Time: 下午3:44
 * To change this template use File | Settings | File Templates.
 */
public class TestNum implements Runnable {

    TestConn testConn = TestConn.getInstance();

    String cardNum;
    int endPageNum;
    int stratPageNum;
    int sucessNum;
    int sameNum;
    String code;
    HttpUtil httpUtil;

    public TestNum(String code, String cardNum, int stratPageNum, int endPageNum, HttpUtil httpUtil, int sucessNum, int sameNum) {
        this.code = code;
        this.cardNum = cardNum;
        this.stratPageNum = stratPageNum;
        this.endPageNum = endPageNum;
        this.httpUtil = httpUtil;
        this.sucessNum = sucessNum;
        this.sameNum = sameNum;
    }

    public static void main(String[] args) throws IOException, SQLException {
        ExecutorService threadPool = Executors.newFixedThreadPool(20);
        String querySql = "select * from cred_dishonesty_log ";
        List list = TestConn.getInstance().executeQueryForList(querySql);
        Iterator it = list.iterator();
        while (it.hasNext()) {
            Map map = (Map) it.next();
            String cardNum = (String) map.get("CARDNUM");
            String endpage = (String) map.get("ENDPAGE");
            String startpage = (String) map.get("STARTPAGE");
            String sucessNum = (String) map.get("SUCESSNUM");
            String sameNum = (String) map.get("SAMENUM");
            HttpUtil httpUtil = new HttpUtil();
            TestNum testNum = new TestNum(null, cardNum, Integer.valueOf(startpage), Integer.valueOf(endpage), httpUtil, Integer.valueOf(sucessNum), Integer.valueOf(sameNum));
            /*Thread thread = new Thread(testNum);
            thread.start();*/
            threadPool.execute(testNum);
        }
    }

    public static String getImageCode(HttpUtil httpUtil) throws IOException, InterruptedException {
        System.out.println("获取验证码...");
        long startTime = System.currentTimeMillis();
        byte[] result = httpUtil.doGetByte("http://shixin.court.gov.cn/image.jsp?date=" + System.currentTimeMillis(), null);
        ByteInputStream bin = new ByteInputStream();
        if (result == null) {
            getImageCode(httpUtil);
        }
        bin.setBuf(result);
        BufferedImage image = ImageIO.read(bin);
        String code = CheckNumber.getCheckNumber(image);
        Long codeTime = System.currentTimeMillis();
        System.out.println("获取验证码时间：" + (codeTime - startTime) / 1000);
        return code;
    }

    public static int getPageAccount(String cons) {
        int start = cons.indexOf("<input onclick=\"jumpTo()\" value=\"到\" type=\"button\" /> <input id=\"pagenum\" name=\"pagenum\" maxlength=\"6\" value=\"\" size=\"4\" type=\"text\" /> 页");
        int length = "<input onclick=\"jumpTo()\" value=\"到\" type=\"button\" /> <input id=\"pagenum\" name=\"pagenum\" maxlength=\"6\" value=\"\" size=\"4\" type=\"text\" /> 页".length();
        int end = cons.indexOf("条\n" +
                "\t\t</div>");
        int page = 0;
        try {
            page = Integer.parseInt(cons.substring(start + length, end).split("/")[1].split(" ")[0]);
        } catch (Exception e) {
            System.out.println(cons);
            e.printStackTrace();
        }
        return page;
    }

    public static List<String> getIDList(String cons) {
        ArrayList list = new ArrayList();
        try {
            Parser e = new Parser();
            e.setInputHTML(cons);
            e.setEncoding("utf-8");
            NodeFilter filter1 = new HasAttributeFilter("class", "View");
            NodeFilter filter2 = new TagNameFilter("a");
            AndFilter contentFilter = new AndFilter(filter1, filter2);
            NodeList nodes2 = e.extractAllNodesThatMatch(contentFilter);
            for (int i = 0; i < nodes2.size(); ++i) {
                LinkTag linkTag = (LinkTag) nodes2.elementAt(i);
                list.add(linkTag.getAttribute("id"));
            }
        } catch (Exception var9) {
            var9.printStackTrace();
        }

        return list;
    }

    public static List<String> getCardNum() {
        List list = new ArrayList();
        for (int i = 1; i <= 12; i++) {
            String mounth = String.valueOf(i);
            while (mounth.length() < 2) {
                mounth = "0" + mounth;
            }
            int maxDay;
            if (i == 2) {
                maxDay = 29;
            } else if (i == 4 || i == 6 || i == 9 || i == 11) {
                maxDay = 30;
            } else {
                maxDay = 31;
            }
            for (int j = 1; j <= maxDay; j++) {
                String day = String.valueOf(j);
                while (day.length() < 2) {
                    day = "0" + day;
                }
                String cardNum = mounth + day;
                list.add(cardNum);
            }
        }
        return list;
    }

    @Override
    public void run() {
        try {
            for (int i = stratPageNum; i <= endPageNum; i++) {
                System.out.println(Thread.currentThread().getName() + ":" + i);
                worker(i + "");
            }
            String logSql = "update cred_dishonesty_log set result = '1',dcurrentdate = sysdate where cardnum = '" + cardNum + "'";
            testConn.executeSave(logSql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void worker(String pageNum) {
        String idInfo = null;
        try {
            if (code == null) {
                code = getImageCode(httpUtil);
            }
            String sql = "insert into CRED_DISHONESTY_PERSON (IID, SINAME, SCARDNUM, SCASECODE, IAGE, SSEXY, SAREANAME, SCOURTNAME, DREGDATE," +
                    " SDUTY, SPERFORMANCE, SPERFORMEDPART, SUNPERFORMPART, SDISRUPTTYPENAME, DPUBLISHDATE, SPARTYTYPENAME, SGISTID, SGISTUNIT) " +
                    "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            String s = httpUtil.doPostString("http://shixin.court.gov.cn/findd",
                    "pName", "__", "pCardNum", "__________" + cardNum + "____", "pProvince", "0", "currentPage", pageNum, "pCode", code);
            if (s == null) {
                code = getImageCode(httpUtil);
                s = httpUtil.doPostString("http://shixin.court.gov.cn/findd",
                        "pName", "__", "pCardNum", "__________" + cardNum + "____", "pProvince", "0", "currentPage", pageNum, "pCode", code);
            }
            List arrayList = getIDList(s);
            if (arrayList == null || arrayList.size() == 0 || s.contains("验证码错误，请重新输入！")) {
                code = getImageCode(httpUtil);
                s = httpUtil.doPostString("http://shixin.court.gov.cn/findd",
                        "pName", "__", "pCardNum", "__________" + cardNum + "____", "pProvince", "0", "currentPage", pageNum, "pCode", code);
                arrayList = getIDList(s);
            }
            for (int i = 0; i < arrayList.size(); i++) {
                String queryIdSql = "select * from CRED_DISHONESTY_PERSON where iid = '" + arrayList.get(i) + "'";
                List resultlist = testConn.executeQueryForList(queryIdSql);
                if (resultlist != null && resultlist.size() > 0) {
                    sameNum++;
                    System.out.println("重复id:" + arrayList.get(i) + ",sameAccount:" + sameNum);
                    continue;
                }
                Map map = new HashMap();
                map.put("id", arrayList.get(i));
                map.put("pCode", code);
                idInfo = httpUtil.doGetString("http://shixin.court.gov.cn/findDetai", map);
                if (idInfo == null) {
                    i--;
                    continue;
                }
                JSONObject json = JSONObject.fromObject(idInfo);
                Integer iid = json.optInt("id");
                String siname = json.optString("iname");
                String scardnum = json.optString("cardNum");
                String scasecode = json.optString("caseCode");
                String sage = json.optString("age");
                Integer iage = Integer.valueOf(sage);
                String ssexy = json.optString("sexy");
                String sareaname = json.optString("areaName");
                String scourtname = json.optString("courtName");
                String sduty = json.optString("duty");
                String sperformance = json.optString("performance");
                String sperformedpart = json.optString("performedpart");
                String sunperformpart = json.optString("unperformpart");
                String sdisrupttypename = json.optString("disruptTypeName");
                String spublishdate = json.optString("publishDate");
                Date dpublishdate = DateUtil.StringToDate2(spublishdate);
                String spartytypename = json.optString("partyTypeName");
                String sgistid = json.optString("gistId");
                String sregdate = json.optString("regDate");
                Date dregdate = DateUtil.StringToDate2(sregdate);
                String sgistunit = json.optString("gistUnit");
                List list = new ArrayList();
                list.add(iid);
                list.add(siname);
                list.add(scardnum.replace("****", cardNum));
                list.add(scasecode);
                list.add(iage);
                list.add(ssexy);
                list.add(sareaname);
                list.add(scourtname);
                list.add(dregdate);

                StringReader reader = new StringReader(sduty);
                list.add(reader);

                list.add(sperformance);
                list.add(sperformedpart);
                list.add(sunperformpart);
                list.add(sdisrupttypename);
                list.add(dpublishdate);
                list.add(spartytypename);
                list.add(sgistid);
                list.add(sgistunit);
                testConn.psAdd(sql, list);
                sucessNum++;
            }
            String logStr = Thread.currentThread().getName() + ":查询条件：" + cardNum + ",当前页数：" + pageNum + "，总重复个数" + sameNum + "查询入库完成。";
            System.out.println(logStr);
            String logSql = "update cred_dishonesty_log set samenum = '" + sameNum + "',sucessnum='" + sucessNum + "', remark='" + logStr + "',startpage='" + pageNum + "',dcurrentdate = sysdate where cardnum = '" + cardNum + "'";
            testConn.executeSave(logSql);
        } catch (Exception e) {
            System.out.println(idInfo);
            e.printStackTrace();
        }

    }

}
