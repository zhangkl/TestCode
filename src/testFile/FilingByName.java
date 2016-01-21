package testFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: zhangkl
 * Date: 2015/10/9
 * Time: 10:59
 * To change this template use File | Settings | File Templates.
 */
public class FilingByName {

    static int totleNum = 0;
    static String initUrl = "D:\\document\\2015-10-12";
    static String desUrl = initUrl+"_按月份";
    public static void main(String[] args) {
        new FilingByName().filing(initUrl, desUrl);
        new FilingByName().statistics(desUrl);
        new FilingByName().addLogFile(desUrl, "总处理文件个数：" + totleNum);
    }

    public void filing(String urlStr,String desURL){
        File[] file = new File(urlStr).listFiles();
        for (File aFile : file) {
            if (aFile.isDirectory()) {
                filing(urlStr+"\\"+aFile.getName(),desURL+"\\"+aFile.getName());
                continue;
            }
            else if (aFile.getName().length()==31 && aFile.getName().endsWith("txt")){
                System.out.println(aFile.getName());
                String month = aFile.getName().substring(14, 20);

                String monthPath = desURL+"\\" + month;
                File monthFile = new File(monthPath);
                if (!monthFile.exists()) {
                    monthFile.mkdirs();
                }
                try {
                    InputStream ins = new FileInputStream(aFile);
                    FileOutputStream outs = new FileOutputStream(monthFile + "\\" + aFile.getName());
                    byte[] buffer = new byte[1024 * 512];
                    int length;
                    while ((length = ins.read(buffer)) != -1) {
                        outs.write(buffer, 0, length);
                    }
                    ins.close();
                    outs.flush();
                    outs.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public void statistics(String urlStr ){
        File[] file = new File(urlStr).listFiles();
        if(file!=null && file.length != 0){
            int pathNum = 0;
            for (int i = 0; i < file.length; i++) {
                if(file[i].isDirectory()){
                    /*statistics(urlStr+"\\"+file[i].getName());
                    addLogFile(urlStr, file[i].getName() + ":" + file[i].listFiles().length);*/
                    statistics(urlStr+"\\"+file[i].getName());
                    continue;
                }else if (file[i].getName().length()==31){
                    pathNum++;
                }
            }
            addLogFile(desUrl, urlStr+":" + pathNum);
            totleNum += pathNum;
        }
    }

    public void addLogFile(String urlStr ,String logString){
        File file = new File(urlStr+"//logFile"+new SimpleDateFormat("yyyyMMdd").format(new Date())+".txt");
        try{
            if(!file.exists()){
                file.createNewFile();
            }
            FileOutputStream outs = new FileOutputStream(file,true);
            logString+="\n";
            byte[] buffer = logString.getBytes();
            outs.write(buffer);
            outs.flush();
            outs.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
