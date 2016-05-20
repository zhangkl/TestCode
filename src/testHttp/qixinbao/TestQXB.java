package testHttp.qixinbao;

import com.gargoylesoftware.htmlunit.CookieManager;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import testHttp.dao.TestConn;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

public class TestQXB extends Thread {
    int startPage = 0;
    int endPage = 0;
    int sucessCount = 0;
    TestConn testConn;
    PreparedStatement statement;
    String url;
    String area;

    public TestQXB(int startPage, int endPage, int sucessCount, TestConn testConn, PreparedStatement statement, String url, String area) {
        this.startPage = startPage;
        this.endPage = endPage;
        this.sucessCount = sucessCount;
        this.testConn = testConn;
        this.statement = statement;
        this.url = url;
        this.area = area;
    }

    public static void main(String[] args) throws Exception {
        try {
            TestConn testConn = new TestConn();
            String querySql = "select * from cred_qixinbao_url where remark is null";
            ResultSet resultSet = testConn.executeQuery(querySql);
            while (resultSet.next()) {
                String queryurl = resultSet.getString("enturl");
                String queryArea = resultSet.getString("area");
                TestQXB testQXB = new TestQXB(0, 20, 0, testConn, testConn.creatPStatement("insert into cred_ent_url (ENTURL, ENTNAME, AREA) values (?,?,?)"), queryurl, queryArea);
                Thread thread = new Thread(testQXB);
                thread.run();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void run() {
        String str;
        //创建一个webclient
        WebClient webClient = new WebClient();
        //htmlunit 对css和javascript的支持不好，所以请关闭之
        webClient.getOptions().setJavaScriptEnabled(false);
        webClient.getOptions().setCssEnabled(false);
        Cookie cookie = new Cookie("www.qixin.com", "login_returnurl", "http%3A//www.qixin.com/search/prov/SH%3Fpage%3D2");
        Cookie cookie1 = new Cookie("www.qixin.com", "userKey", "QXBAdmin-Web2.0_T%2B89WeYDCIYtH5CFmbxdiJYjBjHfJDZEYSRRlXjge74%3D");
        Cookie cookie2 = new Cookie("www.qixin.com", "userValue", "cc8864fb-1922-100a-e0cc-c5c489f15275");
        Cookie cookie3 = new Cookie("www.qixin.com", "gr_session_id_955c17a7426f3e98", "4357de90-6317-4949-9c31-ffc1d0f83a2d");
        CookieManager cookieManager = new CookieManager();
        cookieManager.addCookie(cookie);
        cookieManager.addCookie(cookie2);
        cookieManager.addCookie(cookie3);
        cookieManager.addCookie(cookie1);
        webClient.setCookieManager(cookieManager);
        //获取页面
        HtmlPage page = null;
        for (int i = startPage; i <= endPage; i++) {
            try {
                page = webClient.getPage(url + "?page=" + i);
                //获取页面的XML代码
                List<HtmlAnchor> hbList = (List<HtmlAnchor>) page.getByXPath("//a");
                String context = page.asText();
                if (context.contains("您当日的访问次数过多")) {
                    System.out.println("您当日的访问次数过多，账户已被锁定，请点击");
                    System.out.println("您当日的访问次数过多，账户已被锁定，请点击");
                    System.out.println("您当日的访问次数过多，账户已被锁定，请点击");
                    System.out.println("您当日的访问次数过多，账户已被锁定，请点击");
                    Thread.sleep(1000L);
                    i--;
                    continue;
                }
                Iterator iterator = hbList.iterator();
                while (iterator.hasNext()) {
                    HtmlAnchor ha = (HtmlAnchor) iterator.next();
                    if ("search-result-title".equals(ha.getAttribute("class"))) {
                        String name = ha.asText();
                        String urlstr = "http://www.qixin.com" + ha.getAttribute("href");
                        statement.setString(1, urlstr);
                        statement.setString(2, name);
                        statement.setString(3, area);
                        statement.execute();
                        sucessCount++;
                        System.out.println(url + ":" + i + ":" + sucessCount);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String sql = "update cred_qixinbao_url set remark = '已完成" + sucessCount + "'where enturl = '" + url + "'";
        try {
            testConn.creatStatement().execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //关闭webclient
        webClient.close();
    }
}