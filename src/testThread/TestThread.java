package testThread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        Long stattime = System.currentTimeMillis();
        for (int i = 0; i < 500; i++) {
            TestThread testThread = new TestThread(i);
            Thread thread = new Thread(testThread);
            thread.start();
            //executorService.execute(testThread);
        }
        Long endtime = System.currentTimeMillis();
        System.out.println("执行时间(ms)：" + (endtime - stattime));
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
