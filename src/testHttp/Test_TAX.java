package testHttp;

import org.apache.log4j.Logger;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import testFile.ReadWriteFileWithEncode;
import testHttp.dao.TestConn;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: zhangkl
 * Date: 2016/3/16
 * Time: 17:31
 * To change this template use File | Settings | File Templates.
 */
public class Test_TAX implements Runnable {
    private static Logger logger = Logger.getLogger("Test_TAX.class");

    private int sucessCount = 0;
    private long dateCount = 0;
    TestConn testConn;
    Statement statement;
    Statement statement2;

    public Test_TAX(int sucessCount, long dateCount, TestConn testConn, Statement statement, Statement statement2) {
        this.sucessCount = sucessCount;
        this.dateCount = dateCount;
        this.testConn = testConn;
        this.statement = statement;
        this.statement2 = statement2;
    }

    public static void main(String[] args) {
        try {
            TestConn testConn = new TestConn();
            Test_TAX test = new Test_TAX(0, 0, testConn,testConn.creatStatement(),testConn.creatStatement());
            Thread thread = new Thread(test);
            thread.setName("thread");
            thread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        //System.out.println(Thread.currentThread().getName());
        try {
            getData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getLink(String htmlStr){
        try{

            for (int j = 1; j < 47; j++) {
                Map<String,String> map = new HashMap<String, String>();
                map.put("categeryid","24");
                map.put("querystring24","articlefield03");
                map.put("querystring25","articlefield02");
                map.put("queryvalue","");
                map.put("cPage",j+"");
                Parser parser = new Parser(HtmlParser.getUrl("http://hd.chinatax.gov.cn/xxk/action/ListXxk.do", map));//初始化Parser，这里要注意导入包为org.htmlparser。这里参数有很多。这个地方我写的是提前获取好的html文本。也可以传入URl对象
                parser.setEncoding("utf-8");//设置编码机
                NodeFilter nodeFilter = new TagNameFilter("a");
                NodeList nodes = parser.extractAllNodesThatMatch(nodeFilter);//通过filter获取nodes
                for (int i = 0; i < nodes.size(); i++) {
                    LinkTag linkTag = (LinkTag) nodes.elementAt(i);
                    String link = linkTag.getLink();
                    if (link.startsWith("http")) {
                        System.out.println(link);
                        ReadWriteFileWithEncode.write("D:\\code\\TestCode\\logs\\link.txt", link, "UTF-8");
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void getData(String url) throws ParserException, IOException, SQLException {
        List<String> list = HtmlParser.testTable(url,"");
        if (list.size()==12){
            ReadWriteFileWithEncode.write("D:\\code\\TestCode\\logs\\tax1.txt", "个人"+url, "UTF-8");
            ReadWriteFileWithEncode.write("D:\\code\\TestCode\\logs\\tax1.txt", "个人"+list.toString(), "UTF-8");
        }
        int iid = getSeqNextVal("SEQ_CRED_TAX_ILLEGAL");
        StringBuffer stringBuffer = new StringBuffer("insert into CRED_TAX_ILLEGAL (IID,STAXPAYERNAME,STAXPAYERCODE,SORGANIZATIONCODE,SREGISTEREDADDRESS," +
                "SLEGALNAME,SLEGALCARDTYPE,SLEGALSEX,SLEGALCODE,STAXMANAGERNAME,STAXMANAGERSEX,STAXMANAGERCARDTYPE,STAXMANAGERCODE,SINTERMEDIARY,SCASENATURE," +
                "SMAJORILLEGALFACTS,DGETDATE) values (");
        stringBuffer.append(iid+",");
        stringBuffer.append("'" + list.get(1) + "'" + ",");//纳税人名称
        stringBuffer.append("'" + list.get(3) + "'" + ",");//纳税人识别号
        stringBuffer.append("'" + list.get(5) + "'" + ",");//组织机构代码
        stringBuffer.append("'" + list.get(7) + "'" + ",");//注册地址
        String[] legal = list.get(9).split(",");
        if (legal != null && legal.length==3) {
            stringBuffer.append("'" + legal[0] + "'" + ",");//法定代表人名称
            stringBuffer.append("'" + legal[2].split(":")[0] + "'" + ",");//法定代表人证件类型
            stringBuffer.append("'" + legal[1] + "'" + ",");//法定代表人性别
            stringBuffer.append("'" + legal[2].split(":")[1] + "'" + ",");//法定代表人证件号码
        }
        else{
            stringBuffer.append("'',");//法定代表人名称
            stringBuffer.append("'',");//法定代表人证件类型
            stringBuffer.append("'',");//法定代表人性别
            stringBuffer.append("'',");//法定代表人证件号码
            ReadWriteFileWithEncode.write("D:\\code\\TestCode\\logs\\tax1.txt", "法人错误："+url, "UTF-8");
            ReadWriteFileWithEncode.write("D:\\code\\TestCode\\logs\\tax1.txt", "法人错误："+list.get(9), "UTF-8");
        }
        String[] taxManager = list.get(11).split(",");
        if (taxManager != null & taxManager.length == 3) {
            stringBuffer.append("'" + taxManager[0] + "'" + ",");//财务负责人姓名
            stringBuffer.append("'" + taxManager[1] + "'" + ",");//财务负责人性别
            if (taxManager[2].split(":").length == 2) {
                stringBuffer.append("'" + taxManager[2].split(":")[0] + "'" + ",");//财务负责人证件类型
                stringBuffer.append("'" + taxManager[2].split(":")[1]+ "'" + ",");//财务负责人证件号码
            }
            else{
                stringBuffer.append("'',");//财务负责人证件类型
                stringBuffer.append("'',");//财务负责人证件号码
            }
        }
        else{
            stringBuffer.append("'',");//财务负责人姓名
            stringBuffer.append("'',");//财务负责人性别
            stringBuffer.append("'',");//财务负责人证件类型
            stringBuffer.append("'',");//财务负责人证件号码


            ReadWriteFileWithEncode.write("D:\\code\\TestCode\\logs\\tax1.txt", "财务发责任错误：" + url, "UTF-8");
            ReadWriteFileWithEncode.write("D:\\code\\TestCode\\logs\\tax1.txt", "财务发责任错误：" + list.get(11), "UTF-8");
        }
        stringBuffer.append("'" + list.get(13) + "'" + ",");//中介机构
        stringBuffer.append("'" + list.get(15) + "'" + ",");//案件性质
        stringBuffer.append("'" + list.get(17) + "'" + ",");//主要违法事实和相关法律依据及税务处理处罚情况
        stringBuffer.append("sysdate)");//更新日期
        System.out.println(stringBuffer);
        statement.execute(stringBuffer.toString());
        sucessCount++;
        String str = "当前链接:"+url+",保存成功。目前总成功个数为："+sucessCount;
        System.out.println(str);
    }

    public void getData() {
        List<String> listRecord = null;
        try {
            String src = "D:\\code\\TestCode\\logs\\link.txt";
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(src), "UTF-8"));
            Test_Json tj = new Test_Json();
            tj.testConn = new TestConn();
            tj.statement = tj.testConn.creatStatement();
            tj.statement2 = tj.testConn.creatStatement();
            String line = "";
            while ((line = reader.readLine()) != null) {
                List<String> list = new ArrayList<String>();
                listRecord = list;
                list = HtmlParser.testTable(line,"");
                if (list.size()==12){
                    ReadWriteFileWithEncode.write("D:\\code\\TestCode\\logs\\tax.txt", "个人"+line, "UTF-8");
                    ReadWriteFileWithEncode.write("D:\\code\\TestCode\\logs\\tax.txt", "个人"+listRecord.toString(), "UTF-8");
                    continue;
                }
                int iid = getSeqNextVal("SEQ_CRED_TAX_ILLEGAL");
                StringBuffer stringBuffer = new StringBuffer("insert into CRED_TAX_ILLEGAL (IID,STAXPAYERNAME,STAXPAYERCODE,SORGANIZATIONCODE,SREGISTEREDADDRESS," +
                        "SLEGALNAME,SLEGALCARDTYPE,SLEGALSEX,SLEGALCODE,STAXMANAGERNAME,STAXMANAGERSEX,STAXMANAGERCARDTYPE,STAXMANAGERCODE,SINTERMEDIARY,SCASENATURE," +
                        "SMAJORILLEGALFACTS,DGETDATE,LINK) values (");
                stringBuffer.append(iid+",");
                stringBuffer.append("'" + list.get(1) + "'" + ",");//纳税人名称
                stringBuffer.append("'" + list.get(3) + "'" + ",");//纳税人识别号
                stringBuffer.append("'" + list.get(5) + "'" + ",");//组织机构代码
                stringBuffer.append("'" + list.get(7) + "'" + ",");//注册地址
                String[] legal = list.get(9).split(",");
                if (legal != null && legal.length==3) {
                    stringBuffer.append("'" + legal[0] + "'" + ",");//法定代表人名称
                    stringBuffer.append("'" + legal[2].split(":")[0] + "'" + ",");//法定代表人证件类型
                    stringBuffer.append("'" + legal[1] + "'" + ",");//法定代表人性别
                    stringBuffer.append("'" + legal[2].split(":")[1] + "'" + ",");//法定代表人证件号码
                }
                else{
                    ReadWriteFileWithEncode.write("D:\\code\\TestCode\\logs\\tax.txt", "法人错误："+line, "UTF-8");
                    ReadWriteFileWithEncode.write("D:\\code\\TestCode\\logs\\tax.txt", "法人错误："+list.toString(), "UTF-8");
                    continue;
                }
                String codeNum = "";
                String name = "";
                String sex = "";
                String credType = "";
                String[] taxManager = list.get(11).split(",");
                for (int i = 0; i < taxManager.length; i++) {
                    if (taxManager[i].contains(":")){
                        credType = taxManager[i].split(":")[0];
                        codeNum = taxManager[i].split(":")[1];
                    }else if("男".equals(taxManager[i]) || "女".equals(taxManager[i]) ){
                        sex = taxManager[i];
                    }else{
                        name = taxManager[i];
                    }
                    
                }
                stringBuffer.append("'" +name + "'" + ",");//财务负责人姓名
                stringBuffer.append("'" + sex + "'" + ",");//财务负责人性别
                stringBuffer.append("'" + credType + "'" + ",");//财务负责人证件类型
                stringBuffer.append("'" + codeNum + "'" + ",");//财务负责人证件号码
                stringBuffer.append("'" + list.get(13) + "'" + ",");//中介机构
                stringBuffer.append("'" + list.get(15) + "'" + ",");//案件性质
                stringBuffer.append("'" + list.get(17) + "'" + ",");//主要违法事实和相关法律依据及税务处理处罚情况
                stringBuffer.append("sysdate,");//更新日期
                stringBuffer.append("'" + line + "')");//主要违法事实和相关法律依据及税务处理处罚情况
                System.out.println(stringBuffer);
                statement.execute(stringBuffer.toString());
                sucessCount++;
                String str = "当前链接:"+line+",保存成功。目前总成功个数为："+sucessCount;
                System.out.println(str);
            }

            reader.close();

        } catch (Exception e) {
            try {
                ReadWriteFileWithEncode.write("D:\\code\\TestCode\\logs\\tax.txt", listRecord.toString(), "UTF-8");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            System.out.println(e.getMessage());
        }
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
