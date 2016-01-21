package testExtends;

/**
 * Created with IntelliJ IDEA.
 * User: zhangkl
 * Date: 2015/9/18
 * Time: 9:43
 * 调用-- 子类的method()，子类没有method()执行super.method(),super.method()---调用---method1()
 * 子类和父类都有method1()，是执行子类的method1还是执行父类的method1
 */
public class Test {

    public static void main(String[] args) {
        Father person = new Son();
        person.method();
    }
}

class Father {
    public void method() {
        System.out.println("调用父类method");
        method1();
    }

    public void method1() {
        System.out.println("调用父类method1");
    }
}

class Son extends Father {
    public void method1() {
        System.out.println("调用子类method1");
    }
}
