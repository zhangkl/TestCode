package testFile;

import info.monitorenter.cpdetector.io.CodepageDetectorProxy;

import java.io.*;

public class ReadWriteFileWithEncode {

    public static void write(String path, String content, String encoding)
            throws IOException {
        File file = new File(path);
        file.delete();
        file.createNewFile();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(file), encoding));
        writer.write(content);
        writer.close();
    }

    public static String read(String path, String encoding) throws IOException {
        String content = "";
        File file = new File(path);
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                new FileInputStream(file), encoding));
        String line = null;
        while ((line = reader.readLine()) != null) {
            content += line + "\n";
        }
        reader.close();
        return content;
    }

    public static void getFileEncode(String  path){
        /*------------------------------------------------------------------------
  detector是探测器，它把探测任务交给具体的探测实现类的实例完成。
  cpDetector内置了一些常用的探测实现类，这些探测实现类的实例可以通过add方法
  加进来，如ParsingDetector、 JChardetFacade、ASCIIDetector、UnicodeDetector。
  detector按照“谁最先返回非空的探测结果，就以该结果为准”的原则返回探测到的
  字符集编码。
--------------------------------------------------------------------------*/

        CodepageDetectorProxy detector =
                info.monitorenter.cpdetector.io.CodepageDetectorProxy.getInstance();
/*-------------------------------------------------------------------------
  ParsingDetector可用于检查HTML、XML等文件或字符流的编码,构造方法中的参数用于
  指示是否显示探测过程的详细信息，为false不显示。
---------------------------------------------------------------------------*/
        detector.add(new info.monitorenter.cpdetector.io.ParsingDetector(false));
/*--------------------------------------------------------------------------
  JChardetFacade封装了由Mozilla组织提供的JChardet，它可以完成大多数文件的编码
  测定。所以，一般有了这个探测器就可满足大多数项目的要求，如果你还不放心，可以
  再多加几个探测器，比如下面的ASCIIDetector、UnicodeDetector等。
 ---------------------------------------------------------------------------*/
        detector.add(info.monitorenter.cpdetector.io.JChardetFacade.getInstance());
//ASCIIDetector用于ASCII编码测定
        detector.add(info.monitorenter.cpdetector.io.ASCIIDetector.getInstance());
//UnicodeDetector用于Unicode家族编码的测定
        detector.add(info.monitorenter.cpdetector.io.UnicodeDetector.getInstance());
        java.nio.charset.Charset charset = null;
        File f = new File(path);
        try {
            charset = detector.detectCodepage(f.toURL());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (charset != null) {
            System.out.println(f.getName() + "编码是：" + charset.name());
        } else
            System.out.println(f.getName() + "未知");
    }

    public static void main(String[] args) throws IOException {
        String path = "C:\\Users\\lenovo-01\\Desktop\\213\\PBC152900H00002010030011000.txt";
        System.out.println(read(path, "GB2312"));
        getFileEncode(path);
//        ReadWriteFileWithEncode.write(path, read(path,"GB2312"), "ASCII");
        /*byte[] b = new byte[1];
        b[0]= 13;
        System.out.println(new String(b,"GB2312"));*/

        /*System.out.println("123123\r\n\r\n");
        System.out.println("*********");
        System.out.println("123123\n\r");
        System.out.println("*********");
        System.out.println("123123\n");
        System.out.println("*********");*/


    }
}