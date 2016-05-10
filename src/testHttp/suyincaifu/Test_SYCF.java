package testHttp.suyincaifu;

import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.htmlparser.util.ParserException;
import testFile.ReadWriteFileWithEncode;
import testHttp.httpUtil.TestHttp;
import testHttp.dao.TestConn;
import testHttp.httpUtil.HtmlParser;
import testHttp.httpUtil.HttpRespons;

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
 * Date: 2016/3/16
 * Time: 17:31
 * To change this template use File | Settings | File Templates.
 */
public class Test_SYCF implements Runnable {
    private static Logger logger = Logger.getLogger("Test_4.class");

    private int sucessCount = 0;
    private long dateCount = 0;
    TestConn testConn;
    Statement statement;
    Statement statement2;
    int start;
    int end;
    String isNew;

    public Test_SYCF(int sucessCount, long dateCount, TestConn testConn, Statement statement, Statement statement2, int start, int end, String isNew) {
        this.sucessCount = sucessCount;
        this.dateCount = dateCount;
        this.testConn = testConn;
        this.statement = statement;
        this.statement2 = statement2;
        this.start = start;
        this.end = end;
        this.isNew = isNew;
    }

    public static void main(String[] args) {
        try {
            TestConn testConn = new TestConn();
            Test_SYCF test_sycf = new Test_SYCF(0, 1, testConn, testConn.creatStatement(), testConn.creatStatement(), 1, 24, "4");
            Thread thread = new Thread(test_sycf);
            thread.setName("thread1");
            thread.start();

            Test_SYCF test_sycf1 = new Test_SYCF(0, 1, new TestConn(), new TestConn().creatStatement(), new TestConn().creatStatement(), 1, 4, "8");
            Thread thread1 = new Thread(test_sycf1);
            thread1.setName("thread2");
            thread1.start();

            Test_SYCF test_sycf2 = new Test_SYCF(0, 1, new TestConn(), new TestConn().creatStatement(), new TestConn().creatStatement(), 1, 81,"");
            Thread thread2 = new Thread(test_sycf2);
            thread2.setName("thread3");
            thread2.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        getData(start, end);
    }

    public void getData(int start, int end) {
        try {
            TestHttp testHttp = new TestHttp();
            testHttp.setDefaultContentEncoding("utf-8");
            Map map = new HashMap();
            String url;
            map.put("isNew", isNew); //4--苏享投   8--苏鑫投  --苏盈投
            for (int i = start; i < end; i++) {
                if (sucessCount >= dateCount) {
                    break;
                }
                map.put("pageNo", i+"");
                url = "http://www.suyinwm.com/webloan/inc/Main_toMainPage.action";
                try {
                    HttpRespons hr = testHttp.send(url, "POST", map, null);
                    String jsonString = hr.getContent();
                    json2Model(jsonString);
                } catch (Exception e) {
                    logger.error(e.getMessage());
                    logger.error(map.toString());
                    continue;
                }
                String str = Thread.currentThread().getName() + ":" + ",isNew:" + isNew + ",目前成功插入条数:" + sucessCount + ",当前查询条件总条数:" + dateCount;
                logger.info(str);
            }
            ReadWriteFileWithEncode.write("D:\\code\\TestCode\\logs\\thred.txt", Thread.currentThread().getName() + ":循环结束。", "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void json2Model(String json) throws IOException, SQLException, ParserException {
        JSONObject jsonObject = JSONObject.fromObject(json);
        dateCount = Long.valueOf((Integer) jsonObject.get("counts"));
        List list = (List) jsonObject.get("noticeListInfo");
        for (int i = 0; i < list.size(); i++) {
            Map map = (Map) list.get(i);
            StringBuffer sql = new StringBuffer();
            Long loanAmt = Math.round((Double) map.get("loanAmt"));
            String loanId = (String) map.get("loanId");
            String url = "http://www.suyinwm.com/webloan/inc/Investbid_toSpecialAreaIndex.action?loanId="+loanId;
            List<List> infoList = HtmlParser.testTable(url, "");
            List list0 = infoList.get(0);
            String _borrow = (String) list0.get(0);
            String[] borrow = _borrow.split("#");
            String sborrowname;
            String sidentitycode;
            if (borrow.length<2) {
                sborrowname = borrow[0].replace("借款人：","");
                sidentitycode = "";
            }
            else {
                sborrowname = borrow[0].replace("借款人：","");
                sidentitycode = borrow[1];
            }

            List list3 = infoList.get(3);
            String ssex = (String) list3.get(1);

            List list4 = infoList.get(4);
            String sdegree = (String) list4.get(1);
            String smarriagestate = (String) list4.get(3);

            List list5 = infoList.get(5);
            String sworkcity = (String) list5.get(1);
            String sincome = (String) list5.get(3);

            sql.append("insert into cred_wdpt_borrower (IID, SBORROWNAME, SSEX, SWORKCITY, SPOSITION, SSCHOOL, SCOMPANY, SAGE, SNATURE, SINCOME, " +
                    "SDEGREE, SIDENTITYCODE, SMARRIAGESTATE, SINDUSTRY, SWORKTIME, SBORROWCODE, SP2PORGCODE, DGETDATE, SREMARK, ILOANSUM, CARDTYPE)\n" +
                    "values (");
            int iid = getSeqNextVal("SEQ_CRED_DISHONESTY");
            sql.append(iid + ",");  //iid
            sql.append("'"+ sborrowname + "',");
            sql.append("'"+ ssex + "',");
            sql.append("'"+ sworkcity + "',");
            sql.append("'',");// SPOSITION
            sql.append("'',");// SSCHOOL
            sql.append("'',");// SCOMPANY
            sql.append("'',");// SAGE
            sql.append("'',");// SNATURE
            sql.append("'"+ sincome + "',");
            sql.append("'"+ sdegree + "',");
            sql.append("'"+ sidentitycode + "',");
            sql.append("'"+ smarriagestate + "',");
            sql.append("'',");// SINDUSTRY
            sql.append("'',");// SWORKTIME
            sql.append("'',");// SBORROWCODE
            sql.append("'Q10152900H1201',");// SP2PORGCODE
            sql.append("sysdate,");// DGETDATE
            sql.append("'',");// SREMARK
            sql.append(loanAmt+",");// ILOANSUM
            sql.append("'1')");// CARDTYPE
            //System.out.println(sql);
            statement.execute(sql.toString());
            sucessCount++;
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
