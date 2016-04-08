package testFile;

import java.io.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @author
 */
public class ContextReplace {

    /**
     * 查找并替换
     *
     * @param baseDirName    原文件路径
     * @param targetFileName 需要查找替换文件的关键词：如.jsp
     * @param fileList       查找到的集合
     * @param startStr       文件中需要替换的字符串
     * @param endStr         替换后的字符串
     * @throws IOException
     * @throws InterruptedException
     */
    public static void findFiles(String baseDirName, String targetFileName,
                                 List fileList, String startStr, String endStr) throws IOException,
            InterruptedException {
        String tempName = null;
        File baseDir = new File(baseDirName);
        if (!baseDir.exists() || !baseDir.isDirectory()) {
            System.out.println("未找到文件");
        } else {
            String[] filelist = baseDir.list();
            for (int i = 0; i < filelist.length; i++) {
                File readfile = new File(baseDirName + "\\" + filelist[i]);
// System.out.println(readfile.getName());
                if (!readfile.isDirectory()) {
                    tempName = readfile.getName();
                    if (ContextReplace.wildcardMatch(targetFileName, tempName)) {
                        fileList.add(readfile.getAbsoluteFile());
                        File src = new File(readfile.getAbsoluteFile()
                                .toString());
                        String cont = ContextReplace.read(src);
                        Long fileDate = readfile.lastModified();
// System.out.println(fileDate);

// FileSearcher.readFile(src.toString(), endPath ,
// endStr);
// FileSearcher.createAndDeleteFile(src.toString());
// FileSearcher.createAndDeleteFile(src.toString());
// FileSearcher.readFile(endPath,src.toString() ,
// startStr);

                        cont = cont.replaceAll(startStr, endStr);
                        ContextReplace.write(cont, src);
                        readfile.setLastModified(fileDate);
                    }
                } else if (readfile.isDirectory()) {
                    findFiles(baseDirName + "\\" + filelist[i], targetFileName,
                            fileList, startStr, endStr);
                }
            }
        }
        System.out.println("共有" + fileList.size() + "个文件被修改");
    }

    public static boolean createAndDeleteFile(String filePath)
            throws IOException {
        boolean result = false;
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
            result = true;
        } else {
            file.createNewFile();
            result = true;
        }
        return result;
    }

    private static boolean wildcardMatch(String pattern, String str) {
        int patternLength = pattern.length();
        int strLength = str.length();
        int strIndex = 0;
        char ch;
        for (int patternIndex = 0; patternIndex < patternLength; patternIndex++) {
            ch = pattern.charAt(patternIndex);
            if (ch == '*') {
                while (strIndex < strLength) {
                    if (wildcardMatch(pattern.substring(patternIndex + 1), str
                            .substring(strIndex))) {
                        return true;
                    }
                    strIndex++;
                }
            } else if (ch == '?') {
                strIndex++;
                if (strIndex > strLength) {
                    return false;
                }
            } else {
                if ((strIndex >= strLength) || (ch != str.charAt(strIndex))) {
                    return false;
                }
                strIndex++;
            }
        }
        return (strIndex == strLength);
    }

    public static String read(File src) {
        StringBuffer res = new StringBuffer();
        String line = null;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(src));

            while ((line = reader.readLine()) != null) {
                int index = line.indexOf("查询条件:");
                int indexEnd = line.indexOf(",pn");
                System.out.print("\""+line.substring(index+5,indexEnd)+"\",");
                res.append(line + "\r\n");
            }

            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res.toString();
    }

    // 转换文件格式
    public static boolean write(String cont, File dist) {
        System.out.println(cont);
        try {
            OutputStreamWriter writer = new OutputStreamWriter(
                    new FileOutputStream(dist), "UTF-8");
            System.out.println(dist);
// BufferedWriter writer = new BufferedWriter(new FileWriter(dist));
            writer.write(cont);
            writer.flush();
            writer.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void readFile(String path, String path2, String format)
            throws IOException {
        String str = "";
        FileInputStream fs = null;
        FileWriter fw = null;
        PrintWriter out = null;
        BufferedReader in = null;
        File f = new File(path);
        if (f.exists()) {
            try {

                fs = new FileInputStream(f);
                fw = new FileWriter(path2);
                out = new PrintWriter(fw);
                in = new BufferedReader(new InputStreamReader(fs, format));

                while (true) {
                    str = in.readLine();
                    if (str == null) {
                        break;
                    }

                    out.write(str);
                    out.println();
                    out.flush();

                }

            } catch (IOException e) {

                e.printStackTrace();
            } finally {
                in.close();
                fs.close();
                fw.close();
                out.close();
            }
        }
    }

    /**
     * 更改文件编码格式
     *
     * @param paramert
     * @throws IOException
     * @throws InterruptedException
     * @baseDIR 文件父路径，通过这个路径更新目录下所有文件
     * @fileName 需要更改的文件类型
     * @char1 原编码格式，原文件中的需要被替换的字符
     * @char2 更改后的编码格式，更改后的字符
     */
    public static void main(String[] paramert) throws IOException,
            InterruptedException {
        /*String baseDIR = "D:\\test";
        String fileName = "*.txt";
        String char1 = "UTF-8";
        String char2 = "GBK";
        List resultList = new ArrayList();
        ContextReplace.findFiles(baseDIR, fileName, resultList, char1, char2);
        if (resultList.size() == 0) {
            System.out.println("No File Fount.");
        }*/
        String path = "C:\\Users\\lenovo-01\\Desktop\\int.txt";
        File file = new File(path);
//        read(file);
        String[] arr = {"001","301","201","151","101","002","051","003","005","121","031","1510","1511","0010","1210","0310","0610","1512","0011","0311","1211","0611","1513","0012","1212","0612","0312","1514","1213","0013","0613","0313","1515","1214","0014","0314","3010","1516","2010","0315","1215","0015","0510","3011","1517","1010","0016","0316","2011","0511","3012","1518","1011","0317","0017","2012","0512","3013","1519","1012","2013","0318","0018","0513","3014","1013","0319","2014","0019","0514","3015","1014","2015","0020","0515","3016","2016","0021","1015","0516","3017","2017","0022","1016","0517","3018","2018","0023","1017","0518","3019","2019","0024","1018","0519","0025","1019","0026","0027","0028","0029","0030","0031","0032","0033","0034","0035","0036","0037","0038","0039","0615","1217","1218","1219","0617","0618","0817","0010","0030","0020","0050","0011","0031","0021","0051","0010","0020","0050","0030","001","031","101","002","151","201","121","051","301","003","005","0050","0051","0052","0053","0054","0056","0057","1510","0010","0310","0058","0610","1511","0011","0059","0311","0611","1512","0012","1210","0612","1513","0312","0013","1211","0613","0313","1514","1212","1515","0314","0014","1213","2010","0315","1516","0510","1010","0015","1214","2011","0316","1517","0016","0511","1011","1215","3010","0317","1518","0512","0017","1012","3011","0318","2012","0513","1519","0319","3012","2013","0018","1013","0514","2014","0515","1014","3013","0019","2015","0516","1015","3014","0517","2016","3015","1016","0518","2017","0020","3016","1017","0519","2018","0021","3017","2019","0022","3018","1018","0023","3019","1019","0024","0025","0026","0027","0028","0029","0030","0031","0032","0033","0034","0035","0036","0037","0038","0039","0615","1217","1218","1219","0617","0618","0817"};
        Arrays.sort(arr);
        List<String> list = new LinkedList<String>();
        for (int i = 0; i < arr.length; i++) {
            if(!list.contains(arr[i])){
                list.add(arr[i]);
            }
        }
        System.out.println(list);

    }

}
