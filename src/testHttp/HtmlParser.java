//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package testHttp;

import org.apache.log4j.Logger;
import org.hibernate.annotations.common.util.StringHelper;
import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.Tag;
import org.htmlparser.filters.*;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.TableColumn;
import org.htmlparser.tags.TableRow;
import org.htmlparser.tags.TableTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.htmlparser.util.SimpleNodeIterator;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.*;
import java.util.*;

public class HtmlParser {
    private static Logger logger;
    private Connection conn = null;
    private static final String SiteName = "";

    public HtmlParser() {
    }

    public void indexNewsContent(String sitepath) throws Exception {
        Parser myParser = new Parser(sitepath);
        myParser.setEncoding("UTF-8");
        NodeList nodeList = myParser.extractAllNodesThatMatch(new NodeFilter() {
            public boolean accept(Node node) {
                return node instanceof Tag && !((Tag) node).isEndTag() && ((Tag) node).getTagName().equals("DIV") && ((Tag) node).getAttribute("class") != null && ((Tag) node).getAttribute("class").equals("w_box");
            }
        });
        Node node = nodeList.elementAt(1);
        logger.debug(node.toHtml());
        this.extractText(node.toHtml());
    }

    public void extractText(String inputHtml) throws Exception {
        Parser parser = Parser.createParser(inputHtml, "GBK");
        TagNameFilter filter = new TagNameFilter("a");
        NodeList nodeList = parser.extractAllNodesThatMatch(filter);
        SimpleNodeIterator it = nodeList.elements();
        this.getConnection();

        while (it.hasMoreNodes()) {
            LinkTag node = (LinkTag) it.nextNode();
            String href = node.getLink();
            String title = node.getLinkText();
            logger.info("分析首页新闻【" + title + "】，链接地址【" + href + "】");

            try {
                if (!this.newsExist(title)) {
                    this.insertDataBase(title, this.extractContent(href));
                } else {
                    logger.info("新闻【" + title + "】数据库中已经存在，忽略进入下一个新闻分析！");
                }
            } catch (SQLException var10) {
                logger.error("插入数据库新闻记录异常！" + var10.getMessage());
                var10.printStackTrace();
            } catch (Exception var11) {
                logger.error(var11.getMessage());
                logger.info("分析新闻【" + title + "】，链接地址【" + href + "】失败，进入下一个新闻分析。");
                var11.printStackTrace();
            }
        }

        this.closeConnection();
    }

    public String extractContent(String content) throws Exception {
        try {
            Parser pe = new Parser(content);
            pe.setEncoding("GBK");
            NodeList nodeList = pe.extractAllNodesThatMatch(new NodeFilter() {
                public boolean accept(Node node) {
                    return node instanceof Tag && !((Tag) node).isEndTag() && ((Tag) node).getTagName().equals("DIV") && ((Tag) node).getAttribute("class") != null && ((Tag) node).getAttribute("class").equals("cs_content");
                }
            });
            int size = nodeList.size();
            Node node = nodeList.elementAt(size - 1);
            content = node.toHtml();
            logger.debug("==========extractContent==============");
            logger.debug(content);
        } catch (Exception var6) {
            logger.error("分析新闻页面出现异常！" + var6.getMessage() + "原因可能出现于新闻页面不存在<div class=\"cs_content\"></div>标记。");
            throw var6;
        }

        return this.removeTagA(content);
    }

    public String removeTagA(String content) throws ParserException {
        Parser myParser = new Parser(content);
        myParser.setEncoding("GBK");
        NodeList nodeList = myParser.extractAllNodesThatMatch(new TagNameFilter("a"));
        SimpleNodeIterator it = nodeList.elements();

        while (it.hasMoreNodes()) {
            LinkTag node = (LinkTag) it.nextNode();
            logger.info("移除新闻内容中包含的文字、图片的链接【" + node.toHtml() + "】。");
            if (node.getLink().indexOf("cheshi.com") > -1) {
                content = content.replace(node.toHtml(), node.getStringText());
            }
        }

        logger.debug("==========removeTagA==============");
        logger.debug(content);
        return this.downloadImages(content, "D:\\autodata\\upload\\intersite", "upload/intersite");
    }

    public String downloadImages(String content, String uploadImgPath, String localhost) throws ParserException {
        File f = new File(uploadImgPath);
        if (!f.exists()) {
            f.mkdirs();
        }

        Parser myParser = new Parser(content);
        myParser.setEncoding("GBK");
        NodeList nodeList = myParser.extractAllNodesThatMatch(new TagNameFilter("img"));
        SimpleNodeIterator it = nodeList.elements();

        while (it.hasMoreNodes()) {
            Tag tag = (Tag) it.nextNode();
            String src = tag.getAttribute("src");
            String filename = src.substring(src.lastIndexOf("/") + 1);
            InputStream is = null;
            FileOutputStream fos = null;

            try {
                URL ioe = new URL(src);
                is = ioe.openStream();
                boolean bytesRead = false;
                byte[] buff = new byte[1024];
                fos = new FileOutputStream(uploadImgPath + "/" + filename);

                int bytesRead1;
                while ((bytesRead1 = is.read(buff, 0, buff.length)) != -1) {
                    fos.write(buff, 0, bytesRead1);
                }

                content = content.replace(src, localhost + "/" + filename);
            } catch (FileNotFoundException var26) {
                var26.printStackTrace();
            } catch (IOException var27) {
                var27.printStackTrace();
            } finally {
                try {
                    if (fos != null) {
                        fos.close();
                    }

                    if (is != null) {
                        is.close();
                    }
                } catch (IOException var25) {
                    var25.printStackTrace();
                }

            }
        }

        logger.debug("=================downloadImages==================");
        logger.debug(content);
        return content;
    }

    public void getConnection() {
        try {
            Class.forName("com.microsoft.jdbc.sqlserver.SQLServerDriver");
            String se = "jdbc:microsoft:sqlserver://192.168.99.188:12580;databaseName=Project2009;SelectMethod=cursor";
            String strUserName = "sa";
            String strPWD = "qsyjcsxdl@@@web2009@@@";
            this.conn = DriverManager.getConnection(se, strUserName, strPWD);
        } catch (ClassNotFoundException var4) {
            var4.printStackTrace();
        } catch (SQLException var5) {
            var5.printStackTrace();
        }

    }

    public void closeConnection() {
        try {
            if (this.conn != null && !this.conn.isClosed()) {
                this.conn.close();
            }
        } catch (SQLException var2) {
            var2.printStackTrace();
        }

    }

    public void insertDataBase(String newsTitle, String newsContent) throws SQLException {
        PreparedStatement pstmt = null;

        try {
            pstmt = this.conn.prepareStatement("INSERT INTO FumNews(NewsTitle, NewsContext, NewsState) values(?, ?, ?)");
            pstmt.setString(1, newsTitle);
            pstmt.setString(2, newsContent);
            pstmt.setInt(3, 1);
            pstmt.executeUpdate();
        } catch (SQLException var12) {
            throw var12;
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (SQLException var11) {
                var11.printStackTrace();
            }

        }

    }

    public boolean newsExist(String title) throws SQLException {
        PreparedStatement pstmt = null;

        boolean var4;
        try {
            pstmt = this.conn.prepareStatement("SELECT top 1 NewsId from FumNews where NewsTitle = ?");
            ResultSet e = pstmt.executeQuery();
            var4 = e.next();
        } catch (SQLException var13) {
            throw var13;
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (SQLException var12) {
                var12.printStackTrace();
            }

        }

        return var4;
    }

    public static void main(String[] args) throws IOException, ParserException, InterruptedException {
        System.out.println(login());
        for (int i = 1; i < 10; i++) {
            String url = "http://www.qixin.com/search/prov/LN_2114?page=1";
            HttpRespons hr = new TestHttp().send(url, "GET", null, null);
            String result = hr.getContent();
            System.out.println(getLink("", result));
        }
    }

    public static Map<String, String> getData(String url, String content, NodeFilter nodeFilter) {
        HashMap map = new HashMap();

        try {
            Parser e;
            if (StringHelper.isNotEmpty(url)) {
                e = new Parser(url);
            } else {
                e = new Parser();
            }

            if (content != null) {
                e.setInputHTML(content);
            }

            e.setEncoding("utf-8");
            NodeList nodes = e.extractAllNodesThatMatch(nodeFilter);

            for (int divFilter = 1; divFilter < nodes.size(); ++divFilter) {
                String divnodes = nodes.elementAt(divFilter).toPlainTextString().trim();
                String filters = nodes.elementAt(divFilter).getChildren().elementAt(0).toPlainTextString().trim();
                map.put(filters, divnodes);
            }

            AndFilter var14 = new AndFilter(new TagNameFilter("div"), new HasAttributeFilter("class", "bd aboutBd"));
            NodeList var15 = e.extractAllNodesThatMatch(var14);
            map.put("公司简介", var15.asString());
            NodeFilter[] var16 = new NodeFilter[]{new AndFilter(new TagNameFilter("span"), new HasAttributeFilter("class", "name")), null, null};
            var16[1] = new HasChildFilter(var16[0]);
            var16[2] = new TagNameFilter("p");
            NodeList componynodes = e.extractAllNodesThatMatch(nodeFilter);

            for (int i = 1; i < componynodes.size(); ++i) {
                String value = componynodes.elementAt(i).toPlainTextString().trim();
                String describe = componynodes.elementAt(i).getChildren().elementAt(0).toPlainTextString().trim();
                map.put(describe, value);
                System.out.println(map);
            }
        } catch (Exception var13) {
            var13.printStackTrace();
        }

        System.out.println(map);
        return map;
    }

    public static List<Map<String, String>> getLink(String url, String cons) {
        ArrayList list = new ArrayList();

        try {
            Parser e;
            if (StringHelper.isNotEmpty(url)) {
                e = new Parser(url);
            } else {
                e = new Parser();
                e.setInputHTML(cons);
            }

            e.setEncoding("utf-8");
            NodeFilter filter1 = new HasAttributeFilter("class", "search-result-title");
            NodeFilter filter2 = new TagNameFilter("a");
            AndFilter contentFilter = new AndFilter(filter1, filter2);
            NodeList nodes2 = e.extractAllNodesThatMatch(contentFilter);

            for (int i = 0; i < nodes2.size(); ++i) {
                HashMap map = new HashMap();
                LinkTag linkTag = (LinkTag) nodes2.elementAt(i);
                map.put("link", linkTag.getLink());
                map.put("linkText", linkTag.getLinkText());
                list.add(map);
            }
        } catch (Exception var9) {
            var9.printStackTrace();
        }

        return list;
    }

    public static String getUrl(String url, Map<String, String> map) {
        StringBuffer urlString = new StringBuffer(url);
        int k = 0;

        for (Iterator i$ = map.keySet().iterator(); i$.hasNext(); ++k) {
            String key = (String) i$.next();
            if (k == 0) {
                urlString.append("?");
            } else {
                urlString.append("&");
            }

            urlString.append(key).append("=").append((String) map.get(key));
        }

        return urlString.toString();
    }

    public static List testTable(String url, String content) {
        NodeList nodeList = null;
        ArrayList trlist = new ArrayList();
        NodeClassFilter tableFilter = new NodeClassFilter(TableTag.class);
        HasAttributeFilter cssSelectorNodeFilter = new HasAttributeFilter("instList-degree");
        new AndFilter(tableFilter, cssSelectorNodeFilter);

        try {
            Parser e;
            if (StringHelper.isNotEmpty(url)) {
                e = new Parser(url);
            } else {
                e = new Parser();
            }

            if (StringHelper.isNotEmpty(content)) {
                e.setInputHTML(content);
            }

            nodeList = e.extractAllNodesThatMatch(tableFilter);

            for (int i = 0; i <= nodeList.size(); ++i) {
                if (nodeList.elementAt(i) instanceof TableTag) {
                    TableTag tag = (TableTag) nodeList.elementAt(i);
                    TableRow[] rows = tag.getRows();

                    for (int j = 0; j < rows.length; ++j) {
                        ArrayList tdlist = new ArrayList();
                        TableRow tr = rows[j];
                        TableColumn[] td = tr.getColumns();

                        for (int k = 0; k < td.length; ++k) {
                            tdlist.add(td[k].toPlainTextString().trim());
                        }

                        if (tdlist.size() > 0) {
                            trlist.add(tdlist);
                        }
                    }
                }
            }
        } catch (Exception var16) {
            var16.printStackTrace();
            System.out.println(var16.getMessage());
        }

        return trlist;
    }

    public static String login() throws MalformedURLException, InterruptedException {
        //Thread.sleep(3000000);
        String htmlurl = "https://www.linkedin.com/uas/login-submit";
        HttpURLConnection httpConn = null;
        String cookie = "";
        try {
            URL url = new URL(htmlurl);

            httpConn = (HttpURLConnection) url.openConnection();

            HttpURLConnection.setFollowRedirects(true);
            httpConn.setRequestMethod("POST");
            httpConn.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36");
            httpConn.setRequestProperty("Connection", "keep-alive");
            httpConn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml");
            httpConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            httpConn.setRequestProperty("Cache-control", "no-cache, no-store");
            httpConn.setRequestProperty("Host", "www.linkedin.com");
            //httpConn.setRequestProperty("Referer","https://www.linkedin.com/uas/login?session_redirect=http://www.linkedin.com/profile/view?id=222323610&authType=name&authToken=fcEe");
            //post方法，重定向设置
            httpConn.setDoOutput(true);
            httpConn.setDoInput(true);
            httpConn.setUseCaches(false);
            httpConn.setInstanceFollowRedirects(true);
            //写入，post方法必须用流写入的方式传输数据
            StringBuffer str_buf = new StringBuffer(4096);
            OutputStream os = httpConn.getOutputStream();
            str_buf.append("session_key").append("=335627901").append("email").append("&");
            str_buf.append("session_password").append("=").append("gmail").append("&");
            //str_buf.append("session_redirect").append("=").append(redictURL);
            os.write(str_buf.toString().getBytes());
            os.flush();
            os.close();
            httpConn.setConnectTimeout(20000);
            httpConn.setReadTimeout(20000);
            //获取重定向和cookie
            //String redictURL= httpConn.getHeaderField( "Location" );
            //System.out.println("第一次请求重定向地址 location="+redictURL);

            //获取cookie
            Map<String, List<String>> map = httpConn.getHeaderFields();
            //System.out.println(map.toString());
            Set<String> set = map.keySet();
            for (Iterator<String> iterator = set.iterator(); iterator.hasNext(); ) {
                String key = iterator.next();
                if (key != null) {
                    if (key.equals("Set-Cookie")) {
                        System.out.println("key=" + key + ",开始获取cookie");
                        List<String> list = map.get(key);
                        for (String str : list) {
                            String temp = str.split("=")[0];
                            //System.out.println(temp);
                            //cookie包含到信息非常多，调试发现登录只需这条信息
                            if (temp.equals("li_at")) {
                                cookie = str;
                                return cookie;
                            }

                        }
                    }
                }

            }
            httpConn.disconnect();

        } catch (final MalformedURLException me) {
            System.out.println("url不存在!");
            me.getMessage();
            throw me;
        } catch (final FileNotFoundException me) {
            System.out.println(htmlurl + "反爬启动");
            return "0";
        } catch (final IOException e) {
            e.printStackTrace();
            System.out.println("反爬启动:" + htmlurl + "次数:");
            httpConn.disconnect();
            Thread.sleep(20000);
            return login();
        }

        return cookie;
    }
}
