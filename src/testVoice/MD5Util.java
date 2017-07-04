package testVoice;

import java.security.MessageDigest;

public class MD5Util {
	public static String getLowCaseMD5(String src) throws Exception{
		MessageDigest md = MessageDigest.getInstance("MD5");
		byte[] hashBytes = md.digest(src.getBytes("UTF-8"));
		return HexUtil.byteToHex(hashBytes).toLowerCase();
	}
	
	public static void main(String[] args) throws Exception{
		String test = getLowCaseMD5("hello");
		System.out.println(test);
	}
}
