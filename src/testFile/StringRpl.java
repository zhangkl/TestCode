package testFile;

import java.io.*;

public class StringRpl {

    public static String read(File src) {
        StringBuffer res = new StringBuffer();
        String line = null;
        try {
            FileInputStream in = new FileInputStream(src);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, "GB2312"));
            while ((line = reader.readLine()) != null) {
                res.append(line + "\n");
            }
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res.toString();
    }

    public static boolean write(String cont, File dist) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(dist));
            writer.write(cont);
            writer.flush();
            writer.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


    public static void main(String[] args) {
        File src = new File("C:\\Users\\zhangkl\\Desktop\\太平洋证券基金理财接口文档.docx");
        String cont = StringRpl.read(src);
        System.out.println(cont);
        //对得到的内容进行处理
        cont = cont.replaceAll("16477475474", "123");
        System.out.println(cont);
        //更新源文件
//        System.out.println(StringRpl.write(cont, src));
    }

}