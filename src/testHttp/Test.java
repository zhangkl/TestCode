package testHttp;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import testFile.ReadWriteFileWithEncode;
import testHttp.model.DishonestyModel;
import until.DateUtil;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: zhangkl
 * Date: 2016/3/16
 * Time: 17:31
 * To change this template use File | Settings | File Templates.
 */
public class Test implements Runnable{

    public static void main(String[] args) {
        Test test = new Test();
        Thread thread = new Thread(test);
        thread.start();
    }

    private long dateCount = 0;
    @Override
    public void run() {
        try {
            TestHttp testHttp = new TestHttp();
            testHttp.setDefaultContentEncoding("utf-8");
            Map map = new HashMap();
            String url;
            map.put("resource_id","6899");
            map.put("query","%E5%A4%B1%E4%BF%A1%E8%A2%AB%E6%89%A7%E8%A1%8C%E4%BA%BA%E5%90%8D%E5%8D%95");
            map.put("ie","utf-8");
            map.put("oe","utf-8");
            map.put("format","json");
            for (int i = 111; i < 112; i++) {
                map.put("cardNum",String.valueOf(i));
                dateCount = 50;
                for (int j = 0; j <= dateCount; j=j+50) {
                    if (j >= dateCount-50) {
                        map.put("pn", String.valueOf(j));
                    }else{
                        map.put("pn", String.valueOf(j));
                    }
                    url = "https://sp0.baidu.com/8aQDcjqpAAV3otqbppnN2DJv/api.php";
                    HttpRespons hr = testHttp.send(url, "GET", map, null);
                    String jsonString = hr.getContent();
                    List<DishonestyModel> list = json2Model(jsonString);
                    save(list,getSession());
                    String str = "查询条件:" + i + ",pn:"+ j + ",本次取得记录条数:"+list.size()+",当前查询条件总条数:"+dateCount;
                    System.out.println(str);
                    ReadWriteFileWithEncode.write("D:\\code\\TestCode\\src\\testHttp\\info.txt", str, "utf-8");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public List<DishonestyModel> json2Model(String json) throws IOException {
        List<DishonestyModel> modelList = new ArrayList<DishonestyModel>();
        JSONObject jsonObject = JSONObject.fromObject(json);
        DishonestyModel dishonestyModel = new DishonestyModel();
        /*System.out.println(jsonObject.get("data"));
        System.out.println(jsonObject.get("status"));
        System.out.println(jsonObject.get("t"));
        System.out.println(jsonObject.get("set_cache_time"));*/
        JSONArray jsonArray = JSONArray.fromObject(jsonObject.get("data"));
        if (jsonArray.size() > 0) {

            List list = JSONArray.fromObject(jsonArray.get(0));
            JSONObject jsonObject1 = JSONObject.fromObject(list.get(0));
            String dispNumvalue = jsonObject1.getString("dispNum");
            dateCount = Integer.valueOf(dispNumvalue);
            Iterator it = jsonObject1.keys();
            while (it.hasNext()) {
                String key = (String) it.next();
                if ("result".equals(key)) {
                    JSONArray array = jsonObject1.getJSONArray(key);
                    for (int i = 0; i < array.size(); i++) {
                        //System.out.println(array.get(i));
                        JSONObject jsonObject2 = JSONObject.fromObject(array.get(i));
                        dishonestyModel.setSiname(jsonObject2.getString("iname"));
                        dishonestyModel.setSstdstg(jsonObject2.getString("StdStg"));
                        dishonestyModel.setSchangefreq(jsonObject2.getString("changefreq"));
                        dishonestyModel.setSstdstl(jsonObject2.getString("StdStl"));
                        dishonestyModel.setDupdateTime(new Timestamp(Long.valueOf(jsonObject2.getString("_update_time"))));
                        dishonestyModel.setStype(jsonObject2.getString("type"));
                        dishonestyModel.setSperformedpart(jsonObject2.getString("performedPart"));
                        dishonestyModel.setSpublishdatestamp(jsonObject2.getString("publishDateStamp"));
                        dishonestyModel.setSdisrupttypename(jsonObject2.getString("disruptTypeName"));
                        dishonestyModel.setSpartytypename(jsonObject2.getString("partyTypeName"));
                        dishonestyModel.setSduty(jsonObject2.getString("duty"));
                        dishonestyModel.setScourtname(jsonObject2.getString("courtName"));
                        dishonestyModel.setSgistunit(jsonObject2.getString("gistUnit"));
                        dishonestyModel.setSpriority(jsonObject2.getString("priority"));
                        dishonestyModel.setSperformance(jsonObject2.getString("performance"));
                        dishonestyModel.setIage(Integer.parseInt(jsonObject2.getString("age")));
                        dishonestyModel.setSgistid(jsonObject2.getString("gistId"));
                        dishonestyModel.setSbusinessentity(jsonObject2.getString("businessEntity"));
                        dishonestyModel.setDpublishdate(DateUtil.StringToDate2(jsonObject2.getString("publishDate")));
                        dishonestyModel.setSsiteid(jsonObject2.getString("SiteId"));
                        dishonestyModel.setScardnum(jsonObject2.getString("cardNum").replace("****", "---"));
                        dishonestyModel.setSfocusnumber(jsonObject2.getString("focusNumber"));
                        dishonestyModel.setDregdate(DateUtil.StringToDate2(jsonObject2.getString("regDate")));
                        dishonestyModel.setSloc(jsonObject2.getString("loc"));
                        dishonestyModel.setDlastmod(DateUtil.StringToDate2(jsonObject2.getString("lastmod")));
                        dishonestyModel.setSsitelink(jsonObject2.getString("sitelink"));
                        dishonestyModel.setSareaname(jsonObject2.getString("areaName"));
                        dishonestyModel.setScasecode(jsonObject2.getString("caseCode"));
                        dishonestyModel.setSunperformpart(jsonObject2.getString("unperformPart"));
                        modelList.add(dishonestyModel);
                    }
                }
            }
        }
        return  modelList;
    }

    public void save(List<DishonestyModel> list,Session session){
        ServiceRegistry serviceRegistry;
        SessionFactory sessionFactory;
        Session session2 = null;
        try {
            Configuration configuration = new Configuration();
            configuration.configure();
            serviceRegistry = new ServiceRegistryBuilder().applySettings(configuration.getProperties()).buildServiceRegistry();
            sessionFactory = configuration.buildSessionFactory(serviceRegistry);
            session2 = sessionFactory.getCurrentSession();

            Transaction transaction = session2.beginTransaction();
            for (int i = 0; i < list.size(); i++) {
                DishonestyModel dis = new DishonestyModel();
                dis = list.get(i);
                session2.save(dis);
                session2.clear();//使用这个5个对象会全部保存
            }
            transaction.commit();
        } catch (HibernateException e) {
            e.printStackTrace();
        }
    }

    public Session getSession(){
        ServiceRegistry serviceRegistry;
        SessionFactory sessionFactory;
        Session session = null;
        try {
            Configuration configuration = new Configuration();
            configuration.configure();
            serviceRegistry = new ServiceRegistryBuilder().applySettings(configuration.getProperties()).buildServiceRegistry();
            sessionFactory = configuration.buildSessionFactory(serviceRegistry);
            session = sessionFactory.getCurrentSession();
        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return session;
    }
}
