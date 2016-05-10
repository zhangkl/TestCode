package testHttp;

import org.hibernate.annotations.common.util.StringHelper;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import testHttp.httpUtil.TestHttp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: zhangkl
 * Date: 2016/5/9
 * Time: 9:06
 * To change this template use File | Settings | File Templates.
 * 企信宝数据抓取类
 */
public class TestQXB {


    public static void main(String[] args) throws IOException, ParserException, InterruptedException {
        String url = "http://www.qixin.com/search/prov/BJ";
        String cookie = new TestHttp().getHeadFields(url);
        Map porp = new HashMap();
        porp.put("Referer", "http://www.qixin.com/search/prov/BJ");
        porp.put("Host", "www.qixin.com");
        porp.put("Cookie", "www.qixin.com");
        System.out.println(cookie);
/*            HttpRespons hr = new TestHttp().send(url, "GET", null, porp);*/
           /* String result = hr.getContent();
            System.out.println(result);
            System.out.println(getLink(url, ""));*/
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

}
