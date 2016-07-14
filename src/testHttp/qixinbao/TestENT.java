/*******************************************************************************
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 ******************************************************************************/

package testHttp.qixinbao;

import com.dishonest.dao.TestConn;
import com.gargoylesoftware.htmlunit.CookieManager;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import com.gargoylesoftware.htmlunit.util.Cookie;

import java.io.StringReader;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TestENT {
    public static void main(String[] args) throws Exception {
        try {
            String getDateSql = "select * from (select rownum r,t.* from cred_ent_url t where remark is null) a where a.r > ? and a.r <= ?";
            for (int i = 0; i < 1; i++) {
                TestConn testConn = TestConn.getInstance();
                PreparedStatement ps = testConn.creatPStatement(getDateSql);
                ps.setInt(1, i * 300);
                ps.setInt(2, (i + 1) * 300);
                ResultSet resultSet = ps.executeQuery();
                GetENTDate getENTDate = new GetENTDate(0, testConn, testConn.creatPStatement("update cred_ent_url set GSINFO = ?, GDINFO = ?, ZYINFO = ?,REMARK = ? where enturl =?"), resultSet);
                Thread thread = new Thread(getENTDate);
                thread.setName("Thread-" + i);
                thread.start();
                System.out.println(thread.getName() + "启动...");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

class GetENTDate extends Thread {
    static boolean flag = false;
    int sucessCount = 0;
    TestConn testConn;
    Statement statement;
    String url;
    ResultSet resultSet;


    public GetENTDate(int sucessCount, TestConn testConn, Statement statement, ResultSet resultSet) {
        this.sucessCount = sucessCount;
        this.testConn = testConn;
        this.statement = statement;
        this.resultSet = resultSet;
    }


    public void run() {
        String str;
        //创建一个webclient
        WebClient webClient = new WebClient();
        //htmlunit 对css和javascript的支持不好，所以请关闭之
        webClient.getOptions().setJavaScriptEnabled(false);
        webClient.getOptions().setCssEnabled(false);
        Cookie cookie1 = new Cookie("www.qixin.com", "userKey", "QXBAdmin-Web2.0_5tUrhr/6EVtLT+GVfE+vU8k330y+oPICCM6jhUGEoLc%3D");
        Cookie cookie2 = new Cookie("www.qixin.com", "userValue", "29c473f0-8995-94c8-fe24-711fbc9b9345");
        CookieManager cookieManager = new CookieManager();
        cookieManager.addCookie(cookie2);
        cookieManager.addCookie(cookie1);
        webClient.setCookieManager(cookieManager);
        //获取页面
        HtmlPage page = null;
        try {
            while (resultSet.next()) {
                try {
                    String url = resultSet.getString("enturl");
                    //System.out.println(page.asText());
                    //获取页面的XML代码
                    page = webClient.getPage(url);
                    String context = page.asText();
                    if (context.contains("系统繁忙")) {
                        System.out.println("系统繁忙......");
                        continue;
                    }
                    synchronized (TestENT.class) {
                        if (context.contains("您当日的访问次数过多") || context.contains("想要了解企业最新动态？")) {
                            System.out.println(System.currentTimeMillis() + ":" + Thread.currentThread().getName() + "进入同步代码。" + flag);
                            if (flag) {
                                continue;
                            }
                            flag = true;
                            if (context.contains("您当日的访问次数过多") || context.contains("想要了解企业最新动态？")) {
                                Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
                                Thread.sleep(5000);
                            }
                            System.out.println(System.currentTimeMillis() + ":" + Thread.currentThread().getName() + "结束同步代码。" + flag);
                            continue;
                        }
                    }
                    flag = false;
                    DomNodeList<DomElement> htmlTable = page.getElementsByTagName("table");
                    String GSINFO = "";
                    String GDINFO = "";
                    String ZYINFO = "";
                    if (htmlTable.size() > 0) {
                        HtmlTable entInfo = (HtmlTable) htmlTable.get(0);
                        GSINFO = entInfo.asXml();
                    }
                    if (htmlTable.size() > 1) {
                        HtmlTable gdInfo = (HtmlTable) htmlTable.get(1);
                        GDINFO = gdInfo.asXml();
                    }
                    DomNodeList<DomElement> ulList = page.getElementsByTagName("ul");
                    if (ulList.size() > 3) {
                        HtmlUnorderedList htmlUl = (HtmlUnorderedList) ulList.get(3);
                        ZYINFO = htmlUl.asXml();
                    }

                    PreparedStatement ps = (PreparedStatement) statement;
                    StringReader reader = new StringReader(GSINFO);
                    ps.setCharacterStream(1, reader, GSINFO.length());
                    reader = new StringReader(GDINFO);
                    ps.setCharacterStream(2, reader, GDINFO.length());
                    reader = new StringReader(ZYINFO);
                    ps.setCharacterStream(3, reader, ZYINFO.length());
                    ps.setString(4, "插入完成");
                    ps.setString(5, url);
                    ps.execute();
                    sucessCount++;
                    System.out.println(Thread.currentThread().getName() + ":" + sucessCount);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            System.out.println(Thread.currentThread());
            e.printStackTrace();
        }

        //关闭webclient
        webClient.close();
    }

}