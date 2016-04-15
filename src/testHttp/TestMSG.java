package testHttp;

import net.sf.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created with IntelliJ IDEA.
 * User: zhangkl
 * Date: 2016/4/15
 * Time: 16:48
 * To change this template use File | Settings | File Templates.
 */
public class TestMSG {
    public static void main(String[] args) throws IOException, InterruptedException {
        String url = "http://www.vipai.com/index.php?ctl=ajax&mobile=18617128423&act=send_mobile_verify";
        while (true) {
            HttpURLConnection testHttp = (HttpURLConnection) new URL(url).openConnection();
            InputStream in = testHttp.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(in,"UTF-8"));
            String line = bufferedReader.readLine();
            bufferedReader.close();
            line = line.substring(line.indexOf("{"));
            JSONObject jsonObject = JSONObject.fromObject(line);
            String status = jsonObject.get("status")+"";
            if ("1".equals(status)) {
                System.out.println("发送成功！");
            }
        }
    }
}
