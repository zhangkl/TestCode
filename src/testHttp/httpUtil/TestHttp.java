package testHttp.httpUtil;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

/**
 * HTTP请求对象
 *
 * @author OUWCH
 */
public class TestHttp {
    private String defaultContentEncoding = "UTF-8";

    /**
     * 发送GET请求
     *
     * @param urlString URL地址
     * @return 响应对象
     * @throws IOException
     */
    public HttpRespons sendGet(String urlString) throws IOException {
        return this.send(urlString, "GET", null, null);
    }

    /**
     * 发送GET请求
     *
     * @param urlString URL地址
     * @param params    参数集合
     * @return 响应对象
     * @throws IOException
     */
    public HttpRespons sendGet(String urlString, Map<String, String> params)
            throws IOException {
        return this.send(urlString, "GET", params, null);
    }

    /**
     * 发送GET请求
     *
     * @param urlString URL地址
     * @param params    参数集合
     * @param propertys 请求属性
     * @return 响应对象
     * @throws IOException
     */
    public HttpRespons sendGet(String urlString, Map<String, String> params,
                               Map<String, String> propertys) throws IOException {
        return this.send(urlString, "GET", params, propertys);
    }

    /**
     * 发送POST请求
     *
     * @param urlString URL地址
     * @return 响应对象
     * @throws IOException
     */
    public HttpRespons sendPost(String urlString) throws IOException {
        return this.send(urlString, "POST", null, null);
    }

    /**
     * 发送POST请求
     *
     * @param urlString URL地址
     * @param params    参数集合
     * @return 响应对象
     * @throws IOException
     */
    public HttpRespons sendPost(String urlString, Map<String, String> params)
            throws IOException {
        return this.send(urlString, "POST", params, null);
    }

    /**
     * 发送POST请求
     *
     * @param urlString URL地址
     * @param params    参数集合
     * @param propertys 请求属性
     * @return 响应对象
     * @throws IOException
     */
    public HttpRespons sendPost(String urlString, Map<String, String> params,
                                Map<String, String> propertys) throws IOException {
        return this.send(urlString, "POST", params, propertys);
    }

    /**
     * 发送HTTP请求
     *
     * @param urlString
     * @return 响映对象
     * @throws IOException
     */
    public HttpRespons send(String urlString, String method,
                             Map<String, String> parameters, Map<String, String> propertys)
            throws IOException {
        HttpURLConnection urlConnection = null;

        if (method.equalsIgnoreCase("GET") && parameters != null) {
            StringBuffer param = new StringBuffer();
            int i = 0;
            for (String key : parameters.keySet()) {
                if (i == 0)
                    param.append("?");
                else
                    param.append("&");
                param.append(key).append("=").append(parameters.get(key));
                i++;
            }
            urlString += param;
        }
        URL url = new URL(urlString);

        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod(method);
        urlConnection.setDoOutput(true);
        urlConnection.setDoInput(true);
        urlConnection.setUseCaches(false);
        urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.87 Safari/537.36");
        urlConnection.setRequestProperty("Upgrade-Insecure-Requests", "1");
        urlConnection.setRequestProperty("Pragma", "no-cache");
        urlConnection.setRequestProperty( "Connection" , "keep-alive" );
        urlConnection.setRequestProperty( "Content-Type" , "text/html" );
        urlConnection.setRequestProperty("Server", "Tengine");
        urlConnection.setRequestProperty("Host", "www.qixin.com");
        urlConnection.setRequestProperty("Date", "Mon, 09 May 2016 01:52:30 GMT");
        urlConnection.setRequestProperty("Referer", "http://www.qixin.com/search/prov/BJ");
        urlConnection.setRequestProperty("Cookie", "aliyungf_tc=AQAAANDj4kCywQYAWiUnauW9hmLR0zKK; gr_user_id=7ec449a9-cfec-4125-af87-9767319b14be; connect.sid=s%3AnRQl_d9NAcVkRfoGyZox_x4b7Wf1vcM4.Jv10bBUj5%2BB2yeFJ5J6EiX66qp5RWEmyrvgshVHK990; __qc_wId=413; pgv_pvid=4793751178; login_returnurl=http%3A//www.qixin.com; __qc__k=TC_MK=CB13A6DE66AB1E38885B9F42D47AE29F; userKey=QXBAdmin-Web2.0_5tUrhr/6EVtLT+GVfE+vU8k330y+oPICCM6jhUGEoLc%3D; userValue=23ec2f2f-f3d3-f51a-58d5-000c715166b4; hide-download-panel=1; Hm_lvt_52d64b8d3f6d42a2e416d59635df3f71=1462244046,1462755805; Hm_lpvt_52d64b8d3f6d42a2e416d59635df3f71=1462758348; gr_session_id_955c17a7426f3e98=9d86877e-1c6b-44c6-95c2-46d024d40d37; _alicdn_sec=572fed5e9697b96531daf4934d84aabdd928b13b");

        /*urlConnection.setRequestProperty( "Cache-control" , "no-cache, no-store" );
        urlConnection.setRequestProperty("userValue", "4c9146f9-08b2-bd3c-5a28-75ebf42e684e");
        urlConnection.setRequestProperty("hide-download-panel", "1");*/
        /*urlConnection.setRequestProperty("gr_session_id_955c17a7426f3e98", "357b464b-b9c5-4fe3-9ae3-2ecdfd869e63");
        urlConnection.setRequestProperty("Hm_lvt_52d64b8d3f6d42a2e416d59635df3f71", "1461547655");
        urlConnection.setRequestProperty("Hm_lpvt_52d64b8d3f6d42a2e416d59635df3f71", "1461651647");
        urlConnection.setRequestProperty("__qc__k", "TC_MK=CB13A6DE66AB1E38885B9F42D47AE29F");
        urlConnection.setRequestProperty("pgv_pvid", "8052484779");
        urlConnection.setRequestProperty("__qc_wId", "728");
        urlConnection.setRequestProperty("aliyungf_tc", "AQAAANmn42Cw5gkAWiUnahkDzq51GmU7");
        urlConnection.setRequestProperty("gr_user_id", "53ba8ddf-6ad6-4245-94cc-719f11da1833");
        urlConnection.setRequestProperty("connect.sid", "s%3AXzxvNBR748DHeMQFFkyhsq2I8hxJ0kTs.LSQY6BdwXMTC9WHW40cmC2tmSOJ%2BqGufxkriMKV47CM");
        urlConnection.setRequestProperty("login_returnurl", "http%3A//www.qixin.com");
        urlConnection.setRequestProperty("userKey", "QXBAdmin-Web2.0_5tUrhr/6EVtLT+GVfE+vU8k330y+oPICCM6jhUGEoLc%3D");*/
        urlConnection.setConnectTimeout( 20000 );
        urlConnection.setReadTimeout( 20000 );

        if (propertys != null) {
            for (String key : propertys.keySet()) {
                urlConnection.addRequestProperty(key, propertys.get(key));
            }
        }

        if (method.equalsIgnoreCase("POST") && parameters != null) {
            StringBuffer param = new StringBuffer();
            for (String key : parameters.keySet()) {
                param.append("&");
                param.append(key).append("=").append(parameters.get(key));
            }
            urlConnection.getOutputStream().write(param.toString().getBytes());
            urlConnection.getOutputStream().flush();
            urlConnection.getOutputStream().close();
        }

        return this.makeContent(urlString, urlConnection);
    }

    /**
     * 得到响应对象
     * @param urlConnection
     * @return 响应对象
     * @throws IOException
     */
    private HttpRespons makeContent(String urlString,
                                    HttpURLConnection urlConnection) throws IOException {
        HttpRespons httpResponser = new HttpRespons();
        String ecod = urlConnection.getContentEncoding();
        if (ecod == null)
            ecod = this.defaultContentEncoding;


        try {
            InputStream in = urlConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(in,ecod));
            httpResponser.contentCollection = new Vector<String>();
            StringBuffer temp = new StringBuffer();
            String line = bufferedReader.readLine();
            while (line != null) {
                httpResponser.contentCollection.add(line);
                temp.append(line).append("\r\n");
                line = bufferedReader.readLine();
            }
            bufferedReader.close();

            httpResponser.urlString = urlString;

            httpResponser.defaultPort = urlConnection.getURL().getDefaultPort();
            httpResponser.file = urlConnection.getURL().getFile();
            httpResponser.host = urlConnection.getURL().getHost();
            httpResponser.path = urlConnection.getURL().getPath();
            httpResponser.port = urlConnection.getURL().getPort();
            httpResponser.protocol = urlConnection.getURL().getProtocol();
            httpResponser.query = urlConnection.getURL().getQuery();
            httpResponser.ref = urlConnection.getURL().getRef();
            httpResponser.userInfo = urlConnection.getURL().getUserInfo();

            httpResponser.content = new String(temp.toString().getBytes(), ecod);
            httpResponser.contentEncoding = ecod;
            httpResponser.code = urlConnection.getResponseCode();
            httpResponser.message = urlConnection.getResponseMessage();
            httpResponser.contentType = urlConnection.getContentType();
            httpResponser.method = urlConnection.getRequestMethod();
            httpResponser.connectTimeout = urlConnection.getConnectTimeout();
            httpResponser.readTimeout = urlConnection.getReadTimeout();

            return httpResponser;
        } catch (IOException e) {
            throw e;
        } finally {
            if (urlConnection != null)
                urlConnection.disconnect();
        }
    }

    /**
     * 默认的响应字符集
     */
    public String getDefaultContentEncoding() {
        return this.defaultContentEncoding;
    }

    /**
     * 设置默认的响应字符集
     */
    public void setDefaultContentEncoding(String defaultContentEncoding) {
        this.defaultContentEncoding = defaultContentEncoding;
    }

    public String getHeadFields(String htmlurl) throws IOException, InterruptedException {
        HttpURLConnection httpConn = null;
        String cookie = "";
        try {
            URL url = new URL(htmlurl);

            httpConn = (HttpURLConnection) url.openConnection();

            HttpURLConnection.setFollowRedirects(true);
            httpConn.setRequestMethod("GET");
            httpConn.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36");
            httpConn.setRequestProperty("Connection", "keep-alive");
            httpConn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml");
            httpConn.setRequestProperty("Cache-control", "no-cache");
            httpConn.setRequestProperty("Server", "Tengine");
            httpConn.setRequestProperty("Host", "www.qixin.com");
            httpConn.setRequestProperty("Date", "Mon, 09 May 2016 01:52:30 GMT");
            httpConn.setRequestProperty("Referer", "http://www.qixin.com/search/prov/BJ");
            httpConn.setRequestProperty("Cookie", "aliyungf_tc=AQAAANDj4kCywQYAWiUnauW9hmLR0zKK; gr_user_id=7ec449a9-cfec-4125-af87-9767319b14be; connect.sid=s%3AnRQl_d9NAcVkRfoGyZox_x4b7Wf1vcM4.Jv10bBUj5%2BB2yeFJ5J6EiX66qp5RWEmyrvgshVHK990; __qc_wId=413; pgv_pvid=4793751178; login_returnurl=http%3A//www.qixin.com; __qc__k=TC_MK=CB13A6DE66AB1E38885B9F42D47AE29F; userKey=QXBAdmin-Web2.0_5tUrhr/6EVtLT+GVfE+vU8k330y+oPICCM6jhUGEoLc%3D; userValue=23ec2f2f-f3d3-f51a-58d5-000c715166b4; hide-download-panel=1; Hm_lvt_52d64b8d3f6d42a2e416d59635df3f71=1462244046,1462755805; Hm_lpvt_52d64b8d3f6d42a2e416d59635df3f71=1462758348; gr_session_id_955c17a7426f3e98=9d86877e-1c6b-44c6-95c2-46d024d40d37; _alicdn_sec=572fed5e9697b96531daf4934d84aabdd928b13b");
            //post方法，重定向设置
            httpConn.setDoOutput(true);
            httpConn.setDoInput(true);
            httpConn.setUseCaches(false);
            httpConn.setInstanceFollowRedirects(true);
            //写入，post方法必须用流写入的方式传输数据
            StringBuffer str_buf = new StringBuffer(4096);
            OutputStream os = httpConn.getOutputStream();
            os.write(str_buf.toString().getBytes());
            os.flush();
            os.close();
            httpConn.setConnectTimeout(20000);
            httpConn.setReadTimeout(20000);
            //获取重定向和cookie
            String redictURL = httpConn.getHeaderField("Location");
            System.out.println("第一次请求重定向地址 location=" + redictURL);

            //获取cookie
            Map<String, List<String>> map = httpConn.getHeaderFields();
            System.out.println(map.toString());
            Set<String> set = map.keySet();
            for (Iterator<String> iterator = set.iterator(); iterator.hasNext(); ) {
                String key = iterator.next();
                if (key != null) {
                    System.out.println("key=" + key + ",开始获取cookie");
                    if (key.equals("Set-Cookie")) {
                        List<String> list = map.get(key);
                        for (String str : list) {
                            String temp = str.split("=")[0];
                            //System.out.println(temp);
                            //cookie包含到信息非常多，调试发现登录只需这条信息
                            if (temp.equals("_alicdn_sec")) {
                                cookie = str;
                                return cookie;
                            }

                        }
                    }
                }

            }
            httpConn.disconnect();

        } catch (final MalformedURLException me) {
            System.out.println("url不存在!");
            me.getMessage();
            throw me;
        } catch (final FileNotFoundException me) {
            System.out.println(htmlurl + "反爬启动");
            return "0";
        } catch (final IOException e) {
            e.printStackTrace();
            System.out.println("反爬启动:" + htmlurl + "次数:");
            httpConn.disconnect();
            Thread.sleep(20000);
            return getHeadFields(htmlurl);
        }

        return cookie;

    }

    public static void main(String[] args) {

    }
}

