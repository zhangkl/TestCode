/*******************************************************************************
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 ******************************************************************************/

package testException;

/**
 * Created with IntelliJ IDEA.
 * User: zhangkl
 * Date: 七月,2016
 */
public class ExceptionTest implements Runnable {
    static int i;

    public ExceptionTest(int i) {
        ExceptionTest.i = i;
    }

    public static void main(String[] args) throws Exception {
        for (int i = 0; i < 1; i++) {
            ExceptionTest exceptionTest = new ExceptionTest(i);
            Thread thread = new Thread(exceptionTest);
            thread.start();
        }
    }

    @Override
    public void run() {
        for (int j = 0; j < 1000; j++) {
            if (j == 1) {
                Thread.currentThread().interrupted();
                //return;
            } else {
                System.out.println(Thread.currentThread().isInterrupted());
                System.out.println(j);
            }

        }
    }

    private void print() {
        System.out.println("***********************************");
        System.out.println("***********************************");
        System.out.println("***********************************");
    }
}
