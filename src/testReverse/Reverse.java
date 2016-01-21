package testReverse;

public class Reverse {

    public String reverseString(String iniString) {
        // write code here
        String revStr = "";
        for(int i=iniString.length()-1;i>=0;i--){
           revStr += iniString.charAt(i);

        }
        System.out.println(revStr);
        return  revStr;
    }

    public static void main(String[] args) {
        new Reverse().reverseString("This is nowcoder");
    }
}