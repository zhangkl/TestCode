package testHttp.qixinbao;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.hibernate.annotations.common.util.StringHelper;
import testHttp.dao.TestConn;
import until.DateUtil;

import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: zhangkl
 * Date: 2016/5/17
 * Time: 16:40
 * To change this template use File | Settings | File Templates.
 */
public class TestSplitData implements Runnable {
    int sucessCount = 0;
    TestConn testConn;
    PreparedStatement inserEntPS;
    PreparedStatement inserEntPerPS;
    Statement statement;
    ResultSet resultSet;

    public TestSplitData(int sucessCount, TestConn testConn, PreparedStatement inserEntPS, PreparedStatement inserEntPerPS, Statement statement, ResultSet resultSet) {
        this.sucessCount = sucessCount;
        this.testConn = testConn;
        this.inserEntPS = inserEntPS;
        this.inserEntPerPS = inserEntPerPS;
        this.statement = statement;
        this.resultSet = resultSet;
    }

    public static void main(String[] args) throws SQLException {

        try {
            String getDateSql = "select * from (select rownum r,t.* from cred_ent_url t where t.remark = '拆分完成') a where a.r > ? and a.r <= ?";
            String insertEntSql = "insert into CRED_ENTPRISE_INFO (IID, SENTNAME, SAREA, SORGANIZATIONCODE, SREGISTEREDADDRESS, SLEGALNAME, SOPERATIONTERM, SREGISTEROFFER, DCREATEDATE, SCOMPANYTYPE, SREGISTERMONEY, DLICENSINGDATE, SREMARK, SCREDITCODE)" +
                    "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            String inserEntPerSql = "insert into CRED_ENTPRISE_PERSON_INFO (IID, SORGANIZATIONCODE, SPERSONTYPE, SPERSONNAME, SUBSCRIBEDMONEY, SREALMONEY, SPERSONOFFICE, SREMARK, IENTID)" +
                    "values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            for (int i = 0; i < 1; i++) {
                TestConn testConn = new TestConn();
                PreparedStatement ps = testConn.creatPStatement(getDateSql);
                ps.setInt(1, i * 30);
                ps.setInt(2, (i + 1) * 30);
                ResultSet resultSet = ps.executeQuery();
                TestSplitData testSplitData = new TestSplitData(0, testConn, testConn.creatPStatement(insertEntSql), testConn.creatPStatement(inserEntPerSql), testConn.creatStatement(), resultSet);
                Thread thread = new Thread(testSplitData);
                thread.setName("Thread-" + i);
                thread.start();
                System.out.println(thread.getName() + "启动...");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            String enturl;
            String sorganizationcode = "";
            while (resultSet.next()) {
                int entid = getSeqNextVal("seq_cred_entprise_info");
                String gsInfo = resultSet.getString("GSINFO");
                String gdInfo = resultSet.getString("GDINFO");
                String zyInfo = resultSet.getString("ZYINFO");
                enturl = resultSet.getString("ENTURL");
                if (StringHelper.isNotEmpty(gsInfo)) {
                    sorganizationcode = savaGSINFO(gsInfo, entid);
                }
                if (StringHelper.isNotEmpty(gdInfo)) {
                    savaGDINFO(gdInfo, sorganizationcode, entid);
                }
                if (StringHelper.isNotEmpty(zyInfo)) {
                    savaZYINFO(zyInfo, sorganizationcode, entid);
                }
                savaRemark(enturl);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }

    }

    public void savaRemark(String enturl) throws SQLException {
        String sql = "update cred_ent_url set remark = '又拆一次' where enturl = '" + enturl + "'";
        statement.execute(sql);
        sucessCount++;
        System.out.println(Thread.currentThread().getName() + ":" + sucessCount);
    }

    public String savaGSINFO(String xmlCon, int entid) throws SQLException, DocumentException {
        Map<String, String> gsInfoMap = new HashMap<String, String>();
        Document document = DocumentHelper.parseText(xmlCon);
        Element root = document.getRootElement();
        List<Element> users = root.elements();
        for (Element tableele : users) {
            List<Element> elementList = tableele.elements();
            for (Element trele : elementList) {
                List<Element> tdList = trele.elements();
                for (int i = 0; i < tdList.size(); i += 2) {
                    gsInfoMap.put(tdList.get(i).getTextTrim(), tdList.get(i + 1).getTextTrim());
                }
            }
        }

        String sentname = resultSet.getString("entname");
        String sarea = resultSet.getString("area");
        String sorganizationcode = gsInfoMap.get("组织机构代码：");
        String sregisteredaddress = gsInfoMap.get("企业地址：");
        String slegalname = gsInfoMap.get("法定代表人：");
        String soperationterm = gsInfoMap.get("营业期限：");
        String sregisteroffer = gsInfoMap.get("登记机关：");
        Date dcreatedate = DateUtil.StringToDate2(gsInfoMap.get("成立日期："));
        String scompanytype = gsInfoMap.get("公司类型：");
        String sregistermoney = gsInfoMap.get("注册资本：");
        String slicensingdate = gsInfoMap.get("发照日期：");
        Date dlicensingdate = DateUtil.StringToDate2(slicensingdate);
        String sremark = gsInfoMap.get("");
        String screditcode = gsInfoMap.get("统一社会信用代码：");
        inserEntPS.setInt(1, entid);
        inserEntPS.setString(2, sentname);
        inserEntPS.setString(3, sarea);
        inserEntPS.setString(4, sorganizationcode);
        inserEntPS.setString(5, sregisteredaddress);
        inserEntPS.setString(6, slegalname);
        inserEntPS.setString(7, soperationterm);
        inserEntPS.setString(8, sregisteroffer);
        inserEntPS.setDate(9, dcreatedate);
        inserEntPS.setString(10, scompanytype);
        inserEntPS.setString(11, sregistermoney);
        inserEntPS.setDate(12, dlicensingdate);
        inserEntPS.setString(13, sremark);
        inserEntPS.setString(14, screditcode);
        inserEntPS.execute();
        return sorganizationcode;
    }

    public void savaGDINFO(String xmlCon, String sorganizationcode, int entid) throws SQLException, DocumentException {
        Map<String, String> gdInfoMap = new HashMap<String, String>();
        Document document = DocumentHelper.parseText(xmlCon);
        Element root = document.getRootElement();
        List<Element> users = root.elements("tbody");
        for (Element tableele : users) {
            List<Element> elementList = tableele.elements();
            for (Element trele : elementList) {
                gdInfoMap.clear();
                List<Element> tdList = trele.elements();
                if (tdList.size() > 0) {
                    gdInfoMap.put("人员职务", tdList.get(0).getTextTrim());
                }
                if (tdList.size() > 1) {
                    List<Element> aList = tdList.get(1).elements("a");
                    if (aList.size() > 0) {
                        gdInfoMap.put("名称", aList.get(0).getTextTrim());
                    }
                }
                if (tdList.size() > 2) {
                    List<Element> fdivList = tdList.get(2).elements("div");
                    if (fdivList.size() > 0) {
                        List<Element> spanList = fdivList.get(0).elements("span");
                        gdInfoMap.put("认缴资金", spanList.get(0).getTextTrim());
                    }
                }
                if (tdList.size() > 3) {
                    List<Element> ldivList = tdList.get(3).elements("div");
                    if (ldivList.size() > 0) {
                        List<Element> lspanList = ldivList.get(0).elements("span");
                        gdInfoMap.put("实缴资金", lspanList.get(0).getTextTrim());
                    }
                }
                int iid = getSeqNextVal("seq_cred_entprise_person_info");
                String spersontype = "1";
                String spersonname = gdInfoMap.get("名称");
                String subscribedmoney = gdInfoMap.get("认缴资金");
                String srealmoney = gdInfoMap.get("实缴资金");
                String spersonoffice = gdInfoMap.get("人员职务");
                String sremark = gdInfoMap.get("");

                inserEntPerPS.setInt(1, iid);
                inserEntPerPS.setString(2, sorganizationcode);
                inserEntPerPS.setString(3, spersontype);
                inserEntPerPS.setString(4, spersonname);
                inserEntPerPS.setString(5, subscribedmoney);
                inserEntPerPS.setString(6, srealmoney);
                inserEntPerPS.setString(7, spersonoffice);
                inserEntPerPS.setString(8, sremark);
                inserEntPerPS.setInt(9, entid);
                inserEntPerPS.execute();
            }
        }
    }

    public void savaZYINFO(String xmlCon, String sorganizationcode, int entid) throws SQLException, DocumentException {
        Map<String, String> zyInfoMap = new HashMap<String, String>();
        Document document = DocumentHelper.parseText(xmlCon);
        Element root = document.getRootElement();
        List<Element> users = root.elements();
        for (Element tableele : users) {
            List<Element> elementList = tableele.elements();
            for (Element fdivele : elementList) {
                zyInfoMap.clear();
                List<Element> cheldEle = fdivele.elements();
                if (cheldEle.size() > 1) {
                    zyInfoMap.put("人员职务", cheldEle.get(1).getTextTrim());
                }
                if (cheldEle.size() > 0) {
                    List<Element> alist = cheldEle.get(0).elements();
                    zyInfoMap.put("名称", alist.get(0).getTextTrim());
                }
                int iid = getSeqNextVal("seq_cred_entprise_person_info");
                String spersontype = "2";
                String spersonname = zyInfoMap.get("名称");
                String subscribedmoney = zyInfoMap.get("认缴资金");
                String srealmoney = zyInfoMap.get("实缴资金");
                String spersonoffice = zyInfoMap.get("人员职务");
                String sremark = zyInfoMap.get("");

                inserEntPerPS.setInt(1, iid);
                inserEntPerPS.setString(2, sorganizationcode);
                inserEntPerPS.setString(3, spersontype);
                inserEntPerPS.setString(4, spersonname);
                inserEntPerPS.setString(5, subscribedmoney);
                inserEntPerPS.setString(6, srealmoney);
                inserEntPerPS.setString(7, spersonoffice);
                inserEntPerPS.setString(8, sremark);
                inserEntPerPS.setInt(9, entid);
                inserEntPerPS.execute();
            }
        }
    }

    private synchronized int getSeqNextVal(String seqName) throws SQLException {
        ResultSet newrs;
        int id = 0;
        newrs = statement.executeQuery("select " + seqName + ".nextval as id from dual");
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
