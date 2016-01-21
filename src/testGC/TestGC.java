package testGC;


/**
 * Created with IntelliJ IDEA.
 * User: zhangkl
 * Date: 2016/1/20
 * Time: 8:54
 * To change this template use File | Settings | File Templates.
 */
public class TestGC {
    public static void main(String args[]) {
        long beginTime =  System.currentTimeMillis();
        int maxCount = 0;
        for  (int i=0;i<100000;i++) {
            Runnable threadDemo = new ThreadDemo();
            Thread thread = new Thread(threadDemo); // 创建一个新线程
            thread.start();
            System.out.println("Main Thread" + thread.getName() + ":" + i++ + ":" + Thread.activeCount());
            //System.gc();
            if (Thread.activeCount()>maxCount){
                maxCount = Thread.activeCount();
            }
            System.out.println(maxCount);
        }
        long endTime = System.currentTimeMillis();
        System.out.println("运行时间：" + (endTime-beginTime));
    }
}

class ThreadDemo implements Runnable {
    ThreadDemo() {
        Thread t = new Thread(this, "Demo Thread");
        System.out.println("Child thread: " + t);
        t.start(); // 开始线程
    }

    // 第二个线程入口
    public void run() {
        /*for (int i = 500; i > 0; i--) {
            System.out.println("Child Thread: " + i);
        }*/
        System.out.println("Exiting child thread.");
    }
}
