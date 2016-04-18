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
                return node instanceof Tag && !((Tag)node).isEndTag() && ((Tag)node).getTagName().equals("DIV") && ((Tag)node).getAttribute("class") != null && ((Tag)node).getAttribute("class").equals("w_box");
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

        while(it.hasMoreNodes()) {
            LinkTag node = (LinkTag)it.nextNode();
            String href = node.getLink();
            String title = node.getLinkText();
            logger.info("分析首页新闻【" + title + "】，链接地址【" + href + "】");

            try {
                if(!this.newsExist(title)) {
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
                    return node instanceof Tag && !((Tag)node).isEndTag() && ((Tag)node).getTagName().equals("DIV") && ((Tag)node).getAttribute("class") != null && ((Tag)node).getAttribute("class").equals("cs_content");
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

        while(it.hasMoreNodes()) {
            LinkTag node = (LinkTag)it.nextNode();
            logger.info("移除新闻内容中包含的文字、图片的链接【" + node.toHtml() + "】。");
            if(node.getLink().indexOf("cheshi.com") > -1) {
                content = content.replace(node.toHtml(), node.getStringText());
            }
        }

        logger.debug("==========removeTagA==============");
        logger.debug(content);
        return this.downloadImages(content, "D:\\autodata\\upload\\intersite", "upload/intersite");
    }

    public String downloadImages(String content, String uploadImgPath, String localhost) throws ParserException {
        File f = new File(uploadImgPath);
        if(!f.exists()) {
            f.mkdirs();
        }

        Parser myParser = new Parser(content);
        myParser.setEncoding("GBK");
        NodeList nodeList = myParser.extractAllNodesThatMatch(new TagNameFilter("img"));
        SimpleNodeIterator it = nodeList.elements();

        while(it.hasMoreNodes()) {
            Tag tag = (Tag)it.nextNode();
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
                while((bytesRead1 = is.read(buff, 0, buff.length)) != -1) {
                    fos.write(buff, 0, bytesRead1);
                }

                content = content.replace(src, localhost + "/" + filename);
            } catch (FileNotFoundException var26) {
                var26.printStackTrace();
            } catch (IOException var27) {
                var27.printStackTrace();
            } finally {
                try {
                    if(fos != null) {
                        fos.close();
                    }

                    if(is != null) {
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
            if(this.conn != null && !this.conn.isClosed()) {
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
                if(pstmt != null) {
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
                if(pstmt != null) {
                    pstmt.close();
                }
            } catch (SQLException var12) {
                var12.printStackTrace();
            }

        }

        return var4;
    }

    public static void main(String[] args) throws IOException, ParserException {
        Parser parser = new Parser("http://www.wdzj.com/dangan/phw/");
        parser.setEncoding("utf-8");
        AndFilter divFilter = new AndFilter(new TagNameFilter("div"), new HasAttributeFilter("class", "bd peopleBd"));
        HasParentFilter pFilter = new HasParentFilter(divFilter);
        TagNameFilter tagFilter = new TagNameFilter("ul");
        AndFilter andFilter = new AndFilter(tagFilter, pFilter);
        NodeList nodes = parser.extractAllNodesThatMatch(andFilter);
        String html = nodes.asHtml();
        System.out.println(html);
        System.out.println(html.substring(html.indexOf("<span class=\"name\">", 1) + 19, html.indexOf("</span></p>", html.indexOf("<span class=\"name\">", 1) + 19)));
        System.out.println(html.substring(html.indexOf("<span class=\"gray\">", 1) + 19, html.indexOf("</span></p>", html.indexOf("<span class=\"gray\">", 1) + 19)));
        System.out.println(html.substring(html.indexOf("<p>", html.indexOf("<span class=\"gray\">", 1) + 19) + 3, html.indexOf("</p>", html.indexOf("<span class=\"gray\">", 1) + 19) + 3));
    }

    public static Map<String, String> getData(String url, String content, NodeFilter nodeFilter) {
        HashMap map = new HashMap();

        try {
            Parser e;
            if(StringHelper.isNotEmpty(url)) {
                e = new Parser(url);
            } else {
                e = new Parser();
            }

            if(content != null) {
                e.setInputHTML(content);
            }

            e.setEncoding("utf-8");
            NodeList nodes = e.extractAllNodesThatMatch(nodeFilter);

            for(int divFilter = 1; divFilter < nodes.size(); ++divFilter) {
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

            for(int i = 1; i < componynodes.size(); ++i) {
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

    public static List<Map<String, String>> getLink(String url) {
        ArrayList list = new ArrayList();

        try {
            Parser e = new Parser(url);
            e.setEncoding("utf-8");
            NodeFilter[] filters = new NodeFilter[]{new AndFilter(new TagNameFilter("span"), new HasAttributeFilter("class", "name")), null, null};
            filters[1] = new HasParentFilter(filters[0]);
            filters[2] = new TagNameFilter("a");
            AndFilter contentFilter = new AndFilter(filters[1], filters[2]);
            NodeList nodes2 = e.extractAllNodesThatMatch(contentFilter);

            for(int i = 0; i < nodes2.size(); ++i) {
                HashMap map = new HashMap();
                LinkTag linkTag = (LinkTag)nodes2.elementAt(i);
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

        for(Iterator i$ = map.keySet().iterator(); i$.hasNext(); ++k) {
            String key = (String)i$.next();
            if(k == 0) {
                urlString.append("?");
            } else {
                urlString.append("&");
            }

            urlString.append(key).append("=").append((String)map.get(key));
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
            if(StringHelper.isNotEmpty(url)) {
                e = new Parser(url);
            } else {
                e = new Parser();
            }

            if(StringHelper.isNotEmpty(content)) {
                e.setInputHTML(content);
            }

            nodeList = e.extractAllNodesThatMatch(tableFilter);

            for(int i = 0; i <= nodeList.size(); ++i) {
                if(nodeList.elementAt(i) instanceof TableTag) {
                    TableTag tag = (TableTag)nodeList.elementAt(i);
                    TableRow[] rows = tag.getRows();

                    for(int j = 0; j < rows.length; ++j) {
                        ArrayList tdlist = new ArrayList();
                        TableRow tr = rows[j];
                        TableColumn[] td = tr.getColumns();

                        for(int k = 0; k < td.length; ++k) {
                            tdlist.add(td[k].toPlainTextString().trim());
                        }

                        if(tdlist.size() > 0) {
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
}