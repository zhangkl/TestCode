/*******************************************************************************
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 ******************************************************************************/

package testHtmlUtil;

import com.gargoylesoftware.htmlunit.CookieManager;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import dishonest.dao.TestConn;

import java.io.IOException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.List;

public class TestQXB implements Runnable {
    static int startPage = 0;
    static int endPage = 0;
    static int sucessCount = 0;
    static TestConn testConn;
    static Statement statement;
    static String url;

    public TestQXB(int startPage, int endPage, int sucessCount, TestConn testConn, Statement statement, String url) {
        TestQXB.startPage = startPage;
        TestQXB.endPage = endPage;
        TestQXB.sucessCount = sucessCount;
        TestQXB.testConn = testConn;
        TestQXB.statement = statement;
        TestQXB.url = url;
    }

    public static void main(String[] args) throws Exception {
        TestQXB testQXB = new TestQXB(startPage, endPage, sucessCount, testConn, statement, url);
    }

    @Override
    public void run() {
        String str;
        //创建一个webclient
        WebClient webClient = new WebClient();
        //htmlunit 对css和javascript的支持不好，所以请关闭之
        webClient.getOptions().setJavaScriptEnabled(false);
        webClient.getOptions().setCssEnabled(false);
        Cookie cookie = new Cookie("www.qixin.com", "login_returnurl", "http%3A//www.qixin.com/search/prov/SH%3Fpage%3D2");
        Cookie cookie1 = new Cookie("www.qixin.com", "userKey", "QXBAdmin-Web2.0_5tUrhr/6EVtLT+GVfE+vU8k330y+oPICCM6jhUGEoLc%3D");
        Cookie cookie2 = new Cookie("www.qixin.com", "userValue", "4a68111b-0cfa-457f-91bd-b6fda97fa524");
        Cookie cookie3 = new Cookie("www.qixin.com", "gr_session_id_955c17a7426f3e98", "d25fe84e-fb1d-4ef8-8b4e-b530e5004b30");
        Cookie cookie4 = new Cookie("www.qixin.com", "_alicdn_sec", "5732cf53d99e48a838049be355d47a44000895ae");
        CookieManager cookieManager = new CookieManager();
        cookieManager.addCookie(cookie);
        cookieManager.addCookie(cookie2);
        cookieManager.addCookie(cookie3);
        cookieManager.addCookie(cookie1);
        cookieManager.addCookie(cookie4);
        webClient.setCookieManager(cookieManager);
        //获取页面
        HtmlPage page = null;
        try {
            page = webClient.getPage("http://www.qixin.com/search/prov/SH?page=20");
        } catch (IOException e) {
            e.printStackTrace();
        }
        //获取页面的XML代码
        List<HtmlAnchor> hbList = (List<HtmlAnchor>) page.getByXPath("//a");
        Iterator iterator = hbList.iterator();
        while (iterator.hasNext()) {
            HtmlAnchor ha = (HtmlAnchor) iterator.next();
            if ("search-result-title".equals(ha.getAttribute("class"))) {
                System.out.println(ha.asText());
                System.out.println("http://www.qixin.com" + ha.getAttribute("href"));
            }
        }
        //关闭webclient
        webClient.close();
    }
}