//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package testHttp.httpUtil;

import org.apache.log4j.Logger;
import org.hibernate.annotations.common.util.StringHelper;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.*;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.TableColumn;
import org.htmlparser.tags.TableRow;
import org.htmlparser.tags.TableTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import java.io.IOException;
import java.sql.Connection;
import java.util.*;

public class HtmlParser {
    private static Logger logger;
    private Connection conn = null;
    private static final String SiteName = "";

    public HtmlParser() {
    }

    public static void main(String[] args) throws IOException, ParserException, InterruptedException {
        String s = "\n" +
                "\n" +
                "\n" +
                "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
                "\n" +
                "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
                "<head>\n" +
                "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\n" +
                "<title>全国法院失信被执行人名单信息公布与查询</title>\n" +
                "<link href=\"/static/style/style.css\" type=\"text/css\" rel=\"stylesheet\" media=\"screen, projection\" />\n" +
                "<script language=\"javascript\" type=\"text/javascript\" src=\"/static/javascript/jquery-latest.js\"></script>\n" +
                "<script language=\"javascript\" type=\"text/javascript\" src=\"/static/javascript/search.js\"></script>\n" +
                "<script type=\"text/javascript\" type=\"text/javascript\" src=\"/static/javascript/visit.js\"></script>\n" +
                "<script>\n" +
                "var contextPath = '';\n" +
                "var totalPage = 1084;\n" +
                "</script>\n" +
                "</head>\n" +
                "<body>\n" +
                "\t<div style=\"padding: 14px 60px;\" id=\"ResultlistBlock\">\n" +
                "\t\t<h4 style=\"text-align: left; padding: 8px; margin: 0;\">\n" +
                "\t\t\t对<span style=\"color: red; padding: 4px;\">\"__\"</span>的查询结果：\n" +
                "\t\t</h4>\n" +
                "\t\t<table width=\"100%\" border=\"0\" align=\"center\" cellpadding=\"3\"\n" +
                "\t\t\tcellspacing=\"0\" class=\"Resultlist\" id=\"Resultlist\">\n" +
                "\t\t\t<tbody>\n" +
                "\t\t\t\t<tr>\n" +
                "\t\t\t\t\t<th width=\"30\" align=\"center\">序号</th>\n" +
                "\t\t\t\t\t<th nowrap=\"nowrap\" align=\"center\">被执行人姓名/名称</th>\n" +
                "\t\t\t\t\t<th width=\"120\" align=\"center\">立案时间</th>\n" +
                "\t\t\t\t\t<th nowrap=\"nowrap\" align=\"center\">案号</th>\n" +
                "<!-- \t\t\t\t\t<th width=\"60\" align=\"center\">关注</th> -->\n" +
                "\t\t\t\t\t<th width=\"60\" align=\"center\">查看</th>\n" +
                "\t\t\t\t</tr>\n" +
                "\n" +
                "\t\t\t\t\n" +
                "\t\t\t\t<tr style=\"height:28px;\">\n" +
                "\t\t\t\t\t<td align=\"center\">1</td>\n" +
                "\t\t\t\t\t<td nowrap=\"nowrap\" align=\"left\"><a title=\"吴缨\">吴缨</a></td>\n" +
                "\t\t\t\t\t<td align=\"center\">2016年06月06日</td>\n" +
                "\t\t\t\t\t<td nowrap=\"nowrap\" align=\"left\">(2016)京0105执9239号</td>\n" +
                "\n" +
                "\t\t\t\t\t<td align=\"center\">[<a href=\"javascript:void(0);\" class=\"View\" id=\"4651433\">查看</a>]\n" +
                "\t\t\t\t\t</td>\n" +
                "\t\t\t\t</tr>\n" +
                "\t\t\t\t\n" +
                "\t\t\t\t<tr style=\"height:28px;\">\n" +
                "\t\t\t\t\t<td align=\"center\">2</td>\n" +
                "\t\t\t\t\t<td nowrap=\"nowrap\" align=\"left\"><a title=\"林雄\">林雄</a></td>\n" +
                "\t\t\t\t\t<td align=\"center\">2016年06月06日</td>\n" +
                "\t\t\t\t\t<td nowrap=\"nowrap\" align=\"left\">(2016)闽0102执01774号</td>\n" +
                "\n" +
                "\t\t\t\t\t<td align=\"center\">[<a href=\"javascript:void(0);\" class=\"View\" id=\"4730119\">查看</a>]\n" +
                "\t\t\t\t\t</td>\n" +
                "\t\t\t\t</tr>\n" +
                "\t\t\t\t\n" +
                "\t\t\t\t<tr style=\"height:28px;\">\n" +
                "\t\t\t\t\t<td align=\"center\">3</td>\n" +
                "\t\t\t\t\t<td nowrap=\"nowrap\" align=\"left\"><a title=\"方冠人\">方冠人</a></td>\n" +
                "\t\t\t\t\t<td align=\"center\">2016年06月06日</td>\n" +
                "\t\t\t\t\t<td nowrap=\"nowrap\" align=\"left\">(2016)湘0421执326号</td>\n" +
                "\n" +
                "\t\t\t\t\t<td align=\"center\">[<a href=\"javascript:void(0);\" class=\"View\" id=\"4536932\">查看</a>]\n" +
                "\t\t\t\t\t</td>\n" +
                "\t\t\t\t</tr>\n" +
                "\t\t\t\t\n" +
                "\t\t\t\t<tr style=\"height:28px;\">\n" +
                "\t\t\t\t\t<td align=\"center\">4</td>\n" +
                "\t\t\t\t\t<td nowrap=\"nowrap\" align=\"left\"><a title=\"兰瑞\">兰瑞</a></td>\n" +
                "\t\t\t\t\t<td align=\"center\">2016年06月06日</td>\n" +
                "\t\t\t\t\t<td nowrap=\"nowrap\" align=\"left\">(2016)豫1527执349号</td>\n" +
                "\n" +
                "\t\t\t\t\t<td align=\"center\">[<a href=\"javascript:void(0);\" class=\"View\" id=\"4451670\">查看</a>]\n" +
                "\t\t\t\t\t</td>\n" +
                "\t\t\t\t</tr>\n" +
                "\t\t\t\t\n" +
                "\t\t\t\t<tr style=\"height:28px;\">\n" +
                "\t\t\t\t\t<td align=\"center\">5</td>\n" +
                "\t\t\t\t\t<td nowrap=\"nowrap\" align=\"left\"><a title=\"林明星\">林明星</a></td>\n" +
                "\t\t\t\t\t<td align=\"center\">2016年06月06日</td>\n" +
                "\t\t\t\t\t<td nowrap=\"nowrap\" align=\"left\">(2016)闽0921执00509号</td>\n" +
                "\n" +
                "\t\t\t\t\t<td align=\"center\">[<a href=\"javascript:void(0);\" class=\"View\" id=\"4529774\">查看</a>]\n" +
                "\t\t\t\t\t</td>\n" +
                "\t\t\t\t</tr>\n" +
                "\t\t\t\t\n" +
                "\t\t\t\t<tr style=\"height:28px;\">\n" +
                "\t\t\t\t\t<td align=\"center\">6</td>\n" +
                "\t\t\t\t\t<td nowrap=\"nowrap\" align=\"left\"><a title=\"刘志远\">刘志远</a></td>\n" +
                "\t\t\t\t\t<td align=\"center\">2016年06月06日</td>\n" +
                "\t\t\t\t\t<td nowrap=\"nowrap\" align=\"left\">(2016)冀1182执257号</td>\n" +
                "\n" +
                "\t\t\t\t\t<td align=\"center\">[<a href=\"javascript:void(0);\" class=\"View\" id=\"4470961\">查看</a>]\n" +
                "\t\t\t\t\t</td>\n" +
                "\t\t\t\t</tr>\n" +
                "\t\t\t\t\n" +
                "\t\t\t\t<tr style=\"height:28px;\">\n" +
                "\t\t\t\t\t<td align=\"center\">7</td>\n" +
                "\t\t\t\t\t<td nowrap=\"nowrap\" align=\"left\"><a title=\"王道稽\">王道稽</a></td>\n" +
                "\t\t\t\t\t<td align=\"center\">2016年06月06日</td>\n" +
                "\t\t\t\t\t<td nowrap=\"nowrap\" align=\"left\">(2016)闽0982执00688号</td>\n" +
                "\n" +
                "\t\t\t\t\t<td align=\"center\">[<a href=\"javascript:void(0);\" class=\"View\" id=\"4525986\">查看</a>]\n" +
                "\t\t\t\t\t</td>\n" +
                "\t\t\t\t</tr>\n" +
                "\t\t\t\t\n" +
                "\t\t\t\t<tr style=\"height:28px;\">\n" +
                "\t\t\t\t\t<td align=\"center\">8</td>\n" +
                "\t\t\t\t\t<td nowrap=\"nowrap\" align=\"left\"><a title=\"陈夫柱\">陈夫柱</a></td>\n" +
                "\t\t\t\t\t<td align=\"center\">2016年06月06日</td>\n" +
                "\t\t\t\t\t<td nowrap=\"nowrap\" align=\"left\">(2016)苏1302执1827号</td>\n" +
                "\n" +
                "\t\t\t\t\t<td align=\"center\">[<a href=\"javascript:void(0);\" class=\"View\" id=\"4420400\">查看</a>]\n" +
                "\t\t\t\t\t</td>\n" +
                "\t\t\t\t</tr>\n" +
                "\t\t\t\t\n" +
                "\t\t\t\t<tr style=\"height:28px;\">\n" +
                "\t\t\t\t\t<td align=\"center\">9</td>\n" +
                "\t\t\t\t\t<td nowrap=\"nowrap\" align=\"left\"><a title=\"李从江\">李从江</a></td>\n" +
                "\t\t\t\t\t<td align=\"center\">2016年06月06日</td>\n" +
                "\t\t\t\t\t<td nowrap=\"nowrap\" align=\"left\">(2016)豫1422执426号</td>\n" +
                "\n" +
                "\t\t\t\t\t<td align=\"center\">[<a href=\"javascript:void(0);\" class=\"View\" id=\"4399298\">查看</a>]\n" +
                "\t\t\t\t\t</td>\n" +
                "\t\t\t\t</tr>\n" +
                "\t\t\t\t\n" +
                "\t\t\t\t<tr style=\"height:28px;\">\n" +
                "\t\t\t\t\t<td align=\"center\">10</td>\n" +
                "\t\t\t\t\t<td nowrap=\"nowrap\" align=\"left\"><a title=\"林雪勇\">林雪勇</a></td>\n" +
                "\t\t\t\t\t<td align=\"center\">2016年06月03日</td>\n" +
                "\t\t\t\t\t<td nowrap=\"nowrap\" align=\"left\">(2016)闽0213执00769号</td>\n" +
                "\n" +
                "\t\t\t\t\t<td align=\"center\">[<a href=\"javascript:void(0);\" class=\"View\" id=\"4547130\">查看</a>]\n" +
                "\t\t\t\t\t</td>\n" +
                "\t\t\t\t</tr>\n" +
                "\t\t\t\t\n" +
                "\t\t\t</tbody>\n" +
                "\t\t</table>\n" +
                "\t\t<div align=\"right\">\n" +
                "\t\t\t\n" +
                "\t\t\t\t<a href=\"javascript:void(0);\" onclick=\"gotoPage(1)\">首页</a>\n" +
                "\t\t\t\t<a href=\"javascript:void(0);\" onclick=\"gotoPage(9)\">上一页 </a>\n" +
                "\t\t\t\n" +
                "\t\t\t\n" +
                "\t\t\t\n" +
                "\t\t\t\t<a href=\"javascript:void(0);\" onclick=\"gotoPage(11)\">下一页</a>\n" +
                "\t\t\t\t<a href=\"javascript:void(0);\" onclick=\"gotoPage(1084)\">尾页</a>\n" +
                "\t\t\t\n" +
                "\t\t\t\n" +
                "\t\t\t\n" +
                "\t\t\t<input onclick=\"jumpTo()\" value=\"到\" type=\"button\" /> <input id=\"pagenum\" name=\"pagenum\" maxlength=\"6\" value=\"\" size=\"4\" type=\"text\" /> 页 10/1084 共10838条\n" +
                "\t\t</div>\n" +
                "\t</div>\n" +
                "\t<form id=\"searchForm\" action=\"/findd\" method=\"post\">\n" +
                "\t\t<input type=\"hidden\" id=\"currentPage\" name=\"currentPage\" value=\"10\"/>\n" +
                "\t\t<input type=\"hidden\" id=\"pName\" name=\"pName\" value=\"__\"/>\n" +
                "        <input type=\"hidden\" id=\"pCardNum\" name=\"pCardNum\" value=\"__________1111____\"/>\n" +
                "        <input type=\"hidden\" id=\"pProvince\" name=\"pProvince\" value=\"0\"/>\n" +
                "\t\t<input type=\"hidden\" id=\"pCode\" name=\"pCode\" />\n" +
                "\t</form>\t\t\t\t\n" +
                "</body>\n" +
                "</html>\n" +
                "\n";
        System.out.println(getLink("", s));
        getPage("", s);
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

    public static int getPage(String url, String cons) {
        int start = cons.indexOf("<input onclick=\"jumpTo()\" value=\"到\" type=\"button\" /> <input id=\"pagenum\" name=\"pagenum\" maxlength=\"6\" value=\"\" size=\"4\" type=\"text\" /> 页");
        int length = "<input onclick=\"jumpTo()\" value=\"到\" type=\"button\" /> <input id=\"pagenum\" name=\"pagenum\" maxlength=\"6\" value=\"\" size=\"4\" type=\"text\" /> 页".length();
        int end = cons.indexOf("条\n" +
                "\t\t</div>");
        int page = Integer.parseInt(cons.substring(start + length, end).split("/")[1].split(" ")[0]);
        System.out.println(page);
        return page;
    }


    public static List<Map<String, String>> getLink(String url, String cons) {
        ArrayList list = new ArrayList();

        try {
            Parser e;
            if (StringHelper.isNotEmpty(url)) {
                e = new Parser(url);
                System.out.println(e.toString());
            } else {
                e = new Parser();
                e.setInputHTML(cons);
            }

            e.setEncoding("utf-8");
            NodeFilter filter1 = new HasAttributeFilter("class", "View");
            NodeFilter filter2 = new TagNameFilter("a");
            AndFilter contentFilter = new AndFilter(filter1, filter2);
            NodeList nodes2 = e.extractAllNodesThatMatch(contentFilter);

            for (int i = 0; i < nodes2.size(); ++i) {
                HashMap map = new HashMap();
                LinkTag linkTag = (LinkTag) nodes2.elementAt(i);
                list.add(linkTag.getAttribute("id"));
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
}
