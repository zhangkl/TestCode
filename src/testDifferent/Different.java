package testDifferent;


public class Different {
    public boolean checkDifferent(String iniString) {
        // write code here
        for(int i=0;i<iniString.length();i++){
            for(int y=i+1;y<iniString.length();y++){
                if(iniString.charAt(i)==iniString.charAt(y)){
                    return false;
                }
            }
            for(int z=0;z<1000;z++){
                System.out.println(123123);
            }
        }
        return true;
    }

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        System.out.println(new Different().checkDifferent("11234567890poiuytrewqasdfghjkl,mnbvcxz.';[]~!@#$%^&*()_+{}:?>MQWERTYUIOPLKJHGFDSAZXCVBNM"));
        long endTime = System.currentTimeMillis();
        System.out.println(endTime);
        System.out.println(startTime);
        System.out.println("运行时间："+(endTime-startTime));
    }
}