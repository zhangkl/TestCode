package testSame;

import java.util.Arrays;

public class Same {
    public boolean checkSam(String stringA, String stringB) {
        // write code here
        if(sortString(stringA).equals(sortString(stringB)))
            return true;
        else
            return false;
    }

    public String sortString(String initstr){

        String sortStr = "";
        char[] chars = initstr.toCharArray();
        Arrays.sort(chars);
        for(int i =0 ;i<chars.length;i++){
            sortStr+=chars[i];
        }
        System.out.println(sortStr);
        return sortStr;
    }

    public static void main(String[] args) {
        new Same().checkSam("Here you are","Are you here");
    }
}