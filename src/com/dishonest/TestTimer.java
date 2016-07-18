/*******************************************************************************
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 ******************************************************************************/

package com.dishonest;

import java.io.IOException;
import java.net.InetAddress;

/**
 * Created with IntelliJ IDEA.
 * User: zhangkl
 * Date: 七月,2016
 */
public class TestTimer {
    public static void main(String[] args) throws IOException {
        InetAddress[] address = InetAddress.getAllByName("www.baidu.com");//ping this IP
        for (int i = 0; i < address.length; i++) {
            System.out.println(address[i].isReachable(5000) + " ping " + address[i].getHostAddress() + " with no interface specified");
        }

    }
}
