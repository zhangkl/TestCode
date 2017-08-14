package testFile;

import org.junit.Test;
import redis.clients.jedis.ShardedJedis;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

import static oracle.net.aso.C11.l;
import static oracle.net.aso.C11.s;

/**
 * Created by zhangkl on 2017/7/28.
 */
public class TestFileChannel {

    public static void main(String[] args) throws IOException {

        File file = new File("C:\\Users\\zhangkl\\Desktop\\testFileChannel.txt");
        long len = file.length();
        System.out.println(len);
        // 获取字节数
        /**
         * 总条数对每组数量求余，余数为零返回number
         * 总条数/每组数量+1 = 位置数 返回余数(最后一条数量)
         * 每组数量大于总条数返回所有数据
         * 每组数量小于0返回所有数据
         */
        // 创建缓存区
        ByteBuffer buffer = ByteBuffer.allocate(2);
        FileInputStream fis = new FileInputStream(file);
        FileChannel ch = fis.getChannel();
        // 设置起始点
        ch.position(0);
        ch.read(buffer);
        buffer.flip();
        while (buffer.hasRemaining()){
            System.out.print((char) buffer.get());
        }
        ch.close();
        fis.close();
    }

    @Test
    public void testFloat(){
        float fPos=Float.POSITIVE_INFINITY;
        float fNeg=Float.NEGATIVE_INFINITY;
        double dPos=Double.POSITIVE_INFINITY;
        double dNeg=Double.NEGATIVE_INFINITY;

        //t1
        System.out.println(fPos==dPos);  //output: true
        System.out.println(fNeg==dNeg);  //output: true

        //t2
        System.out.println(fPos*0);  //output: NAN
        System.out.println(fNeg*0);  //output: NAN
        System.out.println(1F/0);  //output: NAN
        System.out.println(0/0f);  //output: NAN

        //t3
        System.out.println(fPos==(fPos+10000));  //output: true
        System.out.println(fPos==(fPos*10000));  //output: true
        System.out.println(fPos==(fPos/0));  //output: true

        //t4
        System.out.println(Double.isInfinite(dPos));  //output: true
    }

    @Test
    public void testSort(){
        List<CurStatus> curStaList = new ArrayList<CurStatus>();
        curStaList.add(new CurStatus(1D/0));
        curStaList.add(new CurStatus(-0.01));
        curStaList.add(new CurStatus(0.55));
        Collections.sort(curStaList, new Comparator<CurStatus>() {
            @Override
            public int compare(CurStatus o1, CurStatus o2) {
                return o1.getRate().compareTo(o2.getRate());
            }
        }); // 涨跌幅排序
        System.out.println(curStaList.get(0).getRate());
        System.out.println(curStaList.get(1).getRate());
        System.out.println(curStaList.get(2).getRate());
    }

    @Test
    public void testEqu(){
        Logger logger = Logger.getLogger("123");
        logger.log(new MyLogLevel("mytest",1100),"123123");
        logger.info("123");
    }
}
