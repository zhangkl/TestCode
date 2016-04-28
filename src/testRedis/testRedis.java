package testRedis;

import redis.clients.jedis.Jedis;

import java.io.UnsupportedEncodingException;

/**
 * Created with IntelliJ IDEA.
 * User: zhangkl
 * Date: 2016/4/27
 * Time: 8:51
 * To change this template use File | Settings | File Templates.
 */
public class testRedis {
    public static void main(String[] args) throws UnsupportedEncodingException {
        Jedis jedis = new Jedis("127.0.0.1", 8080);
        //权限认证
        jedis.auth("admin");
        System.out.println("Connection to server sucessfully");
        //查看服务是否运行
        System.out.println("Server is running: " + jedis.ping());
    }
}
