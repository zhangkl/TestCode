/*******************************************************************************
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 ******************************************************************************/

package testHttp.helidai;

import com.dishonest.dao.TestConn;
import testHttp.httpUtil.HtmlParser;
import testHttp.httpUtil.HttpRespons;
import testHttp.httpUtil.TestHttp;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: zhangkl
 * Date: 16-3-30
 * Time: 下午2:13
 * To change this template use File | Settings | File Templates.
 */
public class GetHLD extends Thread {

    int startPage;
    int endPage;
    int line;
    TestConn testConn;
    Statement statement;
    Statement statement2;

    public GetHLD(int startPage, int endPage, int line, TestConn testConn, Statement statement, Statement statement2) {
        this.startPage = startPage;
        this.endPage = endPage;
        this.line = line;
        this.testConn = testConn;
        this.statement = statement;
        this.statement2 = statement2;
    }

    public static void main(String[] args) throws IOException {
        int j = 1;
        for (int i = 1; i < 211; i += 19) {
            TestConn testConn = TestConn.getInstance();
            GetHLD getHLD = new GetHLD(i, i + 19, 0, testConn, testConn.creatStatement(), testConn.creatStatement());
            System.out.println(getHLD.startPage + "," + getHLD.endPage);
            getHLD.setName("Thread" + j);
            getHLD.start();
            j++;
        }

    }

    public void run() {
        try {
            TestHttp testHttp = new TestHttp();
            Map map = new HashMap();
            int j;
            for (int i = startPage; i < endPage; i++) {
                String url = "http://www.helloan.cn/process/lend/bids?pageNow=" + i + "&isFromPageBtnB=Y";
                HttpRespons hr = testHttp.send(url, "GET", map, map);
                String result = hr.getContent();
                List<List> list = HtmlParser.testTable("", result);
                int start = result.indexOf("<span class=\"instList-produceType\">", 0);
                j = 0;
                while (start != -1) {
                    start = result.indexOf("<a href=\"", start);
                    start = start + 9;
                    url = result.substring(start, result.indexOf("\"", start));
                    String res = testHttp.send(url, "GET", null, null).getContent();
                    getPersonInfo(res, list,j);
                    j++;
                    line++;
                    start = result.indexOf("<span class=\"instList-produceType\">", start);
                    System.out.println(Thread.currentThread().getName() +"：" + i + ":" + line);
                }
                if (i >= 210) {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getPersonInfo(String res,List<List> list,int j) throws SQLException {
        StringBuffer sql = new StringBuffer("insert into cred_wdpt_borrower_bak " +
                "(IID, SBORROWNAME, SSEX, SWORKCITY, SPOSITION, SSCHOOL, SCOMPANY, SAGE, SNATURE, SINCOME, SDEGREE, SIDENTITYCODE, SMARRIAGESTATE, " +
                "SINDUSTRY, SWORKTIME, SBORROWCODE, SP2PORGCODE, DGETDATE, SREMARK, ILOANSUM) values (");
        int start = res.indexOf("<div role=\"tabpanel\" class=\"tab-pane tab-bd bl-df br-df bb-df hao-bg \" id=\"carLTabBorrInfo\">");
        sql.append(getSeqNextVal("SEQ_CRED_WDPT_BORROWER")+",");
        for (int i = 0; i < 14; i++) {
            start = res.indexOf("<div class=\"col-xs-", start) + 10;
            start = res.indexOf("<div class=\"col-xs-", start);
            start = res.indexOf(">", start) + 1;
            String value = res.substring(start, res.indexOf("<", start));
            if (value == null || "".equals(value)) {
                sql.append("'',");
            }else
                sql.append("'"+value+"',");

        }
        sql.append("'',");//SBORROWCODE
        sql.append("'Q10152900H1200',");//SP2PORGCODE
        sql.append("sysdate,");//DGETDATE
        sql.append("'',");//SREMARK
        String value = list.get(j).get(3).toString().trim();
        sql.append("'"+value + "')");//ILOANSUM
        System.out.println(sql);
        statement.execute(sql.toString());
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
