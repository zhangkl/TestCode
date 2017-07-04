package testVoice;

import java.text.SimpleDateFormat;
import java.util.Date;


public class DateUtil {
	static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	public static String getDateStr(Date date){
		return sdf.format(date);
	}
}
