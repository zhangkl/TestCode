/*******************************************************************************
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 ******************************************************************************/

package webservice;

import javax.jws.WebService;
import javax.xml.ws.Endpoint;

/**
 * Created with IntelliJ IDEA.
 * User: zhangkl
 * Date: 七月,2016
 */
@WebService
public class MyService {
    //该方法就是要暴露给其他应用程序调用的方法
    public String transWords(String words) {
        String res = "";
        for (char ch : words.toCharArray()) {
            res += "\t" + ch + "\t";
        }
        return res;
    }

    //这里我们使用main方法来发布我们的service
    public static void main(String[] args) {
        Endpoint.publish("http://localhost:9001/Service/MyService", new MyService());
        System.out.println("Publish Success~");
    }
}
