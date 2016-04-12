package testSwing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Swing进度条样例 
 * @create Apr 24, 2013 1:52:44 PM 
 * @author 玄玉<http://blog.csdn.net/jadyer> 
 */
public class JProgressBarDemo {
    Timer timer;
    JProgressBar jpbFileLoading;

    public JProgressBarDemo() {
        JFrame jf = new JFrame("进度条测试");
        /**
         * 创建一个常规模式的进度条,其默认为水平方向,最小值为0,最大值为100,初始值为0 
         */
        jpbFileLoading = new JProgressBar();
        jpbFileLoading.setStringPainted(true);  //设置进度条呈现进度字符串,默认为false  
        jpbFileLoading.setBorderPainted(false); //不绘制边框,默认为true  
        jpbFileLoading.setPreferredSize(new Dimension(100, 40)); //设置首选大小  
        timer = new Timer(50, new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                int loadingValue = jpbFileLoading.getValue();
                if (loadingValue < 100){
                    jpbFileLoading.setValue(++loadingValue);
                }else {
                    timer.stop();
                }
            }
        });
        timer.start();
        /**
         * 创建一个不确定模式的进度条 
         */
        JProgressBar jpbFileLoadingIndeterminate = new JProgressBar();
        jpbFileLoadingIndeterminate.setIndeterminate(true); //设置进度条为不确定模式,默认为确定模式  
        jpbFileLoadingIndeterminate.setStringPainted(true);
        jpbFileLoadingIndeterminate.setString("文件加载中......");
        /**
         * 将两种进度条放到主面板里 
         */
        jf.add(jpbFileLoading, BorderLayout.NORTH);
        jf.add(new JLabel("上面为常规进度条，下面为不确定模式进度条", SwingConstants.CENTER), BorderLayout.CENTER);
        jf.add(jpbFileLoadingIndeterminate, BorderLayout.SOUTH);
        jf.setSize(300, 150);
        jf.setLocationRelativeTo(null); //居中显示  
        jf.setUndecorated(true);        //禁用此窗体的装饰  
        jf.getRootPane().setWindowDecorationStyle(JRootPane.NONE); //采用指定的窗体装饰风格  
        jf.setVisible(true);
        /**
         * 不确定模式的进度条处理 
         */
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        jpbFileLoadingIndeterminate.setIndeterminate(false); //设置进度条为确定模式,即常规模式,否则那个条还会走来走去  
        jpbFileLoadingIndeterminate.setString("文件加载完毕..");
        try {
            Thread.sleep(800);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        /**
         * 关闭窗体 
         */
        jf.setVisible(false); //隐藏窗体  
        jf.dispose();         //释放资源,关闭窗体  
        jf = null;            //若不再使用了就这样  
    }


    public static void main(String[] args) {
        new JProgressBarDemo();
    }
}  