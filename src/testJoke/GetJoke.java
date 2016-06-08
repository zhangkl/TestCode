package testJoke;

import org.htmlparser.util.ParserException;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: zhangkl
 * Date: 2016/5/20
 * Time: 16:43
 * To change this template use File | Settings | File Templates.
 */
public class GetJoke {
    static final Integer i = 1;

    public static void main(String[] args) throws IOException, ParserException {
        setValue(i);
        System.out.println(new Integer("0") == i);
        System.out.println(i);
        /*TestHttp testHttp = new TestHttp();
        for (int i = 0; i < 10; i++) {
            HttpRespons httpRespons = testHttp.send("http://www.qiushibaike.com/text/page/"+i,"GET",null,null);
            String context = httpRespons.getContent();
            Parser parser = new Parser();
            parser.setInputHTML(context);
            NodeFilter filte = new AndFilter(new TagNameFilter("div"),new HasAttributeFilter("class","content"));
            NodeList nodeList = parser.extractAllNodesThatMatch(filte);
            for (int j = 0; j < nodeList.size(); j++) {
                if (nodeList.elementAt(j).toPlainTextString().length()>0) {
                    System.out.println(nodeList.elementAt(j).toPlainTextString().trim());
                    System.out.println();
                }
            }
        }
        Map postMap  = new HashMap();
        testHttp.send("https://api.weixin.qq.com/cgi-bin/message/mass/preview?access_token=ACCESS_TOKEN","POST",postMap,null);*/
    }

    public static void setValue(int i) {
        i++;
        System.out.println(i);

    }

}
