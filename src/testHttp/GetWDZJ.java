package testHttp;

import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.hibernate.internal.util.StringHelper;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.*;
import org.htmlparser.tags.TableColumn;
import org.htmlparser.tags.TableRow;
import org.htmlparser.tags.TableTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import testFile.ReadWriteFileWithEncode;
import testHttp.dao.TestConn;

import java.io.IOException;
import java.io.StringReader;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: zhangkl
 * Date: 16-2-22
 * Time: 下午2:13
 * To change this template use File | Settings | File Templates.
 */
public class GetWDZJ extends Thread {
    private static Logger logger = Logger.getLogger("GetWDZJ.class");

    private int sucessCount = 0;
    private int dateCount = 0;
    TestConn testConn;
    PreparedStatement ps;
    Statement statement2;
    int start;
    int end;

    public GetWDZJ(int sucessCount, int dateCount, TestConn testConn, PreparedStatement preparedStatement, Statement statement2, int start, int end) {
        this.sucessCount = sucessCount;
        this.dateCount = dateCount;
        this.testConn = testConn;
        this.ps = preparedStatement;
        this.statement2 = statement2;
        this.start = start;
        this.end = end;
    }

    public static void main(String[] args) {
        try {
            for (int i = 1; i < 126; i+=8) {
                String sql = new String("insert into CRED_WDZJ_PLATINFO " +
                        "(IID,SPLATID,SPLATNAME,SPLATURL,SLOCATIONAREANAME,SONLINEDATE,SPLATEARNINGS,IREGISTEREDCAPITAL," +
                        "SWITHDRAWSPEED,STERM,IRISKRESERVE,SSECURITYMODELOTHER,SCREDITASSIGNMENT,SFUNDCUSTODIAN,SBIDSECURITY," +
                        "SGUARANTEEINSTITUTIONS,SBUSTYPE,SPLANTBRIEF,SCOMPANYNAME,SCOMPANYLEGAL,SCOMPANYTYPE,SSHAREHOLDERSTRUCTURE," +  //
                        "SREGISTEREDCAPITAL,SACTUALCAPITAL,SREGISTEREDADDR,SOPENDATE,SAPPROVALDATE,SREGISTRATIONAUTHORITY,SBUSINESSLICENSENUM," +
                        "SORGCODE,STAXREGISTRATIONNUM,SFILINGURL,SFILINGURLDATE,SFILINGTYPE,SFILINGCOMPANYNAME,SFILINGIPCNUM,SMANAGEFEE,SRECHARGEFEE," +
                        "SCASHOUTFEE,SVIPFEE,STRANSFERFEE,SPAYTYPE,SADDR,SSERVICETEL,SCOMPANYTEL,SCOMPANYFAX,SSERVICEMAIL) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," +
                        "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
                TestConn testConn = new TestConn();
                GetWDZJ getWDZJ = new GetWDZJ(0, 0, testConn,testConn.creatPStatement(sql),testConn.creatStatement(), i, i+8);
                Thread thread = new Thread(getWDZJ);
                thread.setName("thread"+i);
                thread.start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            TestHttp testHttp = new TestHttp();
            Map map = new HashMap();
            for (int i = start; i < end; i++) {
                if (i>125){
                    break;
                }
                String url = "http://www.wdzj.com/front_select-plat?currPage=" + i;
                HttpRespons hr = testHttp.send(url, "GET", map, null);
                String result = hr.getContent();
                json2Model(result);
                System.out.println(Thread.currentThread().getName() +"：当前页：" + i + "，成功条数:"+sucessCount );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void json2Model(String json) throws IOException, SQLException, ParserException {
        JSONObject jsonObject = JSONObject.fromObject(json);
        String rowCount = jsonObject.get("rowCount").toString();
        dateCount = Integer.parseInt(rowCount);
        List list = (List) jsonObject.get("list");
        for (int i = 0; i < list.size(); i++) {
            Map map = (Map) list.get(i);
            String splatid = (String) map.get("platId");
            String splatname = (String) map.get("platName");
            String platPin = (String) map.get("platPin");
            String splaturl = (String) map.get("platUrl");
            String slocationareaname = (String) map.get("locationAreaName")+","+(String) map.get("locationCityName");
            String sonlinedate = (String) map.get("onlineDate");
            String splatearnings = map.get("platEarnings").toString();
            Double iregisteredcapital = Double.parseDouble(map.get("registeredCapital").toString());
            String swithdrawspeed;
            if (map.get("withdrawSpeed")!=null){
                swithdrawspeed = map.get("withdrawSpeed").toString();
            } else
                swithdrawspeed = "";
            String sterm = (String) map.get("term");
            Double iriskreserve = (Double) map.get("riskReserve");
            String url = "http://www.wdzj.com/dangan/"+platPin;
            TestHttp testHttp = new TestHttp();
            HttpRespons hr = testHttp.send(url, "GET", null, null);
            String result = hr.getContent();
            Parser parser = new Parser();
            parser.setInputHTML(result);
            parser.setEncoding("utf-8");//设置编码机

            NodeFilter[] filters=new NodeFilter[3];
            filters[0]=new AndFilter(new TagNameFilter("span"), new HasAttributeFilter("class","name"));
            filters[1]=new HasChildFilter(filters[0]);
            filters[2]=new TagNameFilter("li");
            NodeFilter contentFilter=new AndFilter(filters[2],filters[1]);
            Map<String,String> platInfo = new HashMap<String, String>();
            NodeList nodes = parser.extractAllNodesThatMatch(contentFilter);//通过filter获取nodes
            for (int k = 1; k < nodes.size(); k++) {
                String value = nodes.elementAt(k).toPlainTextString().trim();
                String describe  = nodes.elementAt(k).getChildren().elementAt(0).toPlainTextString().trim();
                platInfo.put(describe,value);
            }
            String ssecuritymodelother = platInfo.get("保障模式");
            ssecuritymodelother = this.formatString(ssecuritymodelother);
            String screditassignment = platInfo.get("债权转让");
            String sfundcustodian = platInfo.get("资金托管");
            String sbidsecurity = platInfo.get("投标保障");
            String sguaranteeinstitutions = platInfo.get("担保机构");
            sguaranteeinstitutions = this.formatString(sguaranteeinstitutions);
            String sbustype = platInfo.get("业务类型");

            parser.setInputHTML(result);
            NodeFilter aboutFilter=new AndFilter(new TagNameFilter("div"), new HasAttributeFilter("class","bd aboutBd"));
            NodeList divnodes = parser.extractAllNodesThatMatch(aboutFilter);//通过filter获取nodes
            platInfo.put("公司简介", divnodes.asString());
            String splantbrief = platInfo.get("公司简介");//平台简介
            splantbrief = this.formatString(splantbrief);

            parser.setInputHTML(result);
            NodeFilter componyFilters=new AndFilter(new TagNameFilter("div"), new HasAttributeFilter("class","bd inforBd"));
            NodeList componynodes = parser.extractAllNodesThatMatch(componyFilters);//通过filter获取nodes
            NodeList child = componynodes.elementAt(0).getChildren();
            NodeList spanList = child.extractAllNodesThatMatch(new TagNameFilter("span"));
            Map<String,String> componyMap = new HashMap<String,String>();
            for (int j = 0; j < spanList.size(); j++) {
                String describe = spanList.elementAt(j).toPlainTextString().trim();
                String value ;
                if (j<spanList.size()-1) {
                    value = child.toString().substring(child.toString().indexOf(describe) + describe.length(), child.toString().indexOf(spanList.elementAt(j + 1).toPlainTextString().trim()));
                }
                else {
                    value = child.toString().substring(child.toString().indexOf(describe) + describe.length());
                }
                describe = describe.substring(0, describe.length() - 1);

                componyMap.put(describe,value.trim());
            }

            String scompanyname = componyMap.get("企业名称");//企业名称
            String scompanylegal = componyMap.get("企业法人");//企业法人
            String scompanytype = componyMap.get("公司类型");//公司类型
            String sshareholderstructure = componyMap.get("股东结构"); //股东结构
            sshareholderstructure = formatString(sshareholderstructure);
            String sregisteredcapital = componyMap.get("注册资本");//注册资本
            String sactualcapital = componyMap.get("实缴资本") ;//实缴资本
            String sregisteredaddr = componyMap.get("注册地址") ;//注册地址
            String sopendate = componyMap.get("开业日期") ;//开业日期
            String sapprovaldate = componyMap.get("核准日期") ;//核准日期
            String sregistrationauthority = componyMap.get("登记机关") ;//登记机关
            String sbusinesslicensenum = componyMap.get("营业执照注册号") ;//营业执照注册号
            String sorgcode = componyMap.get("组织机构代码");//组织机构代码
            String staxregistrationnum = componyMap.get("税务登记号") ;//税务登记号

            parser.setInputHTML(result);
            NodeFilter tableFilter = new NodeClassFilter(TableTag.class);
            NodeFilter fatherFilter = new HasParentFilter(new AndFilter(new TagNameFilter("div"),new HasAttributeFilter("class","bd webRecordBd")));
            AndFilter andFilter = new AndFilter(tableFilter,fatherFilter);
            List<List<String>> mapList = new ArrayList<List<String>>();
            NodeList tableList = parser.extractAllNodesThatMatch(andFilter);
            for (int table = 0; table <= tableList.size(); table++) {
                if (tableList.elementAt(table) instanceof TableTag) {
                    TableTag tag = (TableTag) tableList.elementAt(table);
                    TableRow[] rows = tag.getRows();
                    for (int j = 0; j < rows.length; j++) {
                        List<String> tdlist = new ArrayList();
                        TableRow tr = (TableRow) rows[j];
                        TableColumn[] td = tr.getColumns();
                        for (int k = 0; k < td.length; k++) {
                            tdlist.add(td[k].toPlainTextString().trim());
                        }
                        if (tdlist.size() > 0) {
                            mapList.add(tdlist);
                        }
                    }
                }
            }
            Map<String,String> tableMap = new HashMap();
            if (mapList != null && mapList.size()>1&&mapList.get(0).size() == mapList.get(1).size()) {
                for (int j = 0; j < mapList.get(0).size(); j++) {
                    tableMap.put(mapList.get(0).get(j),mapList.get(1).get(j));
                }
            }
            else{
                System.out.println(mapList);
            }
            String sfilingurl = tableMap.get("备案域名");//网站备案域名
            String sfilingurldate = tableMap.get("域名备案时间") ;//网站域名备案时间
            String sfilingtype = tableMap.get("备案单位性质") ;//网站备案单位性质
            String sfilingcompanyname = tableMap.get("备案单位名称") ;//网站备案单位名称
            String sfilingipcnum = tableMap.get("ICP备案号") ;//网站ICP备案号

            parser.setInputHTML(result);
            NodeFilter feeFilters=new AndFilter(new TagNameFilter("div"), new HasAttributeFilter("class","bd costBd"));
            NodeList feenodes = parser.extractAllNodesThatMatch(feeFilters);//通过filter获取nodes
            NodeList feechild = feenodes.elementAt(0).getChildren();
            NodeList feeList = feechild.extractAllNodesThatMatch(new TagNameFilter("span"));
            Map<String,String> feeMap = new HashMap<String,String>();
            for (int j = 0; j < feeList.size(); j++) {
                String describe = feeList.elementAt(j).toPlainTextString().trim();
                String value ;
                if (j<feeList.size()-1) {
                    value = feechild.toString().substring(feechild.toString().indexOf(describe) + describe.length(), feechild.toString().indexOf(feeList.elementAt(j + 1).toPlainTextString().trim()));
                }
                else {
                    value = feechild.toString().substring(feechild.toString().indexOf(describe) + describe.length());
                }
                describe = describe.substring(0, describe.length() - 1);

                feeMap.put(describe, value.trim());
            }
            String smanagefee = feeMap.get("管理费");//管理费
            smanagefee = this.formatString(smanagefee);
            String srechargefee = feeMap.get("充值费") ;//充值费
            srechargefee = this.formatString(srechargefee);
            String scashoutfee = feeMap.get("提现费") ;//提现费
            scashoutfee = this.formatString(scashoutfee);
            String svipfee = feeMap.get("VIP费用") ;// VIP费用
            svipfee = this.formatString(svipfee);
            String stransferfee = feeMap.get("转让费用") ;// 转让费用
            stransferfee = this.formatString(stransferfee);
            String spaytype = feeMap.get("支付方式");//支付方式

            NodeList otherNode = feenodes.elementAt(1).getChildren();
            NodeList otherList = otherNode.extractAllNodesThatMatch(new TagNameFilter("span"));
            Map<String,String> otherMap = new HashMap<String,String>();
            for (int j = 0; j < otherList.size(); j++) {
                String describe = otherList.elementAt(j).toPlainTextString().trim();
                String value ;
                if (j<otherList.size()-1) {
                    value = otherNode.toString().substring(otherNode.toString().indexOf(describe) + describe.length(), otherNode.toString().indexOf(otherList.elementAt(j + 1).toPlainTextString().trim()));
                }
                else {
                    value = otherNode.toString().substring(otherNode.toString().indexOf(describe) + describe.length());
                }
                describe = describe.substring(0, describe.length() - 1);

                otherMap.put(describe, value.trim());
            }
            String saddr = otherMap.get("联系地址");// 联系地址
            String sservicetel = otherMap.get("400电话") ;//400电话
            String scompanytel = otherMap.get("公司电话") ;//公司电话
            String scompanyfax = otherMap.get("公司传真") ;//公司传真
            String sservicemail = otherMap.get("服务邮箱") ;//服务邮箱

            int iid = getSeqNextVal("SEQ_CRED_WDZJ_PLATINFO");
            ps.setInt(1,iid);  //iid
            ps.setString(2, splatid );//
            ps.setString(3, splatname );//
            ps.setString(4, splaturl );//
            ps.setString(5, slocationareaname);//
            ps.setString(6, sonlinedate);//
            ps.setString(7, splatearnings);//
            ps.setDouble(8,iregisteredcapital);//
            ps.setString(9, swithdrawspeed);//
            ps.setString(10, sterm );//
            ps.setDouble(11, iriskreserve);//
            ps.setString(12, ssecuritymodelother);//
            ps.setString(13, screditassignment );//
            ps.setString(14, sfundcustodian);//
            ps.setString(15, sbidsecurity);//
            ps.setString(16, sguaranteeinstitutions);//
            ps.setString(17, sbustype);//
            StringReader reader = new StringReader(splantbrief);
            ps.setCharacterStream(18, reader, splantbrief.length());
            ps.setString(19, scompanyname );//
            ps.setString(20, scompanylegal );//
            ps.setString(21, scompanytype);
            ps.setString(22, sshareholderstructure);
            ps.setString(23, sregisteredcapital);
            ps.setString(24, sactualcapital);
            ps.setString(25, sregisteredaddr);
            ps.setString(26, sopendate);
            ps.setString(27, sapprovaldate);
            ps.setString(28, sregistrationauthority);
            ps.setString(29, sbusinesslicensenum);
            ps.setString(30, sorgcode);
            ps.setString(31, staxregistrationnum);
            ps.setString(32, sfilingurl);
            ps.setString(33, sfilingurldate);
            ps.setString(34, sfilingtype);
            ps.setString(35, sfilingcompanyname);
            ps.setString(36, sfilingipcnum);
            ps.setString(37, smanagefee);
            ps.setString(38, srechargefee);
            ps.setString(39, scashoutfee);
            ps.setString(40, svipfee);
            ps.setString(41, stransferfee);
            ps.setString(42, spaytype);
            ps.setString(43, saddr);
            ps.setString(44, sservicetel);
            ps.setString(45, scompanytel);
            ps.setString(46,scompanyfax);
            ps.setString(47, sservicemail);
            ps.execute();
            sucessCount++;

            /**
             * 保存平台高管
             */
            parser.setInputHTML(result);

            NodeFilter sbdivFilter=new AndFilter(new TagNameFilter("div"), new HasAttributeFilter("class","bd peopleBd"));
            NodeFilter sbpFilter=new HasParentFilter(sbdivFilter);
            NodeFilter tagFilter = new TagNameFilter("ul");
            AndFilter sbandFilter = new AndFilter(tagFilter,sbpFilter);
            NodeList nodeList = parser.extractAllNodesThatMatch(sbandFilter);
            String html = nodeList.toString();
            String[] arr = html.split("\n");
            List<String> sblist = new ArrayList<String>();
            for (int k = 0; k < arr.length; k++) {
                if (StringHelper.isNotEmpty(arr[k].trim())) {
                    sblist.add(arr[k].trim());
                }
            }
            if (sblist.size()>0 && sblist.size()%3!=0){
                System.out.println("错误："+splatid+splatname+platPin+sblist);
                ReadWriteFileWithEncode.write("D:\\code\\TestCode\\logs\\wdzj.txt", "错误："+splatid+splatname+platPin+sblist, "UTF-8");
            }
            else{
                for (int j = 0; j < sblist.size(); j+=3) {
                    StringBuffer sb =  new StringBuffer("insert into CRED_WDZJ_PLATEXECUTIVE (SPLATID, SNAME, SGRAY, SBRIEF)\n" +
                            "values (");
                    sb.append("'"+splatid+"',");
                    if (sblist.get(j).length()>50){
                        ReadWriteFileWithEncode.write("D:\\code\\TestCode\\logs\\wdzj.txt", "错误："+splatid+splatname+platPin+sblist, "UTF-8");
                        break;
                    }
                    sb.append("'"+sblist.get(j)+"',");
                    sb.append("'"+sblist.get(j+1)+"',");
                    sb.append("'" + sblist.get(j + 2) + "')");
                    statement2.execute(sb.toString());
                }
            }
        }
    }

    private String formatString(String sshareholderstructure) {
        Pattern p = Pattern.compile("\\s*|\t|\r|\n");
        sshareholderstructure = p.matcher(sshareholderstructure).replaceAll("");
        return sshareholderstructure;
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
