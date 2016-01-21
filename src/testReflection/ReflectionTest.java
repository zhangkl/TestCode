package testReflection;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created with IntelliJ IDEA.
 * User: zhangkl
 * Date: 2016/1/21
 * Time: 10:52
 * To change this template use File | Settings | File Templates.
 */
public class ReflectionTest {

    public static void main(String[] args) throws Exception {

        /*Class classType = Class.forName("testBeer.Beer");
        //获得类的所有方法
        Method methods[] = classType.getDeclaredMethods();
        Field fields[] = classType.getDeclaredFields();
        for (int i = 0; i < methods.length; i++)
            System.out.println(methods[i].toString());
        for (int i = 0;i < fields.length; i++)
            System.out.println(fields[i].toString());*/

        /*Customer customer=new Customer("Tom",21);
        customer.setId(new Long(1));
        Customer customerCopy=(Customer)new ReflectionTest().copy(customer);
        System.out.println("Copy information:"+customerCopy.getName()+" "+customerCopy.getAge());*/


        /*Class classType=ReflectionTest.class;
        Object invokeTester=classType.newInstance();
        //调用InvokeTester对象的add()方法
        Method addMethod=classType.getMethod("add", new Class[]{int.class,int.class});
        Object result=addMethod.invoke(invokeTester, new Object[]{new Integer(100),new Integer(200)});
        System.out.println((Integer)result);
        //调用InvokeTester对象的echo()方法
        Method echoMethod=classType.getMethod("echo", new Class[]{String.class});
        result=echoMethod.invoke(invokeTester, new Object[]{"Hello"});
        System.out.println((String)result);*/

        Class classType=Class.forName("java.lang.String");
        //创建一个长度为10的字符串数组
        Object array= Array.newInstance(classType, 10);
        //把索引位置为5的元素设为"hello"
        Array.set(array, 5, "hello");
        //读取索引位置为5的元素的值
        String s=(String)Array.get(array, 5);
        System.out.println(s);
    }

    public int add(int param1,int param2){
        return param1+param2;
    }
    public String echo(String msg){
        return "echo:"+msg;
    }

    public Object copy(Object object)throws Exception{
        //获得对象的类型
        Class classType=object.getClass();
        System.out.println("Class: "+classType.getName());
        //通过默认构造方法创建一个新的对象
        Object objectCopy=classType.getConstructor(new Class[]{}).newInstance(new Object[]{});
        //获得对象的所有属性
        Field fields[]=classType.getDeclaredFields();
        for(int i=0;i<fields.length;i++){
            Field field=fields[i];
            String fieldName=field.getName();
            String firstLetter=fieldName.substring(0, 1).toUpperCase();
            //获得和属性对应的getXXX()方法的名字
            String getMethodName="get"+firstLetter+fieldName.substring(1);
            //获得和属性对应的setXXX()方法的名字
            String setMethodName="set"+firstLetter+fieldName.substring(1);
            //获得和属性对应的getXXX()方法
            Method getMethod=classType.getMethod(getMethodName, new Class[]{});
            //获得和属性对应的setXXX()方法
            Method setMethod=classType.getMethod(setMethodName, new Class[]{field.getType()});
            //调用原对象的getXXX()方法
            Object value=getMethod.invoke(object, new Object[]{});
            System.out.println(fieldName+":"+value);
            //调用复制对象的setXXX()方法
            setMethod.invoke(objectCopy, new Object[]{value});
        }
        Method method[] = classType.getDeclaredMethods();
        for (int y = 0; y < method.length; y++) {
           /* System.out.println(method[y].toString());*/
            if (method[y].toString().contains("get")){
                Object value = method[y].invoke(object, new Object[]{});
                System.out.println(value+" "+method[y].toString());
            }
        }
        return objectCopy;
    }
}

class Customer{                       //Customer类是一个JavaBean
    private Long id;
    private String name;
    private int age;

    public Customer(){}
    public Customer(String name,int age){
        this.name=name;
        this.age=age;
    }

    public Long getId(){
        return id;
    }
    public void setId(Long id){
        this.id=id;
    }

    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name=name;
    }

    public int getAge(){
        return age;
    }
    public void setAge(int age){
        this.age=age;
    }
}
