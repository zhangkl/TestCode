package testMonkey;

/**
 * Created with IntelliJ IDEA.
 * User: zhangkl
 * Date: 2015/9/22
 * Time: 16:32
 * To change this template use File | Settings | File Templates.
 */
public class TestMonkey {
    /**一群猴子排成一圈，按1，2，…，n依次编号。然后从第1只开始数，数到第m只,把它踢出圈，
    *从它后面再开始数，再数到第m只，在把它踢出去…，如此不停 的进行下去，直到最后只剩下一只猴子为止，
    *那只猴子就叫做大王。要求编程模拟此过程，输入m、n, 输出最后那个大王的编号。
     * */

    public static void main(String[] args) {
        TestMonkey tm = new TestMonkey();
        tm.getKing(8,3);
    }

    public int getKing(int n ,int m){
        // 1 m , 2
        int king = 0;
        int totle = n;
        while (totle!=1){
            for(int i=0;i<n;i++){
               for(int j=0;j<m;j++){

               }
            }
            totle--;
            System.out.print("----------------------king="+king);
            System.out.print("  n="+n);
            System.out.print("  m="+m);
            System.out.println("");
        }
        return king;

    }
 }
