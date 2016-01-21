package testHoliday;

import java.sql.Date;
import java.util.Calendar;

/**
 * Created with IntelliJ IDEA.
 * User: zhangkl
 * Date: 2016/1/12
 * Time: 10:05
 * To change this template use File | Settings | File Templates.
 */
public class Holiday {

    public static void main(String[] args) {
        String comeDate = System.getProperty("comeDate");
        String endDate = "2016-01-01";
        System.out.println("入职时间："+comeDate);
        int countDay = difference(StringToDate(endDate), StringToDate(comeDate));
        System.out.println("2015年工作天数："+countDay);
        System.out.println("折合年假天数："+((Float.valueOf(countDay))/365)*5);

    }

    /**
     * 根据传入的字符串生成日期
     *
     * @param strDate 格式为"yyyy-mm-dd"或"yyyy-mm"或"yyyy"的字符串
     * @return 日期
     */
    public static java.sql.Date StringToDate(String strDate) {
        try {
            StringBuffer strBuffer = new StringBuffer(strDate);
            while (strBuffer.lastIndexOf("-") < 7) {
                strBuffer.append("-01");
            }

            return java.sql.Date.valueOf(strBuffer.toString());
        } catch (RuntimeException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 得到两个日期之间差的天数
     *
     * @param date1
     * @param date2
     * @return
     */
    public static int difference(Date date1, Date date2) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date1);
        long time1 = cal.getTimeInMillis();

        cal.setTime(date2);
        long time2 = cal.getTimeInMillis();

        long dif = time1 - time2;
        long day = dif / (24 * 60 * 60 * 1000);

        return (int) day;
    }


}
