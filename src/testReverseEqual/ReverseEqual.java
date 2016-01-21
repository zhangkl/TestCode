package testReverseEqual;

import java.util.Arrays;

public class ReverseEqual {
    public boolean checkReverseEqual(String s1, String s2) {
        // write code here
        byte[] s1arr = s1.getBytes();
        byte[] s2arr = s2.getBytes();
        Arrays.sort(s1arr);
        Arrays.sort(s2arr);
        if (Arrays.equals(s1arr,s2arr)){
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        System.out.println(new ReverseEqual().checkReverseEqual("waterbottle","erbottlewat"));
    }
}