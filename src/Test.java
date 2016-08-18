/*******************************************************************************
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 ******************************************************************************/

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created with IntelliJ IDEA.
 * User: zhangkl
 * Date: 七月,2016
 */
public class Test implements Runnable {

    public static void main(String[] args) throws InterruptedException {
        //testMethed(10);
        ExecutorService service = Executors.newFixedThreadPool(2);
        for (int i = 0; i < 10; i++) {
            Test test = new Test();
            System.out.println(1);
            service.execute(test);
            System.out.println(2);
        }
        service.shutdown();
        System.out.println(service.isShutdown());
    }

    public static void testMethed(int num) throws InterruptedException {
        for (int i = 0; i < num; i++) {
            System.out.println(i);
            Thread.sleep(1000);
            testMethed(num);
            System.out.println("*****************");
        }
    }

    @Override
    public void run() {
        try {
            Thread.sleep(1000);
            System.out.println(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
