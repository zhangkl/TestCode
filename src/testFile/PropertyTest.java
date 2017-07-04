package main.java.testFile;

import java.io.*;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

public class PropertyTest {
    public static void main(String[] args) { 
        /*Properties prop = new Properties();
        try{
            //读取属性文件a.properties
            InputStream in = new BufferedInputStream (new FileInputStream("D:\\code\\TestCode\\src\\a.properties"));
            prop.load(in);     ///加载属性列表
            Iterator<String> it=prop.stringPropertyNames().iterator();
            while(it.hasNext()){
                String key=it.next();
                System.out.println(key+":"+prop.getProperty(key));
            }
            in.close();
            
            ///保存属性到b.properties文件
            FileOutputStream oFile = new FileOutputStream("b.properties", true);//true表示追加打开
            prop.setProperty("phone", "10086");
            prop.store(oFile, "The New properties file");
            oFile.close();
        }
        catch(Exception e){
            System.out.println(e);
        }*/
        new PropertyTest().saveProperties();
    }

    public void saveProperties()
    {
        try
        {
            Properties properties = new Properties();

            Properties pload = new Properties();
            File file = new File("D:\\code\\TestCode\\src\\a.properties");
            pload.load(new FileInputStream(file));
            Set<String> pSet = pload.stringPropertyNames();
            Iterator i = pSet.iterator();
            while(i.hasNext())
            {
                String propertiesName = i.next().toString();
                //删除一个当获取的名称hk相同时，就返回到下一步;break;是退出循环
                if("hk".equalsIgnoreCase(propertiesName)) continue;
                properties.setProperty("usa","123");
                //修改
                if("japan".equalsIgnoreCase(propertiesName))
                {
                    properties.setProperty(propertiesName, "123");
                }
            }
            /*properties.setProperty("usa", "美国");
            properties.setProperty("hk", "香港");
            properties.setProperty("japan", "日本");
            properties.setProperty("china", "中国");*/
            //添加
            properties.store(new FileOutputStream(file), properties.toString());

        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}