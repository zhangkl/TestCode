package testFile;

import info.monitorenter.cpdetector.io.CodepageDetectorProxy;

import java.io.*;

public class ReadWriteFileWithEncode {

    public static void write(String path, String content, String encoding)
            throws IOException {
        File file = new File(path);
        if(!file.exists())
            file.createNewFile();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(file), encoding));
        writer.append(content);
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
        long startTime = System.currentTimeMillis();
        String path = "C:\\Users\\lenovo-01\\Desktop\\100.txt";
        File file = new File(path);
        if(!file.exists())
            file.createNewFile();
        String content = "IID,TRADEID,NAME,CERTTYPE,CERTNO,FINANCECODE,GENERALTYPE,TYPE,ACCOUNT,AREACODE,DATEOPENED,DATECLOSED,CURRENCY,CREDITLIMIT,SHAREACCOUNT,MAXDEBT,GUARANTEEWAY,TERMSFREQ,MONTHDURATION,MONTHUNPAID,BILLINGDATE,RECENTPAYDATE,SCHEDULEDAMOUNT,ACTUALPAYAMOUNT,BALANCE,CURTERMSPASTDUE,AMOUNTPASTDUE,AMOUNTPASTDUE30,AMOUNTPASTDUE60,AMOUNTPASTDUE90,AMOUNTPASTDUE180,TERMSPASTDUE,MAXTERMSPASTDUE,CLASS5STATE,ACCOUNTSTATE,PAYSTATE24MONTH,UNPAID180,INFOINDICATOR,RECORDOPRTYPEOFINFO,DGETDATE,IREPORTSTATE,DATACOMPLETESTATE,GUARANTEEFLAG,SPECIALTRADEFLAG,RESERVED"+"\n";
        long IID = 0;
        String IREPORTSTATE = "0";
        String RESERVED = "0";
        String SPECIALTRADEFLAG = "0";
        String GUARANTEEFLAG = "0";
        String DATACOMPLETESTATE = "5";
        String DGETDATE = "20-3月 -14 08.49.23.494 下午";
        String RECORDOPRTYPEOFINFO = "1";
        String INFOINDICATOR = "2";
        String UNPAID180 = "0";
        String PAYSTATE24MONTH = "///////////////////////*";
        String ACCOUNTSTATE = "1";
        String CLASS5STATE = "1";
        String MAXTERMSPASTDUE = "99";
        String TERMSPASTDUE = "999";
        String AMOUNTPASTDUE180 = "0";
        String AMOUNTPASTDUE90 = "0";
        String AMOUNTPASTDUE60 = "0";
        String AMOUNTPASTDUE30 = "0";
        String AMOUNTPASTDUE = "0";
        String CURTERMSPASTDUE = "0";
        String BALANCE = "10000";
        String ACTUALPAYAMOUNT = "0";
        String SCHEDULEDAMOUNT = "0";
        String RECENTPAYDATE = "2013/1/1";
        String BILLINGDATE = "2013/1/1";
        String MONTHUNPAID = "U";
        String MONTHDURATION = "U";
        String TERMSFREQ = "8";
        String GUARANTEEWAY = "4";
        String MAXDEBT = "10000";
        String SHAREACCOUNT = "10000";
        String CREDITLIMIT = "10000";
        String CURRENCY = "CNY";
        String DATEOPENED = "2013/1/1";
        String DATECLOSED = "2014/1/1";
        String AREACODE = "110108";
        String ACCOUNT = "";
        String TYPE = "91";
        long TRADEID = 0;
        String NAME = "";
        String CERTTYPE = "X";
        String CERTNO = "";
        String FINANCECODE = "G10440402007368605";
        String GENERALTYPE = "1";
        if(!file.exists()) {
            file.createNewFile();
        }
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(file), "utf-8"));
        writer.write(content);
        for (int i = 0; i < 1000000; i++) {
            IID++;
            String iidStr = "";
            if(iidStr.length()<7){
                for (int j=0;j<7-(IID+"").length();j++){
                    iidStr += "0";
                }
            }
            iidStr += IID;
            CERTNO = "zjhm0"+iidStr;
            NAME = "第三方测试0"+iidStr;
            ACCOUNT = "ywh0"+iidStr;
            String data = iidStr+","+TRADEID+","+NAME+","+CERTTYPE+","+CERTNO+","+FINANCECODE+","+GENERALTYPE+","+TYPE+","+ACCOUNT+","+AREACODE+","+DATEOPENED+","+DATECLOSED+","+CURRENCY+","+CREDITLIMIT+","+SHAREACCOUNT+","+MAXDEBT+","+GUARANTEEWAY+","+TERMSFREQ+","+MONTHDURATION+","+MONTHUNPAID+","+BILLINGDATE+","+RECENTPAYDATE+","+SCHEDULEDAMOUNT+","+ACTUALPAYAMOUNT+","+BALANCE+","+CURTERMSPASTDUE+","+AMOUNTPASTDUE+","+AMOUNTPASTDUE30+","+AMOUNTPASTDUE60+","+AMOUNTPASTDUE90+","+AMOUNTPASTDUE180+","+TERMSPASTDUE+","+MAXTERMSPASTDUE+","+CLASS5STATE+","+ACCOUNTSTATE+","+PAYSTATE24MONTH+","+UNPAID180+","+INFOINDICATOR+","+RECORDOPRTYPEOFINFO+","+DGETDATE+","+IREPORTSTATE+","+DATACOMPLETESTATE+","+GUARANTEEFLAG+","+SPECIALTRADEFLAG+","+RESERVED;
            writer.append(data);
            writer.append("\n");
        }
        writer.close();
        long entTime = System.currentTimeMillis();
        System.out.println("运行时间(秒)："+(entTime-startTime)/1000f);
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