package testName;
import java.text.Collator;
import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: zhangkl
 * Date: 2015/12/1
 * Time: 10:59
 * To change this template use File | Settings | File Templates.
 */
public class SortNameForCHI {

    public static void main(String[] args) {
        test_sort_pinyin();
    }

    public static void test_sort_pinyin() {
        Collator cmp = Collator.getInstance(java.util.Locale.CHINA);
        String[] arr = { "杨阿斯", "郑阿瑟","怡怡" };
        List<String> list = Arrays.asList(arr);
        Arrays.sort(arr, cmp);
        System.out.println(list);
    }
}
