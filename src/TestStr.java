import junit.framework.TestCase;

import java.util.*;

/**
 * Created by zhangkl on 2017/4/10.
 */
public class TestStr extends TestCase {

    public void testString() throws Exception {
        exeString(new String[]{"A", "B", "C", "D"}, new String[]{"A->C", "B->C", "D->C"});

    }

    public String exeString(String[] parms, String[] rules) {
        String str = "";
        List list = new ArrayList();
        for (int i = 0; i < parms.length; i++) {
            str = "";
            for (int j = 0; j < parms.length; j++) {
                if (i >= j){
                    str += parms[i-j];
                }
            }
            System.out.println(str);
        }
        return str;
    }

    public void testMap() throws Exception {
        Map map = new Hashtable();
        map.put("1",1);
        map.put("2",2);
        map.put("3",3);
        map.put("4",4);

        for (int i = 0; i < map.size(); i++) {
//            System.out.println(map.get());
        }

        Iterator iterator = map.entrySet().iterator();
        while (iterator.hasNext()){
            System.out.println(iterator.next());
        }

    }
}
