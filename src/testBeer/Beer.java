package testBeer;

/**
 * Created with IntelliJ IDEA.
 * User: zhangkl
 * Date: 2016/1/11
 * Time: 14:20
 * To change this template use File | Settings | File Templates.
 */
public class Beer {
    /*啤酒2块钱1瓶，
      4个盖换1瓶，
      2个空瓶换1瓶，*/

    int totle = 0;
    int lidCount = 0;
    int bottleCount = 0;
    int count = 0;
    public static void main(String[] args) {
        Beer test = new Beer();
        test.howLid(10/2);

    }


    public void howBeer(int bootle){
        int beer = bootle/2;
        bottleCount = bootle%2;
        howLid(beer);
    }


    public void howBottle(int lid){
        bottleCount += lid/4;
        lidCount = lid%4;
        howBeer(bottleCount);
    }

    public void howLid(int beer){
        count++;
        totle += beer;
        System.out.print("第" + count + "次喝了" + beer + "瓶酒");
        lidCount += beer;
        bottleCount += beer;
        System.out.print(",剩下盖子：" + lidCount);
        System.out.println("，剩下瓶子："+bottleCount);
        if (lidCount<4&&bottleCount<2){
            System.out.println("-------一共喝了" + totle + "瓶酒--------");
            return;
        }
        howBottle(lidCount);
    }
}
