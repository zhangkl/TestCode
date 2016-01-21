package testFile;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: zhangkl
 * Date: 2015/12/22
 * Time: 15:30
 * To change this template use File | Settings | File Templates.
 */
public class RenameFile {
    public static void main(String[] args) {


        String path = "D:\\sfcp\\test\\sample\\person\\5";
        File[] files = new File(path).listFiles();
        for (int i = 0; i < files.length; i++) {
            String fielname = files[i].getName().substring(14, 20)+".txt";
            files[i].renameTo(new File(path+"\\"+fielname));
            System.out.println(fielname);
        }
    }


}
