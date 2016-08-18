/*******************************************************************************
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 ******************************************************************************/

package testThread;

import java.util.concurrent.ExecutorService;

/**
 * Created with IntelliJ IDEA.
 * User: zhangkl
 * Date: 2016/2/26
 * Time: 10:17
 * To change this template use File | Settings | File Templates.
 */
public class TestThread implements Runnable {
    int num = 0;


    public TestThread(int num) {
        this.num = num;
    }

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = new MyFixedThreadPool(10);
        for (int i = 0; i < 50; i++) {
            Long stattime = System.currentTimeMillis();
            TestThread testThread = new TestThread(i);
            executorService.execute(testThread);
            Long endtime = System.currentTimeMillis();
            System.out.println(Thread.currentThread().getName() + ":执行时间(ms)：" + i + ":" + (endtime - stattime));
        }
        executorService.shutdown();
    }

    @Override
    public void run() {
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + "：" + num);
    }
}
