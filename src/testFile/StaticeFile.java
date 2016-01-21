package testFile;

import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * User: zhangkl
 * Date: 2015/12/9
 * Time: 16:49
 * To change this template use File | Settings | File Templates.
 */
public class StaticeFile {
    int fileNum = 0;
    public static void main(String[] args) {
        String  urlStr = "D:\\document\\BUG修复文档\\更新内容\\";
        System.out.println("文件夹下文件个数："+new StaticeFile().statistics(urlStr));

        try {
            File file = new File(urlStr+"\\更新文本.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file),"GBK"));
            String line = "";
            int count = 0;
            while ((line = reader.readLine()) != null) {
                if(line.trim()!=null && line.trim().length() > 0 && line.trim().startsWith("app")){
                    count++;
                    File temp = new File(urlStr+line.trim());
                    if (!temp.isFile()){
                        System.out.println(urlStr+line.trim());
                    }
                }
            }
            System.out.println("文档中文件个数"+count);
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int statistics(String urlStr){
        File[] file = new File(urlStr).listFiles();
        if(file!=null && file.length != 0){
            for (int i = 0; i < file.length; i++) {
                if(file[i].isDirectory()){
                    statistics(urlStr+"\\"+file[i].getName());
                    continue;
                }else{
                    fileNum++;
                }
            }
        }
        return fileNum;

    }
}
