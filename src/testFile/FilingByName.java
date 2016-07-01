package testFile;

import java.io.*;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: zhangkl
 * Date: 2015/10/9
 * Time: 10:59
 *  根据文件名拷贝出文件到指定文件夹下
 */
public class FilingByName {

    static int totleNum = 1;
    static String filepath = "C:\\Users\\lenovo-01\\Desktop\\0627文件更新\\0627更新列表.txt";
    static String desUrl = "C:\\Users\\lenovo-01\\Desktop\\0627文件更新";

    public static void main(String[] args) throws IOException {
        FilingByName fb = new FilingByName();
        fb.copyFile();
        /*new FilingByName().filing(initUrl, desUrl);*/
        /*new FilingByName().statistics(desUrl);
        new FilingByName().addLogFile(desUrl, "总处理文件个数：" + totleNum);*/
    }

    public void copyFile() throws IOException {
        String line = null;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filepath));
            while ((line = reader.readLine()) != null && line.startsWith("D")) {
                if (line.length() == 0) {
                    continue;
                }
                File afile = new File(line);
                String fileName = afile.getName();
                String prefix = fileName.substring(fileName.lastIndexOf(".") + 1);
                if ("class".equals(prefix)) {
                    String classPath = line.replace("D:\\vss\\SFCPProject\\src", "D:\\vss\\SFCPProject\\WebRoot\\WEB-INF\\classes");
                    afile = new File(classPath);
                } else if ("xml".equals(prefix)) {
                    String classPath = line.replace("D:\\vss\\SFCPProject\\conf", "D:\\vss\\SFCPProject\\WebRoot\\WEB-INF\\classes");
                    afile = new File(classPath);
                } else if ("js".equals(prefix) || "jsp".equals(prefix)) {
                    String classPath = line.replace("D:\\vss\\SFCPProject\\conf", "D:\\vss\\SFCPProject\\WebRoot\\WEB-INF\\classes");
                    afile = new File(classPath);
                }
                System.out.println(afile.getPath());

                String dpath = afile.getPath().replace("D:\\vss\\SFCPProject", desUrl);
                File dfile = new File(dpath);
                if (!dfile.getParentFile().exists()) {
                    dfile.getParentFile().mkdirs();
                }
                if (dfile.exists()) {
                    dfile.delete();
                }
                Files.copy(afile.toPath(), dfile.toPath());
                System.out.println(totleNum++);
            }
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void filing(String urlStr, String desURL) {
        File[] file = new File(urlStr).listFiles();
        for (File aFile : file) {
            if (aFile.isDirectory()) {
                filing(urlStr + "\\" + aFile.getName(), desURL + "\\" + aFile.getName());
                continue;
            } else if (aFile.getName().length() == 31 && aFile.getName().endsWith("txt")) {
                System.out.println(aFile.getName());
                String month = aFile.getName().substring(14, 20);

                String monthPath = desURL + "\\" + month;
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

    public void statistics(String urlStr) {
        File[] file = new File(urlStr).listFiles();
        if (file != null && file.length != 0) {
            int pathNum = 0;
            for (int i = 0; i < file.length; i++) {
                if (file[i].isDirectory()) {
                    /*statistics(urlStr+"\\"+file[i].getName());
                    addLogFile(urlStr, file[i].getName() + ":" + file[i].listFiles().length);*/
                    statistics(urlStr + "\\" + file[i].getName());
                    continue;
                } else if (file[i].getName().length() == 31) {
                    pathNum++;
                }
            }
            addLogFile(desUrl, urlStr + ":" + pathNum);
            totleNum += pathNum;
        }
    }

    public void addLogFile(String urlStr, String logString) {
        File file = new File(urlStr + "//logFile" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + ".txt");
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream outs = new FileOutputStream(file, true);
            logString += "\n";
            byte[] buffer = logString.getBytes();
            outs.write(buffer);
            outs.flush();
            outs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
