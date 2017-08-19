package test;

import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;

import static oracle.net.aso.C11.k;
import static oracle.net.aso.C11.t;

/**
 * Created by zhangkl on 2017/5/    .
 */
public class testTest {


    final String s = "12313";

    @Test
    public void testTest() throws UnsupportedEncodingException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        String str = "1231111";
        baseUtil(str);
    }

    public void baseUtil(final String str) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        System.out.println(str);
        /*System.out.println(Base64.encode("123".getBytes()));
        System.out.println(new String(Base64.decode("MTIz")));
        System.out.println(new String(Base64.decode("MSUFBUHBF")));*/

    }

    @Test
    public void testBool() {
        final String code = "10601099";
    }

    public void setS(String s) {
        s = "123";
    }
}
