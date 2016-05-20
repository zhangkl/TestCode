package testThread;

/**
 * Created with IntelliJ IDEA.
 * User: zhangkl
 * Date: 2016/2/26
 * Time: 10:17
 * To change this template use File | Settings | File Templates.
 */
public class TestThread implements Runnable {
    static int sum = 0;


    public TestThread(int sum) {
        this.sum = sum;
    }

    public TestThread() {

    }


    public static void main(String[] args) {
        for (int i = 0; i < 5; i++) {
            TestThread testThread = new TestThread();
            Thread thread = new Thread(testThread);
            thread.start();

        }
    }

    @Override
    public void run() {
        synchronized (TestThread.class) {
            for (int l = 0; l < 10; l++) {
                System.out.println(Thread.currentThread().getName() + "：" + l);
            }
        }
    }
}
