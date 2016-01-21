package testSinDemo;
import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.Random;

public class SinDemo extends JFrame {
    private double cx = 1, cy = 1;
    private double toCx = 1, toCy = 1;
    private Random rnd = new Random();
    private DecimalFormat df = new DecimalFormat("0.00");
    private SinDemo () {
        super("Sin-Demo");
        setSize(600, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        run();
    }
    private int translateX (double x) {
        return (int)(x*getWidth()/Math.PI/4 + getWidth()/2);
    }
    private int translateY (double y) {
        return (int)(getHeight()/2 - y*getWidth()/Math.PI/4);
    }
    private double sin (double x) {
        return (cy * Math.sin(cx * x));
    }
    @Override
    public void paint (Graphics g) {
        super.paint(g);
        g.setColor(Color.BLUE);
        g.drawString("y = " + df.format(cx) + " * sin( " + df.format(cy) + " * x)", 50, 50);
        g.setColor(Color.LIGHT_GRAY);
        g.drawLine(0, getHeight()/2, getWidth(), getHeight()/2);
        g.drawLine(getWidth()/2, 0, getWidth()/2, getHeight());
        g.setColor(Color.BLACK);
        for (double i=-Math.PI/cx; i<Math.PI/cx; i+=0.1)
            g.drawLine(translateX(i), translateY(sin(i)),
                    translateX(i+0.1), translateY(sin(i+0.1)));
    }
    public void run () {
        while (true) {
            if (Math.abs(cx - toCx) < 0.1) {
                toCx = rnd.nextDouble()*2;
                toCy = rnd.nextDouble()*2;
            }
            cx += (toCx - cx)/50;
            cy += (toCy - cy)/50;
            paint(getGraphics());
            try {
                Thread.sleep(80);
            } catch (InterruptedException ie) {}
        }
    }
    public static void main (String args[]) {
        new SinDemo();
    }
} 