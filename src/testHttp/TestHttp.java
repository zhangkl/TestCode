package testHttp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Vector;

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
        urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36");
        urlConnection.setRequestProperty( "Connection" , "keep-alive" );
        urlConnection.setRequestProperty( "Content-Type" , "text/html" );
        urlConnection.setRequestProperty( "Cache-control" , "no-cache, no-store" );
        /*urlConnection.setRequestProperty( "Host" , "www.wdzj.com" );
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


        if (propertys != null)
            for (String key : propertys.keySet()) {
                urlConnection.addRequestProperty(key, propertys.get(key));
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
     *
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

    public static void main(String[] args) {

    }
}

