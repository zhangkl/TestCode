package testHttp;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class TestTaiNiu extends Thread {

    // 接口地址
    private static String apiURL = "http://km-tnhq.tpyzq.com/HTTPServer/servlet";
    private Log logger = LogFactory.getLog(this.getClass());
    private long startTime = System.currentTimeMillis();
    private final long firetStartTime = System.currentTimeMillis();
    private long endTime = 0L;
    private long maxTime = 0L;
    private static Long exeHigh = 0L;
    private static Long phoneNum = 18617128423L;
    public static Long totleCount = 0L;
    public static Long errorCount = 0L;
    public static List list = new ArrayList();

    /**
     * 接口地址
     *
     * @param url
     */
    public TestTaiNiu(String url) {
        if (url != null) {
            this.apiURL = url;
        }
    }

    /**
     * 调用 API
     *
     * @param parameters
     * @return
     */
    public String post(String parameters) {
        String body = null;
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost method = new HttpPost(apiURL);
        if (method != null & parameters != null
                && !"".equals(parameters.trim())) {
            try {
                startTime = System.currentTimeMillis();
                // 建立一个NameValuePair数组，用于存储欲传送的参数
                method.addHeader("Content-type", "application/json; charset=utf-8");
                method.setHeader("Accept", "application/json");
                method.setEntity(new StringEntity(parameters, Charset.forName("UTF-8")));

                HttpResponse response = httpClient.execute(method);

                endTime = System.currentTimeMillis();
                int statusCode = response.getStatusLine().getStatusCode();

                Long exeTime = endTime - startTime;
                if (exeTime > 2000) {
                    exeHigh++;
                    if (maxTime < exeTime) {
                        maxTime = exeTime;
                    }
                    logger.error("访问时长：" + parameters + ":time:" + exeTime);
                    list.add("time:" + exeTime + ";");
                }

                if (statusCode != HttpStatus.SC_OK) {
                    logger.error("Method failed:" + response.getStatusLine());
                }
                body = EntityUtils.toString(response.getEntity());

                JSONObject json = JSONObject.fromObject(body);
                String code = json.getString("code");
                if (!"0".equals(code)) {
                    errorCount++;
                    /*logger.error("入参："+parameters);
                    logger.error("返回："+body);
                    logger.error("返回错误：" + Thread.currentThread().getName() + ":::::::" + "totleCount:" + totleCount + ",exeHigh:" + exeHigh);*/
                }
            } catch (IOException e) {
                // 网络错误
                e.printStackTrace();
            } finally {
                totleCount++;
                logger.info(Thread.currentThread().getName() + ":::::::" + "totleCount:" + totleCount + ",exeHigh:" + exeHigh + ",errorCount:" + errorCount + ",time:" + (System.currentTimeMillis() - firetStartTime) / 1000);
            }

        }
        return body;
    }

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            System.out.println(Thread.currentThread().getName() + "：start");
            TestTaiNiu ac = new TestTaiNiu(apiURL);
            ac.start();
        }
    }

    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            post("{\"token\":\"\",\"funcid\":\"800121\",\"parms\":{\"TYPE\":\"\",\"STATUS\":\"1\",\"USERID\":\"" + phoneNum++ + "\",\"CapitalAcount\":\"\"}}");
            try {
                Thread.currentThread().sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        logger.info("maxTime:"+maxTime+";"+list);
    }
}