package testList;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: zhangkl
 * Date: 2015/12/7
 * Time: 14:25
 * To change this template use File | Settings | File Templates.
 */
public class ListTest {
    public static void main(String[] args) {
        List alist = new ArrayList();
        List llist = new LinkedList();
        //createList(alist,100000);
        createList(llist,100);
        //traverList(alist);
        traverList(llist);
    }

    public static List createList(List list,int size){
        long start = System.currentTimeMillis();
        for (int i = 0; i < size; i++) {
            list.add("test"+i);
        }
        long end = System.currentTimeMillis();
        System.out.println("创建list运行时间："+(end-start));
        return list;
    }

    public static void traverList(List list){
        Long t1,t2;
        t1=System.currentTimeMillis();
        for(Object tmp:list)
        {
            Object obj = tmp;
            //System.out.println(tmp);
        }
        t2=System.currentTimeMillis();
        System.out.println("简化For循环遍历时间:" + (t2 -t1) + "(ms)");

        t1=System.currentTimeMillis();
        list.get(0);
        /*for(int i = 0; i < list.size(); i++)
        {
            list.get(50000);
            //System.out.println(list.get(i));
        }*/
        t2=System.currentTimeMillis();
        System.out.println("For循环遍历时间:" + (t2 -t1) + "(ms)");
        Iterator<Object> iter = list.iterator();
        t1=System.currentTimeMillis();
        while(iter.hasNext())
        {
            Object obj = iter.next();
        }
        t2=System.currentTimeMillis();
        System.out.println("Iterator循环遍历时间:" + (t2 -t1) + "(ms)");
    }

}
