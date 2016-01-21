package testZipper;

public class Zipper {

    public static void main(String[] args) {
        System.out.println(new Zipper().zipString("welcometonowcoderrrrr"));
    }

    /**
     * 利用字符重复出现的次数，编写一个方法，实现基本的字符串压缩功能。比如，字符串“aabcccccaaa”经压缩会变成“a2b1c5a3”。若压缩后的字符串没有变短，则返回原先的字符串。
     * 给定一个string iniString为待压缩的串(长度小于等于3000)，保证串内字符均由大小写英文字母组成，返回一个string，为所求的压缩后或未变化的串。
     * @param iniString
     * @return
     */
    public String zipString(String iniString) {
        // write code here
        StringBuffer sb = new StringBuffer("");
        int count = 1;
        for(int i=0;i<iniString.length();i++){
            if(i+1==iniString.length()){
                sb.append(iniString.charAt(i)).append(count);
            }
            else if(iniString.charAt(i+1) ==iniString.charAt(i)){
                count++;
            }
            else{
                sb.append(iniString.charAt(i)).append(count);
                count = 1;
            }
        }
        if(sb.length()>=iniString.length()){
           return iniString;
        }
        else
            return sb.toString();
    }
}