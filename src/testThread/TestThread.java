package testThread;

/**
 * Created with IntelliJ IDEA.
 * User: zhangkl
 * Date: 2016/2/26
 * Time: 10:17
 * To change this template use File | Settings | File Templates.
 */
public class TestThread {
    public static void main(String[] args) {
        Long startTime = System.currentTimeMillis();
        for (int i = 0; i < 10000000; i++) {
            System.out.println(i);
            Thread thread = new Thread();

        }
        Long endTime = System.currentTimeMillis();

        System.out.println("运行时间："+(endTime-startTime));
    }

}
